package org.example.ssmDemo.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.example.ssmDemo.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="/v1/api")
public class TestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @RequestMapping(value="/hello", method= RequestMethod.GET)
    @ApiOperation(value = "接口测试", notes = "接口测试11111")
    public String hello() {
        LOGGER.info("HelloWorld!");
        return "success";
    }

    @ResponseBody
    @RequestMapping(value = "/testGetMethod", method = RequestMethod.GET)
    public Person testGetMethod(String name, Integer age, String sex) {
        Person person = new Person();
        person.setName(name);
        person.setAge(age);
        person.setSex(sex);
        LOGGER.info("person: {}", person);
        return person;
    }

    @ResponseBody
    @RequestMapping(value = "/testRestful/{id}", method = RequestMethod.GET)
    public JSONObject testRestful(@PathVariable String id) {
        LOGGER.info("id: {}", id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 200);
        jsonObject.put("msg", "success");
        return jsonObject;
    }

    @ResponseBody
    @RequestMapping(value = "/testPostMethod", method = RequestMethod.POST)
    public Person testPostMethod(Person person) {
        LOGGER.info("person: {}", person);
        return person;
    }

    @ResponseBody
    @RequestMapping(value = "/testPutMethod", method = RequestMethod.PUT)
    public Person testPutMethod(Person person) {
        LOGGER.info("person: {}", person);
        return person;
    }



}
