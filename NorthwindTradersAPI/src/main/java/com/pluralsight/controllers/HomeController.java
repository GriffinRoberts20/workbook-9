package com.pluralsight.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home(@RequestParam(defaultValue = "World") String country){
        return "Hello "+country+"!";
    }
}
