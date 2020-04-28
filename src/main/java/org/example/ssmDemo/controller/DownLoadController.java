package org.example.ssmDemo.controller;

import org.example.ssmDemo.utils.CommonUtils;
import org.example.ssmDemo.utils.VerifyCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.awt.image.BufferedImage;

@Controller
@RequestMapping(value="/v1/api")
public class DownLoadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DownLoadController.class);

    @RequestMapping(value = "/getVerifyCode", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getVerifyCode() {
        String verifyCode = VerifyCode.genRandomCode();
        LOGGER.info("verifyCode: {}", verifyCode);
        BufferedImage image = VerifyCode.genVerifyCodeImage(verifyCode);
        byte[] imgByte = CommonUtils.imageToBytes(image, "png");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.add("Content-Disposition", "attachment;filename=demo.png");
        return new ResponseEntity<byte[]>(imgByte, headers, HttpStatus.CREATED);
    }

}
