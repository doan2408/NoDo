package com.example.handlehttp.Service;

import com.example.handlehttp.Dto.KhachHangResponse;
import com.example.handlehttp.Model.KhachHang;
import com.example.handlehttp.Projection.KhachHangProjection;
import com.example.handlehttp.Repository.KhachHangRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class KhachHangService {
    private final KhachHangRepository khachHangRepository;
    //    private final KhachHangMapper khachHangMapper;
    private final EntityManager em;

    public KhachHangService(KhachHangRepository khachHangRepository, EntityManager em) {
        this.khachHangRepository = khachHangRepository;
        this.em = em;
    }


    public List<KhachHang> getClient() {
        return khachHangRepository.findAll();
    }

    public KhachHang update(KhachHang khachHang) {
        return khachHangRepository.save(khachHang);
    }

    @Transactional
    public KhachHang create(KhachHang khachHang) {
        khachHang.setId(1000);
        return khachHangRepository.save(khachHang);
    }

//    public Page<Object[]> searchKhachHang(String name, Double soDu, BigDecimal tyLe, Pageable pageable) {
//        return khachHangRepository.searchKhachHang(name, soDu, tyLe, pageable);
//    }
//
//    public Page<KhachHangResponse> searchDto(String name, Double soDu, BigDecimal tyLe, Pageable pageable) {
//        Page<KhachHangResponse> pageResult = khachHangRepository.dto(name, soDu, tyLe, pageable);
//        return pageResult;
//    }
//
//    public Page<KhachHangProjection> timKiemProjection(String name, Double soDu, BigDecimal tyLe, Pageable pageable) {
//        Page<KhachHangProjection> pageResult = khachHangRepository.projection(name, soDu, tyLe, pageable);
//        return pageResult;
//    }
//
//    public Page<Map<String, Object>> timKiemNhieuDieuKienSQL(String name, Double soDu, BigDecimal tyLe, Pageable pageable) {
//        Page<Map<String, Object>> pageResult = khachHangRepository.timKiemNhieuDieuKienSQL(name, soDu, tyLe, pageable);
//        return pageResult;
//    }
//
//
//    // criteria
//    public Page<KhachHangResponse> criteria(Double minSoDu, Double maxSoDu, BigDecimal tyLe, Pageable pageable) {
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
//        Root<KhachHang> root = cq.from(KhachHang.class);
//
//        List<Predicate> predicates = new ArrayList<>();
//        if (minSoDu != null) predicates.add(cb.ge(root.get("soDu"), minSoDu));
//        if (maxSoDu != null) predicates.add(cb.le(root.get("soDu"), maxSoDu));
//        if (tyLe != null) predicates.add(cb.equal(root.get("tyLe"), tyLe));
//
//        cq.multiselect(root.get("soDu").alias("soDu"), root.get("tyLe").alias("tyLe"))
//                .where(predicates.toArray(new Predicate[0]));
//
//        TypedQuery<Tuple> query = em.createQuery(cq);
//        query.setFirstResult((int) pageable.getOffset());
//        query.setMaxResults(pageable.getPageSize());
//
//        List<KhachHangResponse> list = query.getResultList().stream()
//                .map(t -> new KhachHangResponse(
//                        t.get("soDu", Double.class),
//                        t.get("tyLe", BigDecimal.class)
//                ))
//                .toList();
//
//        return new PageImpl<>(list, pageable, list.size());
//    }
//
//    public List<Object[]> findTenVaLuong() {
//        return khachHangRepository.findTenVaLuong();
//    }

    public List<KhachHang> findByName(String name) {
        return khachHangRepository.findByTenkh(name);
    }


}
