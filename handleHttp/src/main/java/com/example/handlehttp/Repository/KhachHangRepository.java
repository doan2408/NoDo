package com.example.handlehttp.Repository;

import com.example.handlehttp.Dto.KhachHangResponse;
import com.example.handlehttp.Model.KhachHang;
import com.example.handlehttp.Projection.KhachHangProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang, Integer> {

//    @Query(value = "select kh.soDu, kh.tyLe from KhachHang kh where " +
//            "(:tenKh is null or kh.tenKH like %:tenKh%) and " +
//            "(:soDu is null or kh.soDu = :soDu) and " +
//            "(:tyLe is null or kh.tyLe = :tyLe)")
//    Page<Object[]> searchKhachHang(
//            @Param("tenKh") String tenKh,
//            @Param("soDu") Double soDu,
//            @Param("tyLe") BigDecimal tyLe,
//            Pageable pageable);
//
//
//    // projection
//    @Query("""
//                SELECT k.soDu as soDu, k.tyLe as tyLe
//                FROM KhachHang k
//                WHERE (:ten IS NULL OR k.tenKH LIKE %:ten%)
//                  AND (:soDu IS NULL OR k.soDu = :soDu)
//                  AND (:tyLe IS NULL OR k.tyLe = :tyLe)
//            """)
//    Page<KhachHangProjection> projection(
//            @Param("ten") String ten,
//            @Param("soDu") Double soDu,
//            @Param("tyLe") BigDecimal tyLe,
//            Pageable pageable);
//
//
//    // dto
//    @Query("SELECT new com.example.handlehttp.Dto.KhachHangResponse(kh.soDu, kh.tyLe) " +
//            "FROM KhachHang kh WHERE " +
//            "(:tenKh IS NULL OR kh.tenKH LIKE %:tenKh%) AND " +
//            "(:soDu IS NULL OR kh.soDu = :soDu) AND " +
//            "(:tyLe IS NULL OR kh.tyLe = :tyLe)")
//    Page<KhachHangResponse> dto(
//            @Param("tenKh") String tenKh,
//            @Param("soDu") Double soDu,
//            @Param("tyLe") BigDecimal tyLe,
//            Pageable pageable);
//
//
//    // native sql
//    @Query(value = """
//                SELECT SoDu AS soDu, TyLe AS tyLe
//                FROM khachhang
//                WHERE (:TenKH IS NULL OR TenKH LIKE %:TenKH%)
//                  AND (:SoDu IS NULL OR SoDu = :SoDu)
//                  AND (:TyLe IS NULL OR TyLe = :TyLe)
//            """,
//            countQuery = """
//                        SELECT COUNT(*) FROM khachhang
//                        WHERE (:TenKH IS NULL OR TenKH LIKE %:TenKH%)
//                          AND (:SoDu IS NULL OR SoDu = :SoDu)
//                          AND (:TyLe IS NULL OR TyLe = :TyLe)
//                    """,
//            nativeQuery = true)
//    Page<Map<String, Object>> timKiemNhieuDieuKienSQL(
//            @Param("TenKH") String TenKH,
//            @Param("SoDu") Double SoDu,
//            @Param("TyLe") BigDecimal TyLe,
//            Pageable pageable);
//
//
//    @Query("SELECT n.tenKH, n.tyLe FROM KhachHang n")
//    List<Object[]> findTenVaLuong();

    List<KhachHang> findByTenkh(String tenKh);

}
