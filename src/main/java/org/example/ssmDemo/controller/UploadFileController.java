package org.example.ssmDemo.controller;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value="/v1/api")
public class UploadFileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadFileController.class);

    /**
     * 单文件上传功能测试
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public JSONObject uploadFile(@RequestParam("file") MultipartFile file) {
        LOGGER.info("file name: {}", file.getOriginalFilename());
        LOGGER.info("file size: {}", file.getSize());
        LOGGER.info("file type: {}", file.getContentType());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", file.getOriginalFilename());
        jsonObject.put("size", file.getSize());
        jsonObject.put("type", file.getContentType());
        return jsonObject;
    }

    /**
     * 多文件上传功能测试
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/uploadFiles", method = RequestMethod.POST)
    public JSONObject uploadFiles(@RequestParam("files") MultipartFile[] files) {
        JSONObject jsonObject = new JSONObject();
        List<JSONObject> fileList = new ArrayList<>();
        for (MultipartFile file : files) {
            LOGGER.info("file name: {}", file.getOriginalFilename());
            LOGGER.info("file size: {}", file.getSize());
            LOGGER.info("file type: {}", file.getContentType());
            JSONObject fileObj = new JSONObject();
            fileObj.put("name", file.getOriginalFilename());
            fileObj.put("size", file.getSize());
            fileObj.put("type", file.getContentType());
            fileList.add(fileObj);
        }
        jsonObject.put("fileList", fileList);
        return jsonObject;
    }
}
