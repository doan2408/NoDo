package com.example.mappingdto.Controller;

import com.example.mappingdto.Dto.KhachHangDTO;
import com.example.mappingdto.Enum.Status;
import com.example.mappingdto.Service.KhachHangService;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/mapStruct1/{id}")
    ResponseEntity<?> mapStruct1(@PathVariable Integer id) {
        return ResponseEntity.ok(khachHangService.mapStruct1(id));
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

    //
    @GetMapping
    ResponseEntity<?> getAllKhachHang() {
        return ResponseEntity.ok(khachHangService.getKhachHangList());
    }

    @PostMapping("/entity")
    ResponseEntity<?> persistKhachHangWithDonHang() {
        khachHangService.persistKhachHangWithDonHang();
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/entity/{khId}/{dhId}")
    ResponseEntity<?> deleteKhachHangWithDonHang(
            @PathVariable int khId,
            @PathVariable int dhId
    ) {
        khachHangService.removeDonHangFromKhachHang(khId, dhId);
        return ResponseEntity.ok("xóa thành công");
    }

    @GetMapping("/hehe")
    ResponseEntity<?> getKhachHangById() {
        return ResponseEntity.ok(khachHangService.getKhachHang());
    }
}
