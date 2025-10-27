package com.example.mappingdto.Mapper;

import com.example.mappingdto.Dto.KhachHangDTO;
import com.example.mappingdto.Entity.KhachHang;
import com.example.mappingdto.Enum.Status;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = DonHangMapper.class)
public interface KhachHangMapper {

    @Mapping(target = "donHangs")
    KhachHangDTO toDto(KhachHang entity);

    @Mapping(source = "donHangs", target = "donHangs")
    KhachHang toEntity(KhachHangDTO dto);

    List<KhachHangDTO> toDoList(List<KhachHang> entities);

    List<KhachHang> toDoEntityList(List<KhachHangDTO> entities);

//    @Mapping(target = "donHangs", ignore = true)
//    KhachHangDTO toDtoa(KhachHang entity);
//    List<KhachHangDTO> toDoLista(List<KhachHang> entities);

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

