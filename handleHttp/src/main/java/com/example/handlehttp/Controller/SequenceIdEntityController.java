package com.example.handlehttp.Controller;

import com.example.handlehttp.Model.SequenceIdEntity;
import com.example.handlehttp.Service.SequenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sequence")
@RequiredArgsConstructor
public class SequenceIdEntityController {

    private final SequenceService sequenceService;


    // API để lưu một entity mới
    @PostMapping
    public String saveSequenceIdEntity() {
        sequenceService.saveSequenceIdEntity();
        return "Entity saved!";
    }

    // API để lấy một entity theo ID
    @GetMapping("/{id}")
    public SequenceIdEntity getSequenceIdEntity(@PathVariable Integer id) {
        return sequenceService.getSequenceIdEntity(id);
    }
}
