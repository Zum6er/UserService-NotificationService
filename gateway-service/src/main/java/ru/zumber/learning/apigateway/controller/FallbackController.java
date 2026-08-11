package ru.zumber.learning.apigateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/fallback/user")
    public ResponseEntity<String> fallbackUser() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("User-service unavailable");
    }

    @RequestMapping(value = "/fallback/notification",
            method = RequestMethod.POST)
    public ResponseEntity<String> fallbackNotification() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Notification service unavailable");

    }
}
