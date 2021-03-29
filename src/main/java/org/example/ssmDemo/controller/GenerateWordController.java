package org.example.ssmDemo.controller;

import org.example.ssmDemo.constants.FileConstants;
import org.example.ssmDemo.entity.FileEntity;
import org.example.ssmDemo.service.GenerateWordService;
import org.example.ssmDemo.utils.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private GenerateWordService generateWordService;

    @RequestMapping(value = "/generateWord", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<byte[]> generate(@RequestParam("file") MultipartFile file) throws Exception {
        LOGGER.info("file name: {}", file.getOriginalFilename());
        LOGGER.info("file size: {}", file.getSize());
        LOGGER.info("file type: {}", file.getContentType());
        // 文件名
        String fileName = file.getOriginalFilename();
        assert fileName != null;
        fileName = fileName.substring(0, fileName.lastIndexOf(FileConstants.FILE_SPOT));
        InputStream in = new FileInputStream(FileUtils.multipartFileToFile(file));

        // 读取整个Excel
        XSSFWorkbook sheets = new XSSFWorkbook(in);

        // 生成的文件流List
        List<FileEntity> outputStreamList = new ArrayList<>();

        for (int i = 0; i < sheets.getNumberOfSheets(); i++) {
            outputStreamList.addAll(generateWordService.getWordStreamList(sheets.getSheetAt(i)));
        }

        // 关闭文件流
        sheets.close();

        // 批量压缩文件
        ByteArrayOutputStream byteArrayOutputStream = FileUtils.zipFile(outputStreamList);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.add("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8"),"ISO-8859-1") + ".zip");
        return new ResponseEntity<>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.CREATED);
    }
}
