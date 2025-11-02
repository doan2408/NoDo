package com.example.validate.Mapper;


import com.example.validate.Dto.Request.KhachHangRequest;
import com.example.validate.Dto.Response.KhachHangResponse;
import com.example.validate.Entity.KhachHang;
import com.example.validate.Enum.Status;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = DonHangMapper.class)
public interface KhachHangMapper {

    @Mapping(source = "donHangs", target = "donHangs")
    KhachHangResponse toDto(KhachHang entity);

    @Mapping(source = "donHangs", target = "donHangs")
    KhachHang toEntity(KhachHangResponse dto);

    List<KhachHangResponse> toDoList(List<KhachHang> entities);

    List<KhachHang> toDoEntityList(List<KhachHangResponse> entities);


    @Mapping(source = "donHangs", target = "donHangs")
    KhachHangRequest toRequest(KhachHang entity);

    @Mapping(source = "donHangs", target = "donHangs")
    KhachHang toEntity(KhachHangRequest dto);

    List<KhachHangRequest> toDoRequestList(List<KhachHang> entities);

    List<KhachHang> toDoEntityListFromRequest(List<KhachHangRequest> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(KhachHangRequest request, @MappingTarget KhachHang entity);

//    @Mapping(target = "donHangs", ignore = true)
//    KhachHangResponse toDtoa(KhachHang entity);
//    List<KhachHangResponse> toDoLista(List<KhachHang> entities);

    // custom auto transfer IdKhachHang -> KhachHang Object
//    default KhachHang toKhachHang(Integer id) {
//        if(id == null) return null;
//        KhachHang khachHang = new KhachHang();
//        khachHang.setId(id);
//        return khachHang;
//    }

    // custom transfer from String to Enum Status
    default Status mapGhiChu(String ghiChu) {
        if (ghiChu == null) return null;
        switch (ghiChu) {
            case "ACTIVE":
                return Status.ACTIVE;
            case "INACTIVE":
                return Status.INACTIVE;
            case "PENDING":
                return Status.PENDING;
            default:
                throw new IllegalArgumentException("Unknown Status: " + ghiChu);
        }
    }

    // map status -> ghiChu
    default String mapStatus(Status status) {
        if (status == null) return null;
        return status.name();
    }
}

