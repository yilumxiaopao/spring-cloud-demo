package com.cloud.consumer.feign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wei.li
 * on 2019/2/26
 */

@RestController
public class HelloController {

    @Autowired
    IHelloService helloService;

    @GetMapping(value = "hello")
    public String hello(@RequestParam String name){
        System.out.println("222222");
        return helloService.hello(name);
    }
}
