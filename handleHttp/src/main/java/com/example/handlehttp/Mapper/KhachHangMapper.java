package com.example.handlehttp.Mapper;

import com.example.handlehttp.Dto.KhachHangResponse;
import com.example.handlehttp.Model.KhachHang;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Lazy;

//@Mapper(componentModel = "spring") // để spring có thể inject vào service
//public interface KhachHangMapper {
//    KhachHangMapper instance = Mappers.getMapper(KhachHangMapper.class);
//
//    KhachHangResponse toDto(KhachHang khachHang);
//
//    KhachHang toEntity(KhachHangResponse khachHangResponse);
//}
