package com.example.nodotest.Mapper;

import com.example.nodotest.Dto.Request.ProductImageRequest;
import com.example.nodotest.Dto.Response.ProductImageResponse;
import com.example.nodotest.Entity.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.sql.Date;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {

     @Mapping(target = "id", ignore = true)
     @Mapping(target = "product", ignore = true)
     @Mapping(target = "status")
    ProductImage toEntity(ProductImageRequest productImageRequest);

    ProductImageResponse toResponse(ProductImage productImage);

    List<ProductImage> toEntities(List<ProductImageRequest> productImageRequest);

    List<ProductImageResponse> toResponses(List<ProductImage> productImage);

    void updateProductImageFromRequest(ProductImageRequest productImageRequest, @MappingTarget ProductImage productImage);

    default Date toSqlDate(long timeInMillis) {
        return new Date(timeInMillis);
    }
}
