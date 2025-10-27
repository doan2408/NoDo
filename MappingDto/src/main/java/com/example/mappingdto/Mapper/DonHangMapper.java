package com.example.mappingdto.Mapper;

import com.example.mappingdto.Dto.DonHangDTO;
import com.example.mappingdto.Entity.DonHang;
import org.mapstruct.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

// , uses = KhachHangMapper.class
@Mapper(componentModel = "spring")
public interface DonHangMapper {

    @Mapping(source = "maKH.id", target = "maKHId")
    DonHangDTO toDto(DonHang entity);


    @Mapping(target = "maKH.id", source = "maKHId")
    DonHang toEntity(DonHangDTO dto);


    List<DonHangDTO> toDto(List<DonHang> entities);

    List<DonHang> toEntity(List<DonHangDTO> dtos);

    //    @IterableMapping
    Set<DonHangDTO> toDtoSet(Set<DonHang> entities);  // Ánh xạ từ Set<DonHang> sang Set<DonHangDTO>

    Set<DonHang> toEntitySet(Set<DonHangDTO> entities);

    // Method mới với expression - truyền thêm toàn bộ list để tạo danhSachMaDon
    @Mapping(source = "maKH.id", target = "maKHId")
    @Mapping(target = "maDon", expression = "java(allEntities.stream().map(DonHang::getMaDon).collect(java.util.stream.Collectors.joining(\", \")))")
    DonHangDTO toDtoWithList(DonHang entity, @Context List<DonHang> allEntities);


    default String listToSTring(List<DonHang> donHangs) {
        if (donHangs == null || donHangs.isEmpty()) return null;

        return donHangs.stream().map(DonHang::getMaDon).collect(Collectors.joining(", "));
    }
}
