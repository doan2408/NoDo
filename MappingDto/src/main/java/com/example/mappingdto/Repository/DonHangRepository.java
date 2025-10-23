package com.example.mappingdto.Repository;

import com.example.mappingdto.Dto.DonHangDTO;
import com.example.mappingdto.Entity.DonHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonHangRepository extends JpaRepository<DonHang, Integer> {
    @Query(value = "select dh.maDon, dh.moTa, KH.TenKH from DonHang dh\n" +
            "join KhachHang KH on dh.MaKH = KH.MaKH\n" +
            "where dh.MaDon = :maDon", nativeQuery = true)
    List<DonHangDTO> findByMaDon(@Param("maDon") String maDon);

    @Query("""
            select (
                dh.maDon,
                dh.moTa,
                ''
            ) from DonHang dh
            where (:maDon is null or dh.maDon = :maDon)
            """)
    List<Object[]> findAllDto(@Param("maDon") String maDon);


}
