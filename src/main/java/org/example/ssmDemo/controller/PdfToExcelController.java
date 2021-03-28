package org.example.ssmDemo.controller;

import org.example.ssmDemo.utils.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.io.*;

/**
 * @author liangc
 */
@Controller
@ResponseBody
@RequestMapping(value = "/v1/api")
public class PdfToExcelController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PdfToExcelController.class);


    @RequestMapping(value = "/aliBills", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<byte[]> tansAliBillsToExcel(@RequestParam("file") MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        fileName = fileName.substring(0, fileName.lastIndexOf("."));
        LOGGER.info("file name: {}", file.getOriginalFilename());
        LOGGER.info("file size: {}", file.getSize());
        LOGGER.info("file type: {}", file.getContentType());

        PDDocument doc = PDDocument.load(FileUtils.multipartFileToFile(file));
        // 获取总页数
        int pageNumber = doc.getNumberOfPages();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Writer writer = null;

        try {
            // 文件按字节读取，然后按照UTF-8的格式编码显示
            writer = new OutputStreamWriter(byteArrayOutputStream, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 生成PDF文档内容剥离器
        PDFTextStripper stripper = new PDFTextStripper();
        // 排序
        stripper.setSortByPosition(true);
        // 设置转换的开始页
        stripper.setStartPage(1);
        // 设置转换的结束页
        stripper.setEndPage(pageNumber);
        try {
            stripper.writeText(doc, writer);
            writer.close();
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.add("Content-Disposition", "attachment;filename=" + fileName + ".doc");
        return new ResponseEntity<byte[]>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.CREATED);
    }
}
