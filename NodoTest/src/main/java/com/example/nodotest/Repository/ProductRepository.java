package com.example.nodotest.Repository;

import com.example.nodotest.Entity.Product;
import com.example.nodotest.Entity.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // check exist product_code
    Boolean existsByProductCode(String productCode);

    @Query("select distinct p from Product p " +
           "left join p.productCategories pc " +
           "left join pc.category c " +
           "where p.status = :status " +
           "and (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) ESCAPE '\\') " +
           "and (:code IS NULL OR LOWER(p.productCode) LIKE LOWER(CONCAT('%', :code, '%')) ESCAPE '\\') " +
           "and (:startDate IS NULL OR p.createdDate >= :startDate) " +
           "and (:endDate IS NULL OR p.createdDate <= :endDate) " +
           "and (:cateId IS NULL OR c.id = :cateId)")
    Page<Product> getAllProductsByStatus(@Param("status") String status,
                                         @Param("name") String name,
                                         @Param("code") String code,
                                         @Param("startDate") Date startDate,
                                         @Param("endDate") Date endDate,
                                         @Param("cateId") Long cateId,
                                         Pageable pageable);

    @Query("select pc From ProductCategory pc " +
           "join fetch pc.category c " +
           "join fetch pc.product p " +
           "where pc.product.id in :productIds " +
           "and p.status = :statusPro " +
           "and c.status =:statusCate")
    List<ProductCategory> findByProductIdsAndStatus(@Param("productIds") List<Long> productIds,
                                            @Param("statusPro") String statusPro,
                                            @Param("statusCate") String statusCate
                                            );

    @Query("select p from Product p " +
           "where p.id =:id " +
           "and (:status is null or p.status =:status)")
    Optional<Product> findByIdAndStatus(@Param("id") Long id, @Param("status") String status);


    // Query export excel
    @Query("select DISTINCT p from Product p " +
           "left join p.productCategories pc " +
           "left join pc.category c " +
           "where p.status = :status " +
           "and (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "and (:code is null or lower(p.productCode) like lower(concat('%', :code, '%'))) " +
           "and (:startDate is null or p.createdDate >= :startDate) " +
           "and (:endDate is null or p.createdDate <= :endDate) " +
           "and (:cateId is null or c.id = :cateId) " +
           "order by p.createdDate desc")
    List<Product> findAllProductsForExport(
            @Param("status") String status,
            @Param("name") String name,
            @Param("code") String code,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("cateId") Long cateId
    );

    // tìm tất cả ProductCategory của 1 product (kể cả status = 0)
    @Query("SELECT pc FROM ProductCategory pc " +
           "JOIN FETCH pc.category c " +
           "WHERE pc.product.id = :productId")
    List<ProductCategory> findAllByProductId(@Param("productId") Long productId);

    // tìm ProductCategory theo productId và categoryId gòm soft delete
    @Query("SELECT pc FROM ProductCategory pc " +
           "WHERE pc.product.id = :productId " +
           "AND pc.category.id = :categoryId")
    Optional<ProductCategory> findByProductIdAndCategoryId(
            @Param("productId") Long productId,
            @Param("categoryId") Long categoryId
    );

}
