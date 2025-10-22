package com.example.mappingdto.Controller;

import com.example.mappingdto.Service.DonHangService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/donHang")
public class DonHangController {
    private final DonHangService donHangService;

    public DonHangController(DonHangService donHangService) {
        this.donHangService = donHangService;
    }

    @GetMapping("/manual")
    public ResponseEntity<?> manual() {
        return ResponseEntity.ok(donHangService.manual());
    }

    @GetMapping("/mapStruct")
    ResponseEntity<?> getAllDonHang() {
        return ResponseEntity.ok(donHangService.mapStruct());
    }

    @GetMapping("/modelMapper")
    ResponseEntity<?> modelMapper() {
        return ResponseEntity.ok(donHangService.modelMapper());
    }

    @GetMapping("/beanUtils")
    ResponseEntity<?> beanUtils() {
        return ResponseEntity.ok(donHangService.beanUtils());
    }

    @GetMapping("/converter")
    ResponseEntity<?> converter() {
        return ResponseEntity.ok(donHangService.converter());
    }

    @GetMapping("/builderPattern")
    ResponseEntity<?> builderPattern() {
        return ResponseEntity.ok(donHangService.builderPattern());
    }

    @GetMapping("/search")
    ResponseEntity<?> searchMaDon(@RequestParam("maDon") String maDon) {
        return ResponseEntity.ok(donHangService.search(maDon));
    }

}
