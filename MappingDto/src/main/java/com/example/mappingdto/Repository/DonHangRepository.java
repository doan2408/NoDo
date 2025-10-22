package com.example.mappingdto.Repository;

import com.example.mappingdto.Entity.DonHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonHangRepository extends JpaRepository<DonHang, Integer> {
        @Query("select dh from DonHang dh " +
                "join dh.maKH kh " +
                "where dh.maDon = :maDon")
    List<DonHang> findByMaDon(@Param("maDon") String maDon);
}
