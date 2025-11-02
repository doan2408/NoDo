package com.example.nodotest.Repository;

import com.example.nodotest.Dto.Response.CategoryResponse;
import com.example.nodotest.Entity.Category;
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
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // check exist category_code
    Boolean existsByCategoryCode(String categoryCode);

    // only get category have status = 1
    @Query(value = "select c from Category c " +
                   "where (:status is null or c.status =:status) " +
                   "and (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) ESCAPE '\\') " +
                   "and (:categoryCode IS NULL OR LOWER(c.categoryCode) LIKE LOWER(CONCAT('%', :categoryCode, '%')) ESCAPE '\\') " +
                   "and (:startDate is null or c.createdDate >= :startDate) " +
                   "and (:startDate is null or c.createdDate <= :endDate) ")
    Page<Category> getAllCategoriesByStatus(@Param("status") String status,
                                            @Param("name") String name,
                                            @Param("categoryCode") String categoryCode,
                                            @Param("startDate") java.sql.Date startDate,
                                            @Param("endDate") java.sql.Date endDate,
                                            Pageable pageable);


    @Query("SELECT c FROM Category c WHERE c.status = :status " +
           "AND (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "AND (:categoryCode IS NULL OR c.categoryCode = :categoryCode) " +
           "AND (:startDate IS NULL OR c.createdDate >= :startDate) " +
           "AND (:endDate IS NULL OR c.createdDate <= :endDate) " +
           "ORDER BY c.createdDate DESC")

    List<Category> findAllForExport(
            @Param("status") String status,
            @Param("name") String name,
            @Param("categoryCode") String categoryCode,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );


    @Query("select c from Category c " +
           "where c.id =:id " +
           "and (:status is null or c.status =:status)")
    Optional<Category> findByIdAndStatus(@Param("id") Long id, @Param("status") String status);

    // get list categoryID with status
    @Query("select c from Category c " +
           "where c.id in :id " +
           "and (:status is null or c.status =:status)")
    List<Category> getListIdWithStatus(@Param("id") List<Long> id, @Param("status") String status);
}
