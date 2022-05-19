package com.ovelychko.controller;

import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Log
@RestController
public class HelloWorldController {

    @RequestMapping({"/hello"})
    public String hello(Principal principal) {
        log.info(String.valueOf(principal));
        return "Hello World";
    }
}
