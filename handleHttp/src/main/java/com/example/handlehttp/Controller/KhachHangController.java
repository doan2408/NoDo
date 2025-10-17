package com.example.handlehttp.Controller;

import com.example.handlehttp.Dto.KhachHangResponse;
import com.example.handlehttp.Model.KhachHang;
import com.example.handlehttp.Projection.KhachHangProjection;
import com.example.handlehttp.Service.KhachHangService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/client")
public class KhachHangController {
    private final KhachHangService khachHangService;

//    @GetMapping
//    ResponseEntity<List<KhachHang>> getKhachHang() {
//        return ResponseEntity.ok(khachHangService.getClient());
//    }
//
//    @PutMapping
//    ResponseEntity<KhachHang> updateKhachHang(@RequestBody KhachHang khachHang) {
//        return ResponseEntity.ok(khachHangService.update(khachHang));
//    }
//
//    @PostMapping
//    ResponseEntity<KhachHang> addKhachHang(@RequestBody KhachHang khachHang) {
//        return ResponseEntity.ok(khachHangService.create(khachHang));
//    }

//    @GetMapping("/search")
//    ResponseEntity<Page<Object[]>> search(@RequestParam(value = "name", required = false) String name,
//                                          @RequestParam(value = "soDu", required = false) Double soDu,
//                                          @RequestParam(value = "tyLe", required = false) BigDecimal tyLe,
//                                          @RequestParam(value = "page", defaultValue = "0") int page,
//                                          @RequestParam(value = "size", defaultValue = "2") int size
//    ) {
//        Pageable pageable = PageRequest.of(page, size);
//        Page<Object[]> list = khachHangService.searchKhachHang(name, soDu, tyLe, pageable);
////        for (Object[] row : list) {
////            System.out.println("ten: " + row[0] + ",luong: " + row[1]);
////        }
//        return ResponseEntity.ok(list);
//    }

    @GetMapping("/projection")
    ResponseEntity<Page<KhachHangProjection>> projection(@RequestParam(value = "name", required = false) String name,
                                                         @RequestParam(value = "soDu", required = false) Double soDu,
                                                         @RequestParam(value = "tyLe", required = false) BigDecimal tyLe,
                                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                                         @RequestParam(value = "size", defaultValue = "2") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<KhachHangProjection> list = khachHangService.timKiemProjection(name, soDu, tyLe, pageable);
//        System.out.println(list);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/dto")
    ResponseEntity<Page<KhachHangResponse>> searchDto(@RequestParam(value = "name", required = false) String name,
                                                      @RequestParam(value = "soDu", required = false) Double soDu,
                                                      @RequestParam(value = "tyLe", required = false) BigDecimal tyLe,
                                                      @RequestParam(value = "page", defaultValue = "0") int page,
                                                      @RequestParam(value = "size", defaultValue = "2") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<KhachHangResponse> list = khachHangService.searchDto(name, soDu, tyLe, pageable);
//        System.out.println(list);
        return ResponseEntity.ok(list);
    }

    //
    @GetMapping("/sql")
    ResponseEntity<Page<Map<String, Object>>> timKiemNhieuDieuKienSQL(@RequestParam(value = "name", required = false) String name,
                                                                      @RequestParam(value = "soDu", required = false) Double soDu,
                                                                      @RequestParam(value = "tyLe", required = false) BigDecimal tyLe,
                                                                      @RequestParam(value = "page", defaultValue = "0") int page,
                                                                      @RequestParam(value = "size", defaultValue = "2") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Map<String, Object>> list = khachHangService.timKiemNhieuDieuKienSQL(name, soDu, tyLe, pageable);
//        System.out.println(list);
        return ResponseEntity.ok(list);
    }

    //
    @GetMapping("/criteria")
    public ResponseEntity<Page<KhachHangResponse>> searchKhachHang(
            @RequestParam(required = false) String ten,
            @RequestParam(required = false) Double minSoDu,
            @RequestParam(required = false) Double maxSoDu,
            @RequestParam(value = "tyLe", required = false) BigDecimal tyLe,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<KhachHangResponse> result = khachHangService.criteria(minSoDu, maxSoDu, tyLe, pageable);
        return ResponseEntity.ok(result);
    }
//
//
//    @GetMapping("/find")
//    ResponseEntity<List<Object[]>> findTenVaLuong() {
//        List<Object[]> list = khachHangService.findTenVaLuong();
//        for (Object[] row : list) {
//            System.out.println("ten: " + row[0] + ",luong: " + row[1]);
//        }
//        return ResponseEntity.ok(khachHangService.findTenVaLuong());
//    }

//    @GetMapping
//    ResponseEntity<List<KhachHang>> getAllKhachHang(
//            @RequestParam("name") String name
//    ) {
//        return ResponseEntity.ok(khachHangService.findByName(name));
//    }

    @GetMapping
    ResponseEntity<Page<KhachHang>> getAllKhachHang(
            @RequestParam("name") String name,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "2") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(khachHangService.findByName(name, pageable));
    }

    @GetMapping("/name")
    ResponseEntity<List<KhachHang>> getName(@RequestParam("name") String name) {
        return ResponseEntity.ok(khachHangService.getName(name));
    }

    @GetMapping("/count")
    ResponseEntity<?> count(@RequestParam("name") String name) {
        return ResponseEntity.ok(khachHangService.count(name));
    }

}
