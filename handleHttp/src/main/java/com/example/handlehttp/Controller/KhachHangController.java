package com.example.handlehttp.Controller;

import com.example.handlehttp.Model.KhachHang;
import com.example.handlehttp.Service.KhachHangService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/client")
public class KhachHangController {
    private final KhachHangService khachHangService;

    @GetMapping
    ResponseEntity <List<KhachHang>> getKhachHang() {
        return ResponseEntity.ok(khachHangService.getClient());
    }

    @PutMapping
    ResponseEntity <KhachHang> updateKhachHang(@RequestBody KhachHang khachHang) {
        return ResponseEntity.ok(khachHangService.update(khachHang));
    }

}
