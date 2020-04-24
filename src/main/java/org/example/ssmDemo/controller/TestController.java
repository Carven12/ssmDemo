package org.example.ssmDemo.controller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/v1/api")
public class TestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @RequestMapping(value="hello", method= RequestMethod.GET)
    @ApiOperation(value = "接口测试", notes = "接口测试11111")
    public String hello() {
        LOGGER.info("HelloWorld!");
        return "success";
    }

}
