package com.example.mappingdto.Service;

import com.example.mappingdto.Dto.DonHangDTO;
import com.example.mappingdto.Entity.DonHang;
import com.example.mappingdto.Entity.KhachHang;
import com.example.mappingdto.Mapper.DonHangMapper;
import com.example.mappingdto.Repository.DonHangRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonHangService {
    private final DonHangRepository donHangRepository;
    private final DonHangMapper donHangMapper;
    private final ModelMapper modelMapper;
    private final Converter<DonHang, DonHangDTO> toDtoConverter;

    public DonHangService(DonHangRepository donHangRepository, DonHangMapper donHangMapper,
                          ModelMapper modelMapper,
                          Converter<DonHang, DonHangDTO> toDtoConverter) {
        this.donHangRepository = donHangRepository;
        this.donHangMapper = donHangMapper;
        this.modelMapper = modelMapper;
        this.toDtoConverter = toDtoConverter;
    }

    // Entity → DTO
    public DonHangDTO toDto(DonHang entity) {
        if (entity == null) return null;
        DonHangDTO dto = new DonHangDTO();
        dto.setId(entity.getId());
        dto.setMaDon(entity.getMaDon());
        dto.setMoTa(entity.getMoTa());
        dto.setTongTien(entity.getTongTien());
        dto.setNgayDat(entity.getNgayDat());
        dto.setGioDat(entity.getGioDat());

        // chỉ lấy ID khách hàng, tránh nạp toàn bộ đối tượng
        if (entity.getMaKH() != null) {
            dto.setMaKHId(entity.getMaKH().getId());
        }

        return dto;
    }

    // 🔹 Chuyển từng entity sang DTO bằng Builder Pattern
    private DonHangDTO toDtoBuilder(DonHang entity) {
        if (entity == null) return null;

        return new DonHangDTO.Builder()
                .id(entity.getId())
                .maDon(entity.getMaDon())
                .moTa(entity.getMoTa())
                .tongTien(entity.getTongTien())
                .ngayDat(entity.getNgayDat())
                .gioDat(entity.getGioDat())
                // chỉ set id của Khách hàng, tránh nạp cả object KhachHang
                .maKHId(entity.getMaKH() != null ? entity.getMaKH().getId() : null)
                .build();
    }

    // DTO → Entity
    public static DonHang toEntity(DonHangDTO dto) {
        if (dto == null) return null;

        DonHang entity = new DonHang();
        entity.setId(dto.getId());
        entity.setMaDon(dto.getMaDon());
        entity.setMoTa(dto.getMoTa());
        entity.setTongTien(dto.getTongTien());
        entity.setNgayDat(dto.getNgayDat());
        entity.setGioDat(dto.getGioDat());

        // chỉ gắn đối tượng KhachHang giả (chứa id)
        if (dto.getMaKHId() != null) {
            KhachHang kh = new KhachHang();
            kh.setId(dto.getMaKHId());
            entity.setMaKH(kh);
        }

        return entity;
    }

    // mapStruct
    public List<DonHangDTO> mapStruct() {
        List<DonHang> entities = donHangRepository.findAll();
        return donHangMapper.toDto(entities);
    }

    // manual
    public List<DonHangDTO> manual() {
        List<DonHang> entities = donHangRepository.findAll();
        return entities.stream().map(this::toDto).toList();
    }

    // modelMapper
    public List<DonHangDTO> modelMapper() {
        List<DonHang> entities = donHangRepository.findAll();
        return entities.stream().map(e -> modelMapper.map(e, DonHangDTO.class)).toList();
    }

    // beanUtils
    public List<DonHangDTO> beanUtils() {
        List<DonHang> entities = donHangRepository.findAll();
        return entities.stream()
                .map(e -> {
                    DonHangDTO dto = new DonHangDTO();
                    BeanUtils.copyProperties(e, dto);
                    if (e.getMaKH() != null) {
                        dto.setMaKHId(e.getMaKH().getId());
                    }
                    return dto;
                }).toList();
    }

    // converter
    public List<DonHangDTO> converter() {
        return donHangRepository.findAll().stream()
                .map(toDtoConverter::convert)
                .toList();
    }

    // builder pattern
    public List<DonHangDTO> builderPattern() {
        return donHangRepository.findAll()
                .stream()
                .map(this::toDtoBuilder)
                .toList();
    }

    // search
    public List<DonHangDTO> search(String maDon) {
        return donHangRepository.findByMaDon(maDon).stream()
                .map(this::toDto).toList();
    }

    // add
    public DonHangDTO add(DonHangDTO donHangDTO) {
        DonHang donHangSaved = donHangRepository.save(donHangMapper.toEntity(donHangDTO));
        return donHangMapper.toDto(donHangSaved);
    }

    // update
    public DonHangDTO update(DonHangDTO donHangDTO) {
        DonHang donHangUpdated = donHangRepository.save(donHangMapper.toEntity(donHangDTO));
        return donHangMapper.toDto(donHangUpdated);
    }

}
