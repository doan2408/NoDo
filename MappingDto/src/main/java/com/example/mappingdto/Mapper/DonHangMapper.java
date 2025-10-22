package com.example.mappingdto.Mapper;

import com.example.mappingdto.Dto.DonHangDTO;
import com.example.mappingdto.Entity.DonHang;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

// , uses = KhachHangMapper.class
@Mapper(componentModel = "spring")
public interface DonHangMapper {

    @Mapping(source = "maKH.id", target = "maKHId")
    DonHangDTO toDto(DonHang entity);

    @Mapping(target = "maKH.id", source = "maKHId")
    DonHang toEntity(DonHangDTO dto);

    List<DonHangDTO> toDto(List<DonHang> entities);
    List<DonHang> toEntity(List<DonHangDTO> dtos);

    Set<DonHangDTO> toDtoSet(Set<DonHang> entities);  // Ánh xạ từ Set<DonHang> sang Set<DonHangDTO>
    Set<DonHang> toEntitySet(Set<DonHangDTO> entities);
}
