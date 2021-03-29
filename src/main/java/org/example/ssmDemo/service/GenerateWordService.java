package org.example.ssmDemo.service;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.example.ssmDemo.constants.CommonConstants;
import org.example.ssmDemo.constants.ExcelConstants;
import org.example.ssmDemo.constants.FileConstants;
import org.example.ssmDemo.controller.GenerateWordController;
import org.example.ssmDemo.entity.FileEntity;
import org.example.ssmDemo.utils.ExcelUtils;
import org.example.ssmDemo.utils.FileUtils;
import org.example.ssmDemo.utils.IdCardUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 生成word service类
 * ClassName: GenerateWordService
 * date: 2021/3/28 20:18
 *
 * @author liangc
 * @version 1.0
 */
@Service
public class GenerateWordService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateWordService.class);

    /**
     * 根据sheet页内容生成文件流List
     * @param sheet sheet页
     * @return 文件流List
     * @throws FileNotFoundException
     */
    public List<FileEntity> getWordStreamList(XSSFSheet sheet) throws FileNotFoundException {
        List<FileEntity> outputStreamList = new ArrayList<>();

        String sheetName = sheet.getSheetName();
        List<Map<String, String>> dataList = this.getDataListFromSheet(sheet);

        // 获取模板文件位置
        String templatePath = ResourceUtils.getFile("classpath:static/template/" + sheetName + ".docx").getPath();

        dataList.stream().forEach(map -> {
            try {
                String tempFileName = sheetName + FileConstants.FILE_UNDERLINE + map.get(ExcelConstants.EXCEL_TITLE_ROOM_NUMBER)
                        + FileConstants.FILE_UNDERLINE + map.get(ExcelConstants.EXCEL_TITLE_PERSON_NAME)
                        + FileConstants.FILE_SPOT + FileConstants.FILE_WORD_TYPE;
                ByteArrayOutputStream byteArrayOutputStream = FileUtils.generateDoc(templatePath, map, tempFileName);
                outputStreamList.add(new FileEntity(tempFileName, byteArrayOutputStream));
            } catch (IOException e) {
                LOGGER.error("使用模板文件生成失败！", e);
            }
        });

        return outputStreamList;
    }

    /**
     * 获取sheet中的数据
     * @param sheet sheet页
     * @return sheet中的数据
     */
    private List<Map<String, String>> getDataListFromSheet(XSSFSheet sheet) {
        ArrayList<Map<String, String>> dataList = new ArrayList<>();

        //默认第一行为标题行，i = 0
        XSSFRow titleRow = sheet.getRow(0);
        // 循环获取每一行数据
        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            XSSFRow row = sheet.getRow(i);
            LinkedHashMap<String, String> dataMap = new LinkedHashMap<>();

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
                    dataMap.put(ExcelUtils.getString(titleCell), cellValue);
                } else if (ExcelConstants.EXCEL_TITLE_ARREARS_PERIOD.equals(ExcelUtils.getString(titleCell))) {
                    String[] strArr = cellValue.split(FileConstants.FILE_BAR);
                    String startDate = strArr[0].replace(FileConstants.FILE_SPOT, ExcelConstants.EXCEL_CONTENT_YEAR) + ExcelConstants.EXCEL_CONTENT_MONTH;
                    String endDate = strArr[1].replace(FileConstants.FILE_SPOT, ExcelConstants.EXCEL_CONTENT_YEAR) + ExcelConstants.EXCEL_CONTENT_MONTH;
                    dataMap.put(ExcelConstants.EXCEL_TITLE_START_DATE, startDate);
                    dataMap.put(ExcelConstants.EXCEL_TITLE_END_DATE, endDate);
                } else if (ExcelConstants.EXCEL_TITLE_PERSON_ID.equals(ExcelUtils.getString(titleCell))) {
                    // 身份证号特殊处理
                    if (!IdCardUtils.isValid(cellValue)) {
                        cellValue = CommonConstants.UNKNOWN_INFO;

                        // 根据根据身份证号获取性别
                        String sex = CommonConstants.UNKNOWN_INFO;
                        dataMap.put(ExcelConstants.EXCEL_TITLE_SEX, sex);

                        // 根据身份证号获取出生日期
                        String birthDay = CommonConstants.UNKNOWN_INFO;
                        dataMap.put(ExcelConstants.EXCEL_TITLE_BIRTHDAY, birthDay);
                    } else {
                        // 根据根据身份证号获取性别
                        String sex = IdCardUtils.getSex(cellValue);
                        dataMap.put(ExcelConstants.EXCEL_TITLE_SEX, sex);

                        // 根据身份证号获取出生日期
                        String birthDay = IdCardUtils.getBirthday(cellValue);
                        dataMap.put(ExcelConstants.EXCEL_TITLE_BIRTHDAY, birthDay);
                    }

                    dataMap.put(ExcelUtils.getString(titleCell), cellValue);
                } else {
                    dataMap.put(ExcelUtils.getString(titleCell), cellValue);
                }
            }

            for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                buffer.append(entry.getKey()).append(": ").append(entry.getValue()).append(" ");
            }
            LOGGER.info(buffer.toString());
            if (dataMap.isEmpty()) {
                continue;
            }
            dataList.add(dataMap);
        }

        return  dataList;
    }
}
