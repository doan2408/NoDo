package com.example.mappingdto.Service;

import com.example.mappingdto.Dto.DonHangDTO;
import com.example.mappingdto.Entity.DonHang;
import com.example.mappingdto.Entity.KhachHang;
import com.example.mappingdto.Mapper.DonHangMapper;
import com.example.mappingdto.Repository.DonHangRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DonHangService {
    private final DonHangRepository donHangRepository;
    private final DonHangMapper donHangMapper;
    private final ModelMapper modelMapper;
    private final Converter<DonHang, DonHangDTO> toDtoConverter;

    @PersistenceContext
    private EntityManager entityManager;

    public DonHangService(DonHangRepository donHangRepository,
                          DonHangMapper donHangMapper,
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

    //🔹 Chuyển từng entity sang DTO bằng Builder Pattern
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
//    public List<DonHangDTO> mapStruct() {
//        List<DonHang> entities = donHangRepository.findAll();
//        return donHangMapper.toDto(entities);
//    }

    public List<DonHangDTO> mapStruct() {
        List<DonHang> entities = donHangRepository.findAll();
        return entities.stream()
                .map(entity -> donHangMapper.toDtoWithList(entity, entities))
                .toList();
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

    //     builder pattern
    public List<DonHangDTO> builderPattern() {
        return donHangRepository.findAll()
                .stream()
                .map(this::toDtoBuilder)
                .toList();
    }

    // search
    public List<DonHangDTO> search(String maDon) {
        List<Object[]> list = donHangRepository.findAllDto(maDon);
        List<DonHangDTO> dtos = new ArrayList<>();
        for (Object[] row : list) {
            DonHangDTO dto = new DonHangDTO();
            dtos.add(dto);
            dto.setMaDon((String) row[0]);
            dto.setMoTa((String) row[1]);
            dto.setTenKh((String) row[2]);

        }
        return dtos;
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


    public Page<DonHangDTO> searchDto(String maDon, Pageable pageable) {
        StringBuilder sql = new StringBuilder("select dh.maDon, dh.moTa, KH.TenKH from DonHang dh " +
                "           join KhachHang KH on dh.MaKH = KH.MaKH " +
                "         where 1 = 1 ");

        // Query để đếm tổng số bản ghi
        StringBuilder countSql = new StringBuilder(
                "SELECT COUNT(*) " +
                        "FROM DonHang dh " +
                        "JOIN KhachHang KH ON dh.MaKH = KH.MaKH " +
                        "WHERE 1 = 1 "
        );

        if (maDon != null && !maDon.isEmpty()) {
            sql.append(" AND dh.MaDon = :maDon");
            countSql.append(" AND dh.MaDon = :maDon");
        }

        sql.append(" ORDER BY dh.MaDon OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY");
        // thực thi query lấy data
        Query query = entityManager.createNativeQuery(sql.toString());
        if (maDon != null && !maDon.isEmpty()) {
            query.setParameter("maDon", maDon);
        }
        query.setParameter("limit", pageable.getPageSize());
        query.setParameter("offset", pageable.getPageNumber() * pageable.getPageSize());

        List<Object[]> results = query.getResultList();

        List<DonHangDTO> dtos = results.stream()
                .map(row -> new DonHangDTO(
                        (String) row[0],
                        (String) row[1],
                        (String) row[2]
                )).toList();

        // Thực thi query đếm tổng số
        Query countQuery = entityManager.createNativeQuery(countSql.toString());
        if (maDon != null && !maDon.isEmpty()) {
            countQuery.setParameter("maDon", maDon);
        }
        Long total = ((Number) countQuery.getSingleResult()).longValue();
        System.out.println("total: " + total);

        return new PageImpl<>(dtos, pageable, total);
    }


    public Page<DonHangDTO> searchDtoJpql(String maDon, String ghiChu, Pageable pageable) {
        StringBuilder sql = new StringBuilder("""
                    select new com.example.mappingdto.Dto.DonHangDTO(
                    dh.maDon,
                    dh.moTa,
                    kh.tenKH
                ) from DonHang dh
                join dh.maKH kh
                where 1 = 1
                    """);

        // Query để đếm tổng số bản ghi
//        StringBuilder countSql = new StringBuilder(
//                "SELECT COUNT(*) " +
//                        "FROM DonHang dh " +
//                        "JOIN dh.maKH kh " +
//                        "WHERE 1 = 1 "
//        );

        if (maDon != null && !maDon.isEmpty()) {
            sql.append(" AND dh.maDon = :maDon");
        }

        if (ghiChu != null && !ghiChu.isEmpty()) {
            sql.append(" AND dh.moTa = :moTa");
        }

//        sql.append(" ORDER BY dh.maDon OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY");
        // thực thi query lấy data
        TypedQuery<DonHangDTO> query = entityManager.createQuery(sql.toString(), DonHangDTO.class);

        if (maDon != null && !maDon.isEmpty()) {
            query.setParameter("maDon", maDon);
        }
        if (ghiChu != null && !ghiChu.isEmpty()) {
            query.setParameter("moTa", ghiChu);
        }

        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        List<DonHangDTO> dtos = query.getResultList();


        // Query đếm tổng số bản ghi
        StringBuilder countJpql = new StringBuilder("""
                    select count(dh)
                    from DonHang dh
                    join dh.maKH kh
                    where 1 = 1
                """);
        if (maDon != null && !maDon.isEmpty()) {
            countJpql.append(" and dh.maDon = :maDon");
        }
        if (ghiChu != null && !ghiChu.isEmpty()) {
            countJpql.append(" and dh.moTa = :moTa");
        }

        TypedQuery<Long> countQuery = entityManager.createQuery(countJpql.toString(), Long.class);
        if (maDon != null && !maDon.isEmpty()) {
            countQuery.setParameter("maDon", maDon);
        }
        if (ghiChu != null && !ghiChu.isEmpty()) {
            countQuery.setParameter("moTa", ghiChu);
        }

        Long total = ((Number) countQuery.getSingleResult()).longValue();
        System.out.println("total: " + total);

        return new PageImpl<>(dtos, pageable, total);
    }




}
