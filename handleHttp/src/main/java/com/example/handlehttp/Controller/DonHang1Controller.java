//package com.example.handlehttp.Controller;
//
//import com.example.handlehttp.Model.DonHang;
//import com.example.handlehttp.Service.DonHangService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.Instant;
//import java.time.LocalTime;
//import java.util.List;
//
//@RestController
//@RequestMapping("/donHang1")
//public class DonHang1Controller {
//    private final DonHangService donHangService;
//
//     @Autowired
//    @Qualifier("getDonHang")
//     DonHang donHang;
//
//    public DonHang1Controller(DonHangService donHangService) {
//        this.donHangService = donHangService;
//    }
//
//    @GetMapping("")
//    ResponseEntity<Object> findAll() {
//
//        return ResponseEntity.ok(donHang);
//    }
//
//    @GetMapping("/param")
//    ResponseEntity<DonHang> findById(@RequestParam Integer id) {
//        return ResponseEntity.ok(null);
//    }
//
//    @PutMapping
//    ResponseEntity <DonHang> update(@RequestBody DonHang donHang) {
//        donHang.setNgayDat(Instant.now());
//        donHang.setGioDat(LocalTime.now());
//        return ResponseEntity.ok(donHangService.updateDonHang(donHang));
//    }
//}
