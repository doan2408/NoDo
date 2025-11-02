package com.example.nodotest.Repository;

import com.example.nodotest.Entity.ProductImage;
import org.hibernate.annotations.QueryCacheLayout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    @Query(value = "select i from ProductImage i " +
                   "join i.product p " +
                   "where (:status is null or i.status = :status) " +
                   "and (:productIds is null or p.id in :productIds)")
    List<ProductImage> findByProductIdsAndStatus(List<Long> productIds, String status);

    // Tìm tất cả images của product (kể cả status = 0)
    @Query("SELECT pi FROM ProductImage pi WHERE pi.product.id = :productId")
    List<ProductImage> findAllByProductId(@Param("productId") Long productId);

    // Update status của tất cả images của product
    @Modifying
    @Query("UPDATE ProductImage pi SET pi.status = :status, pi.modifiedDate = :modifiedDate " +
           "WHERE pi.product.id = :productId AND pi.status = '1'")
    void updateStatusByProductId(
            @Param("productId") Long productId,
            @Param("status") String status,
            @Param("modifiedDate") Date modifiedDate
    );
}
