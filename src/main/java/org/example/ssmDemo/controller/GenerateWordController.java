package org.example.ssmDemo.controller;

import org.example.ssmDemo.constants.CommonConstants;
import org.example.ssmDemo.constants.ExcelConstants;
import org.example.ssmDemo.constants.FileConstants;
import org.example.ssmDemo.entity.FileEntity;
import org.example.ssmDemo.utils.ExcelUtils;
import org.example.ssmDemo.utils.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * <p>
 * ClassName: GenerateWordController
 * date: 2021/3/27 18:02
 *
 * @author liangc
 * @version 1.0
 */
@Controller
@ResponseBody
@RequestMapping(value = "/v1/api")
public class GenerateWordController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateWordController.class);

    @RequestMapping(value = "/generateWord", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<byte[]> generate(@RequestParam("file") MultipartFile file) throws Exception {
        LOGGER.info("file name: {}", file.getOriginalFilename());
        LOGGER.info("file size: {}", file.getSize());
        LOGGER.info("file type: {}", file.getContentType());
        // 文件名
        String fileName = file.getOriginalFilename();
        fileName = fileName.substring(0, fileName.lastIndexOf(FileConstants.FILE_SPOT));
        InputStream in = new FileInputStream(FileUtils.multipartFileToFile(file));

        // 读取整个Excel
        XSSFWorkbook sheets = new XSSFWorkbook(in);
        // 获取第一个表单Sheet
        XSSFSheet sheetAt = sheets.getSheetAt(0);
        ArrayList<Map<String, String>> dataList = new ArrayList<>();

        //默认第一行为标题行，i = 0
        XSSFRow titleRow = sheetAt.getRow(0);
        // 循环获取每一行数据
        for (int i = 1; i < sheetAt.getPhysicalNumberOfRows(); i++) {
            XSSFRow row = sheetAt.getRow(i);
            LinkedHashMap<String, String> map = new LinkedHashMap<>();

            StringBuffer buffer = new StringBuffer();
            // 读取每一格内容
            for (int index = 0; index < row.getPhysicalNumberOfCells(); index++) {
                XSSFCell titleCell = titleRow.getCell(index);
                XSSFCell cell = row.getCell(index);
                String cellValue = ExcelUtils.getString(cell);

                // 如果单元格内容为空，设置内容为不详
                if (CommonConstants.EMPTY_STRING.equals(cellValue)) {
                    cellValue = CommonConstants.UNKNOWN_INFO;
                }

                // 内容特殊处理
                if (ExcelConstants.EXCEL_TITLE_ROOM_NUMBER.equals(ExcelUtils.getString(titleCell))) {
                    String[] strArr = cellValue.split(FileConstants.FILE_BAR);
                    cellValue = strArr[0] + ExcelConstants.EXCEL_CONTENT_DONG + strArr[1] + ExcelConstants.EXCEL_CONTENT_ROOM;
//                    cellValue.replace(FileConstants.FILE_BAR, ExcelConstants.EXCEL_CONTENT_DONG);
//                    cellValue = cellValue + ExcelConstants.EXCEL_CONTENT_ROOM;
                    map.put(ExcelUtils.getString(titleCell), cellValue);
                } else if (ExcelConstants.EXCEL_TITLE_ARREARS_PERIOD.equals(ExcelUtils.getString(titleCell))) {
                    String[] strArr = cellValue.split(FileConstants.FILE_BAR);
                    String startDate = strArr[0].replace(FileConstants.FILE_SPOT, ExcelConstants.EXCEL_CONTENT_YEAR) + ExcelConstants.EXCEL_CONTENT_MONTH;
                    String endDate = strArr[1].replace(FileConstants.FILE_SPOT, ExcelConstants.EXCEL_CONTENT_YEAR) + ExcelConstants.EXCEL_CONTENT_MONTH;
                    map.put(ExcelConstants.EXCEL_TITLE_START_DATE, startDate);
                    map.put(ExcelConstants.EXCEL_TITLE_END_DATE, endDate);
                } else {
                    map.put(ExcelUtils.getString(titleCell), cellValue);
                }
            }

            for (Map.Entry<String, String> entry : map.entrySet()) {
                buffer.append(entry.getKey()).append(": ").append(entry.getValue()).append(" ");
            }
            LOGGER.info(buffer.toString());
            if (map.isEmpty()) {
                continue;
            }
            dataList.add(map);
        }

        // 关闭文件流
        sheets.close();

        // 获取模板文件位置
        String templatePath = ResourceUtils.getFile("classpath:static/template/templateDoc.docx").getPath();

        List<FileEntity> outputStreamList = new ArrayList<>();

        dataList.stream().forEach(map -> {
            try {
                String tempFileName = map.get(ExcelConstants.EXCEL_TITLE_ROOM_NUMBER)
                        + FileConstants.FILE_UNDERLINE + map.get(ExcelConstants.EXCEL_TITLE_PERSON_NAME)
                        + FileConstants.FILE_SPOT + FileConstants.FILE_WORD_TYPE;
                ByteArrayOutputStream byteArrayOutputStream = FileUtils.generateDoc(templatePath, map, tempFileName);
                outputStreamList.add(new FileEntity(tempFileName, byteArrayOutputStream));
            } catch (IOException e) {
                LOGGER.error("使用模板文件生成失败！", e);
            }
        });

















        ByteArrayOutputStream byteArrayOutputStream = FileUtils.zipFile(outputStreamList);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.add("Content-Disposition", "attachment;filename=" + fileName + ".zip");
        return new ResponseEntity<byte[]>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.CREATED);
    }
}
