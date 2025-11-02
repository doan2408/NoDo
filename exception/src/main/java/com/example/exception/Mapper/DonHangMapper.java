package com.example.validate.Mapper;

import com.example.validate.Dto.Response.DonHangResponse;
import com.example.validate.Entity.DonHang;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// , uses = KhachHangMapper.class
@Mapper(componentModel = "spring")
public interface DonHangMapper {

    @Mapping(source = "maKH.id", target = "maKHId")
    DonHangResponse toDto(DonHang entity);


    @Mapping(target = "maKH.id", source = "maKHId")
    DonHang toEntity(DonHangResponse dto);


    List<DonHangResponse> toDto(List<DonHang> entities);

    List<DonHang> toEntity(List<DonHangResponse> dtos);

    //    @IterableMapping
    Set<DonHangResponse> toDtoSet(Set<DonHang> entities);  // Ánh xạ từ Set<DonHang> sang Set<DonHangResponse>

    Set<DonHang> toEntitySet(Set<DonHangResponse> entities);

    // Method mới với expression - truyền thêm toàn bộ list để tạo danhSachMaDon
    @Mapping(source = "maKH.id", target = "maKHId")
    @Mapping(target = "maDon", expression = "java(allEntities.stream().map(DonHang::getMaDon).collect(java.util.stream.Collectors.joining(\", \")))")
    DonHangResponse toDtoWithList(DonHang entity, @Context List<DonHang> allEntities);


    default String listToSTring(List<DonHang> donHangs) {
        if (donHangs == null || donHangs.isEmpty()) return null;

        return donHangs.stream().map(DonHang::getMaDon).collect(Collectors.joining(", "));
    }
}
