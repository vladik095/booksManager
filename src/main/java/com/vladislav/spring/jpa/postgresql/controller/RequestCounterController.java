package com.vladislav.spring.jpa.postgresql.controller;

import com.vladislav.spring.jpa.postgresql.service.RequestCounterService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/counter")
public class RequestCounterController {

    private final RequestCounterService requestCounterService;

    public RequestCounterController(RequestCounterService requestCounterService) {
        this.requestCounterService = requestCounterService;
    }

    @GetMapping("/total")
    public long getTotalRequestCount() {
        return requestCounterService.getTotalRequestCount();
    }
}
