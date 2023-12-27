package com.ovelychko.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RestController
public class HelloWorldController {

    @GetMapping(value = { "/", "/hello"})
    public String hello(Principal principal) {
        log.info("hello, principal: " + String.valueOf(principal));
        return "Hello World";
    }

    @GetMapping(value = "/error")
    public String error(Principal principal) {
        log.info("error, principal: " + String.valueOf(principal));
        return "error happened";
    }

}
