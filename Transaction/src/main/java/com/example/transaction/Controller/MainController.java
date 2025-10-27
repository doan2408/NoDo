package com.example.transaction.Controller;

import com.example.transaction.Service.LogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
public class LogController {
    private final LogService logService;
    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping
    ResponseEntity<?> log() {
        logService.saveLog("Name");
        return ResponseEntity.ok().build();
    }
}
