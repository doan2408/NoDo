package com.example.nodotest.Repository;

import com.example.nodotest.Entity.CategoryImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryImageRepository extends JpaRepository<CategoryImage, Long> {

    // select all categoryImage theo status =1 và list id category
    @Query(value = "select i from CategoryImage i " +
                   "join fetch i.category c " +
                   "where (:status is null or i.status =:status) " +
                   "and (:categoryId is null or c.id in :categoryId)")
    List<CategoryImage> findAllByStatus(@Param("status") String status,
                                        @Param("categoryId") List<Long> categoryId);

    @Query(value = "select i from CategoryImage i " +
                   "where i.category.id =:categoryId " +
                   "and i.uuid =:uuid " +
                   "and (:status is null or i.status =:status)")
    Optional<CategoryImage> findByCategoryIdAndStatus(@Param("categoryId") Long categoryId,
                                                      @Param("uuid") String uuid,
                                                      @Param("status") String status
                                         );

}
