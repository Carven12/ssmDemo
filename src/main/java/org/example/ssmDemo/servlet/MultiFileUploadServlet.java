package org.example.ssmDemo.servlet;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.example.ssmDemo.model.RequestFormModel;
import org.example.ssmDemo.utils.FormRequestUtil;
import org.example.ssmDemo.utils.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MultiFileUploadServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(MultiFileUploadServlet.class);

//    {
//        ServletConfig config = getServletConfig();
//        // 获取文件最大SIZE
//        Long maxFileSize = Strings.isNullOrEmpty(config.getInitParameter("maxFileSize"))
//                ? Long.valueOf(config.getInitParameter("maxFileSize")) : 1024 * 1024 * 1024;
//        // 获取允许上传的文件类型
//        String allowFileType = Strings.isNullOrEmpty(config.getInitParameter("maxFileSize"))
//                ? config.getInitParameter("maxFileSize") : "zip";
//    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        // 判断表单的属性
        if(!ServletFileUpload.isMultipartContent(request)) {
            // 返回错误码
        }
        RequestFormModel formModel = FormRequestUtil.parseParam(request);
        JSONObject jsonObject = new JSONObject();
        List<JSONObject> fileList = new ArrayList<>();
        formModel.getFileMap().forEach((key, value) -> {
            LOGGER.info("fileName: {}", value.getName());
            LOGGER.info("fileSize: {}", value.getSize());
            JSONObject fileObj = new JSONObject();
            fileObj.put("name", value.getName());
            fileObj.put("size", value.getSize());
            fileObj.put("type", value.getContentType());
            fileList.add(fileObj);
        });
        jsonObject.put("fileList", fileList);
        ResponseUtils.backSuccessResp(jsonObject, response);
    }

}
