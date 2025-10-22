package com.example.mappingdto.Service;

import com.example.mappingdto.Dto.KhachHangDTO;
import com.example.mappingdto.Entity.KhachHang;
import com.example.mappingdto.Mapper.KhachHangMapper;
import com.example.mappingdto.Repository.KhachHangRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KhachHangService {
    private final KhachHangRepository khachHangRepository;
    private final KhachHangMapper khachHangMapper;
    private final ModelMapper modelMapper;

    public KhachHangService(KhachHangRepository khachHangRepository, KhachHangMapper khachHangMapper, ModelMapper modelMapper) {
        this.khachHangRepository = khachHangRepository;
        this.khachHangMapper = khachHangMapper;
        this.modelMapper = modelMapper;
    }

    public KhachHangDTO toDto(KhachHang khachHang) {
        KhachHangDTO khachHangDTO = new KhachHangDTO();
        khachHangDTO.setId(khachHang.getId());
        khachHangDTO.setMakhLon(khachHang.getMakhLon());
        khachHangDTO.setMakhNho(khachHang.getMakhNho());
        khachHangDTO.setMakhNhohon(khachHang.getMakhNhohon());
        khachHangDTO.setMaCode(khachHang.getMaCode());
        khachHangDTO.setTenKH(khachHang.getTenKH());
        khachHangDTO.setGhiChu(khachHang.getGhiChu());
        khachHangDTO.setTenUnicode(khachHang.getTenUnicode());
        khachHangDTO.setSoDu(khachHang.getSoDu());
        khachHangDTO.setDiem(khachHang.getDiem());
        khachHangDTO.setTyLe(khachHang.getTyLe());
        khachHangDTO.setGiaTri(khachHang.getGiaTri());
        khachHangDTO.setNgaySinh(khachHang.getNgaySinh());
        khachHangDTO.setGioTao(khachHang.getGioTao());
        khachHangDTO.setNgayTao(khachHang.getNgayTao());
        khachHangDTO.setMakhGioithieu(khachHang.getMakhGioithieu());
        return khachHangDTO;
    }

    public KhachHang toDto(KhachHangDTO khachHangDTO) {
        KhachHang khachHang = new KhachHang();
        khachHang.setMakhLon(khachHangDTO.getMakhLon());
        khachHang.setMakhNho(khachHangDTO.getMakhNho());
        khachHang.setMakhNhohon(khachHangDTO.getMakhNhohon());
        khachHang.setMaCode(khachHangDTO.getMaCode());
        khachHang.setTenKH(khachHangDTO.getTenKH());
        khachHang.setGhiChu(khachHangDTO.getGhiChu());
        khachHang.setTenUnicode(khachHangDTO.getTenUnicode());
        khachHang.setSoDu(khachHangDTO.getSoDu());
        khachHang.setDiem(khachHangDTO.getDiem());
        khachHang.setTyLe(khachHangDTO.getTyLe());
        khachHang.setGiaTri(khachHangDTO.getGiaTri());
        khachHang.setNgaySinh(khachHangDTO.getNgaySinh());
        khachHang.setGioTao(khachHangDTO.getGioTao());
        khachHang.setNgayTao(khachHangDTO.getNgayTao());
        khachHang.setMakhGioithieu(khachHangDTO.getMakhGioithieu());
        return khachHang;
    }

    // mapStruct
    public List<KhachHangDTO> mapStruct() {
        List<KhachHang> entities = khachHangRepository.findAll();
        return khachHangMapper.toDoList(entities);
    }

    // manual
    public List<KhachHangDTO> manual() {
        List<KhachHang> entities = khachHangRepository.findAll();
        return entities.stream().map(this::toDto).toList();
    }

    // modelMapper
    public List<KhachHangDTO> modelMapper() {
        List<KhachHang> entities = khachHangRepository.findAll();
        return entities.stream().map(e -> modelMapper.map(e, KhachHangDTO.class)).toList();
    }

    // beanUtils
    public List<KhachHangDTO> beanUtils() {
        List<KhachHang> entities = khachHangRepository.findAll();
        return entities.stream().map(e -> {
            KhachHangDTO dto = new KhachHangDTO();
            BeanUtils.copyProperties(e, dto);
            return dto;
        }).toList();
    }


    public KhachHangDTO addKhachHang(KhachHangDTO khachHangDTO) {
        if (khachHangDTO ==null) return null;
        KhachHang khachHangSaved = khachHangRepository.save(khachHangMapper.toEntity(khachHangDTO));
        return khachHangDTO;
    }

}
