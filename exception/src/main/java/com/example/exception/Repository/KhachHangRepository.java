package com.example.validate.Repository;

import com.example.validate.Dto.Response.KhachHangResponse;
import com.example.validate.Entity.KhachHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang, Integer> {
//    @EntityGraph(attributePaths = "donHangs")
    @Query("select kh from KhachHang kh " +
            "join kh.donHangs dh")
    Page<KhachHang> findAll(Pageable pageable);


    Optional<KhachHang> findById(Integer integer);

    @Query("select kh from KhachHang kh " +
            "where (:maCode is null or kh.maCode like %:maCode% escape '\\')")
    List<KhachHang> findByMaCode(String maCode);


}
