package com.example.handlehttp.Repository;

import com.example.handlehttp.Dto.KhachHangResponse;
import com.example.handlehttp.Model.KhachHang;
import com.example.handlehttp.Projection.KhachHangProjection;
import jakarta.persistence.NamedQuery;
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

    @Query(value = "select kh.soDu, kh.tyLe from KhachHang kh where " +
            "(:tenKh is null or kh.tenkh like %:tenKh%) and " +
            "(:soDu is null or kh.soDu = :soDu) and " +
            "(:tyLe is null or kh.tyLe = :tyLe)")
    Page<Object[]> searchKhachHang(
            @Param("tenKh") String tenKh,
            @Param("soDu") Double soDu,
            @Param("tyLe") BigDecimal tyLe,
            Pageable pageable);
//
//
//    // projection
    @Query(value = """
                SELECT k.soDu as soDu, k.tyLe as tyLe
                FROM KhachHang k
                WHERE (:ten IS NULL OR k.tenkh LIKE %:ten%)
                  AND (:soDu IS NULL OR k.soDu = :soDu)
                  AND (:tyLe IS NULL OR k.tyLe = :tyLe)
            """)
    Page<KhachHangProjection> projection(
            @Param("ten") String ten,
            @Param("soDu") Double soDu,
            @Param("tyLe") BigDecimal tyLe,
            Pageable pageable);


    // dto
    @Query("SELECT new com.example.handlehttp.Dto.KhachHangResponse(kh.soDu, kh.tyLe) " +
            "FROM KhachHang kh WHERE " +
            "(:tenKh IS NULL OR kh.tenkh LIKE %:tenKh%) AND " +
            "(:soDu IS NULL OR kh.soDu = :soDu) AND " +
            "(:tyLe IS NULL OR kh.tyLe = :tyLe)")
    Page<KhachHangResponse> dto(
            @Param("tenKh") String tenKh,
            @Param("soDu") Double soDu,
            @Param("tyLe") BigDecimal tyLe,
            Pageable pageable);
//
//
    // native sql
    @Query(value = """
                SELECT SoDu AS soDu, TyLe AS tyLe
                FROM khachhang
                WHERE (:TenKH IS NULL OR TenKH LIKE %:TenKH%)
                  AND (:SoDu IS NULL OR SoDu = :SoDu)
                  AND (:TyLe IS NULL OR TyLe = :TyLe)
            """,
            countQuery = """
                        SELECT COUNT(*) FROM khachhang
                        WHERE (:TenKH IS NULL OR TenKH LIKE %:TenKH%)
                          AND (:SoDu IS NULL OR SoDu = :SoDu)
                          AND (:TyLe IS NULL OR TyLe = :TyLe)
                    """,
            nativeQuery = true)
    Page<Map<String, Object>> timKiemNhieuDieuKienSQL(
            @Param("TenKH") String TenKH,
            @Param("SoDu") Double SoDu,
            @Param("TyLe") BigDecimal TyLe,
            Pageable pageable);
//
//
//    @Query("SELECT n.tenKH, n.tyLe FROM KhachHang n")
//    List<Object[]> findTenVaLuong();

    List<KhachHang> findByTenkh(String tenKh);


    // count query: lấy tổng số bản ghi cho phân trang
    //đếm số lượng kh có tên 'name'
    @Query(value = "select kh from KhachHang kh where " +
            "kh.tenkh = ?1",
    countQuery = "select count(kh) from KhachHang kh where " +
            "kh.tenkh = ?1"
    )
    Page<KhachHang> findByName(String name, Pageable pageable);


//     Sử dụng truy vấn đếm tùy chỉnh từ NamedQuery đã định nghĩa trong Entity
//     countByName sẽ trả về số lượng khách hàng có tên giống với giá trị name
    @Query(countName = "KhachHang.countByName")  // Dùng countName để tham chiếu đến truy vấn đếm
    long countByName(@Param("name") String name);


    @Query(name = "KhachHang.findByName")
    List<KhachHang> getTen(@Param("tenKh") String tenKh);
}
