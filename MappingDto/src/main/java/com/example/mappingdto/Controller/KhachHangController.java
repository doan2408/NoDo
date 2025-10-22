package com.example.mappingdto.Controller;

import com.example.mappingdto.Dto.KhachHangDTO;
import com.example.mappingdto.Service.KhachHangService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class KhachHangController {
    private final KhachHangService khachHangService;

    public KhachHangController(KhachHangService khachHangService) {
        this.khachHangService = khachHangService;
    }

    @GetMapping("/manual")
    ResponseEntity<?> manual() {
        return ResponseEntity.ok(khachHangService.manual());
    }

    @GetMapping("/mapStruct")
    ResponseEntity<?> mapStruct() {
        return ResponseEntity.ok(khachHangService.mapStruct());
    }

    @GetMapping("/modelMapper")
    ResponseEntity<?> modelMapper() {
        return ResponseEntity.ok(khachHangService.modelMapper());
    }

    @GetMapping("/beanUtils")
    ResponseEntity<?> beanUtils() {
        return ResponseEntity.ok(khachHangService.beanUtils());
    }

    @PostMapping
    ResponseEntity<?> addKhachHang(@RequestBody KhachHangDTO dto) {
        return ResponseEntity.ok(khachHangService.addKhachHang(dto));
    }

}
