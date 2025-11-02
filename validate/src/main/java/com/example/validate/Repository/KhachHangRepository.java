package com.example.mappingdto.Repository;

import com.example.mappingdto.Entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang, Integer> {
//    @EntityGraph(attributePaths = "donHangs")
    @Query("select kh from KhachHang kh " +
            "join kh.donHangs dh")
//    Page<KhachHang> findAll(Pageable pageable);


    @Override
    Optional<KhachHang> findById(Integer integer);
}
