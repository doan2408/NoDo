package com.example.mappingdto.Controller;

import com.example.mappingdto.Service.DonHangService;
import com.example.mappingdto.Service.KhachHangService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/donHang")
public class DonHangController {
    private final DonHangService donHangService;
    private final KhachHangService khachHangService;

    public DonHangController(DonHangService donHangService, KhachHangService khachHangService) {
        this.donHangService = donHangService;
        this.khachHangService = khachHangService;
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

    @GetMapping("/search1")
    ResponseEntity<?> searchMaDon1(@RequestParam(value = "maDon", required = false) String maDon,
                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "size", defaultValue = "2") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(donHangService.searchDto(maDon, pageable));
    }

    @GetMapping("/search2")
    ResponseEntity<?> searchMaDon2(@RequestParam(value = "maDon", required = false) String maDon,
                                   @RequestParam(value = "moTa", required = false) String moTa,
                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "size", defaultValue = "2") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(donHangService.searchDtoJpql(maDon, moTa, pageable));
    }


    @GetMapping("/hehe")
    ResponseEntity<?> hehe() {
//        khachHangService.testLazyInitializationException1();
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
