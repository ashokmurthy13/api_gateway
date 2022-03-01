package com.kadmos.api.gateway.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/savings-fallback")
public class FallbackController {

    @GetMapping("")
    public String fallback() {
        return "Server is not reachable. Please try after sometime.";
    }
}
