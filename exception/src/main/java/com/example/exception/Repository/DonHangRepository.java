package com.example.validate.Repository;


import com.example.validate.Dto.Response.DonHangResponse;
import com.example.validate.Entity.DonHang;
import com.example.validate.Entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface DonHangRepository extends JpaRepository<DonHang, Integer> {
    @Query(value = "select dh.maDon, dh.moTa, KH.TenKH from DonHang dh\n" +
            "join KhachHang KH on dh.MaKH = KH.MaKH\n" +
            "where dh.MaDon = :maDon", nativeQuery = true)
    List<DonHangResponse> findByMaDon(@Param("maDon") String maDon);

    @Query("""
            select (
                dh.maDon,
                dh.moTa,
                ''
            ) from DonHang dh
            where (:maDon is null or dh.maDon = :maDon)
            """)
    List<Object[]> findAllDto(@Param("maDon") String maDon);


    List<DonHangResponse> findByMaKHIn(Set<KhachHang> khachHangs);
}
