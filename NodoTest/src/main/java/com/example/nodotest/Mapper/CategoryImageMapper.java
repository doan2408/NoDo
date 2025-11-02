package com.example.nodotest.Mapper;

import com.example.nodotest.Dto.Request.CategoryImageRequest;
import com.example.nodotest.Dto.Response.CategoryImageResponse;
import com.example.nodotest.Entity.CategoryImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryImageMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true) // Category sẽ được set trong service
    @Mapping(target = "status")
        // Trạng thái ảnh là 1
    CategoryImage toCategoryImage(CategoryImageRequest categoryImageRequest);

    CategoryImageResponse toCategoryImageResponse(CategoryImage categoryImage);

    List<CategoryImageResponse> toCategoryImageResponseList(List<CategoryImage> categoryImageList);

    List<CategoryImage> toCategoryImageList(List<CategoryImageRequest> requests);
}
