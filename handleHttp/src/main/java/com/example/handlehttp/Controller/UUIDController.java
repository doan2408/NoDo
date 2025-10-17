package com.example.handlehttp.Controller;

import com.example.handlehttp.Model.UuidIdEntity;
import com.example.handlehttp.Service.UUIDService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/uuid")
@RequiredArgsConstructor
public class UUIDController {
    private final UUIDService uuidService;

    @PostMapping
    public String saveUuidEntity(@RequestBody UuidIdEntity uuidIdEntity) {
        uuidService.saveUuidEntity(uuidIdEntity);
        return "Entity saved";
    }

    // API để lấy một entity theo ID
    @GetMapping("/{id}")
    public UuidIdEntity getUuidEntity(@PathVariable String id) {
        return uuidService.getUuidEntity(id);
    }
}
