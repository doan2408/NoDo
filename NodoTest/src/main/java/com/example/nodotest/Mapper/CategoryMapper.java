package com.example.nodotest.Mapper;


import com.example.nodotest.Dto.Request.CategoryRequest;
import com.example.nodotest.Dto.Response.CategoryResponse;
import com.example.nodotest.Entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    // Ánh xạ từ CategoryRequest sang Category
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "1")
    @Mapping(target = "createdDate", expression = "java(toSqlDate(System.currentTimeMillis()))")
    @Mapping(target = "modifiedDate", expression = "java(toSqlDate(System.currentTimeMillis()))")
    @Mapping(target = "createdBy", constant = "admin")
    @Mapping(target = "modifiedBy", constant = "admin")
    @Mapping(target = "categoryCode", source = "categoryCode")
    @Mapping(target = "productCategories", ignore = true)
    @Mapping(source = "images", target = "images", ignore = true)
    Category toCategory(CategoryRequest request);

    CategoryResponse toCategoryResponse(Category category);

    List<CategoryResponse> toCategoryResponseList(List<Category> categories);

    List<Category> toCategoryList(List<CategoryRequest> categories);

    @Mapping(target = "id", ignore = true)
    void updateCategoryFromRequest(CategoryRequest req, @MappingTarget Category category);


    default java.sql.Date toSqlDate(long timeInMillis) {
        return new java.sql.Date(timeInMillis);
    }
}
