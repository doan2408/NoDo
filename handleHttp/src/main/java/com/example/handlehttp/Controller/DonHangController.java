package com.example.handlehttp.Controller;

import com.example.handlehttp.Model.DonHang;
import com.example.handlehttp.Service.DonHangService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/donHang")
@RequiredArgsConstructor
public class DonHangController {
    private final DonHangService donHangService;

    @GetMapping
    ResponseEntity <List<DonHang>> findAll() {
        return ResponseEntity.ok(donHangService.getDonHang());
    }

    @PutMapping
    ResponseEntity <DonHang> update(@RequestBody DonHang donHang) {
        donHang.setNgayDat(Instant.now());
        donHang.setGioDat(LocalTime.now());
        return ResponseEntity.ok(donHangService.updateDonHang(donHang));
    }
}
