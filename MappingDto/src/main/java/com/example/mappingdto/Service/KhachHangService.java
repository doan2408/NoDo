package com.example.mappingdto.Service;

import com.example.mappingdto.Dto.KhachHangDTO;
import com.example.mappingdto.Entity.DonHang;
import com.example.mappingdto.Entity.KhachHang;
import com.example.mappingdto.Mapper.KhachHangMapper;
import com.example.mappingdto.Repository.KhachHangRepository;
import jakarta.persistence.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KhachHangService {
    private final KhachHangRepository khachHangRepository;
    private final KhachHangMapper khachHangMapper;
    private final ModelMapper modelMapper;

    @PersistenceContext
    private EntityManager entityManager;

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
        if (khachHangDTO == null) return null;
        KhachHang khachHangSaved = khachHangRepository.save(khachHangMapper.toEntity(khachHangDTO));
        return khachHangDTO;
    }

    public List<KhachHangDTO> getKhachHangList() {
//        EntityGraph<KhachHang> graph = entityManager.createEntityGraph(KhachHang.class);
//        graph.addAttributeNodes("donHangs");
//        Map<String, Object> hints = new HashMap<>();
//        hints.put("javax.persistence.loadgraph", graph);
//        KhachHang kh = entityManager.find(KhachHang.class, hints);

        EntityGraph<?> entityGraph = entityManager.getEntityGraph("KhachHang.donHangs");

        List<KhachHang> dto = entityManager.createQuery("""
                        select kh from KhachHang kh 
                        """, KhachHang.class)
//                .setHint("javax.persistence.loadgraph", entityGraph)
                .getResultList();

//        List<KhachHang> khachHang = khachHangRepository.findAll();
        for (KhachHang kh : dto) {
            System.out.println("don hang: " + kh.getDonHangs());
        }
        return dto.stream().map(this::toDto).toList();
    }

//    public void persistKhachHangWithDonHang() {
//        EntityTransaction tx = entityManager.getTransaction();
//        tx.begin();
//
//        KhachHang kh = new KhachHang();
//        kh.setTenKH("Nguyen Van A");
//
//        DonHang dh1 = new DonHang();
//        dh1.setMaDon("DH001");
//        dh1.setMaKH(kh);
//
//        DonHang dh2 = new DonHang();
//        dh2.setMaDon("DH002");
//        dh2.setMaKH(kh);
//
//        kh.getDonHangs().add(dh1);
//        kh.getDonHangs().add(dh2);
//
//        entityManager.persist(kh); // cascade will auto persist dh1, dh2
//        tx.commit();
//    }
//
//
//    public void removeKhachHang() {
//        EntityTransaction tx = entityManager.getTransaction();
//        tx.begin();
//
//        KhachHang kh = entityManager.find(KhachHang.class, 1);  // giả sử id = 1
//        entityManager.remove(kh);  // CascadeType.REMOVE sẽ xóa DonHang liên quan
//
//        tx.commit();
//    }
//
//
//    public void mergeKhachHang() {
//        EntityTransaction tx = entityManager.getTransaction(); // Lấy đối tượng giao dịch (transaction) từ EntityManager.
//        tx.begin(); // start new transaction
//
//        // Tạo một object mới detachedKh trên bộ nhớ Java.
//        KhachHang detachedKh = new KhachHang();
//        detachedKh.setId(1); // id 1 trong database
//        detachedKh.setTenKH("Update name");
//
//        DonHang newDonHang = new DonHang();
//        newDonHang.setMaDon("DH003");
//        newDonHang.setMaKH(detachedKh);
//
//
//        // Thêm DonHang mới vào tập donHangs của khách hàng.
//
//        //Bây giờ ta có mối quan hệ hai chiều nhất quán:
//        // newDonHang.maKH = detachedKh
//        // detachedKh.donHangs chứa newDonHang
//        detachedKh.getDonHangs().add(newDonHang);
//
//        // Tìm entity có id=1 trong persistence context hoặc trong DB.
//        // Nếu có, nó copy giá trị từ detachedKh vào entity managed.
//        // Trả về bản managed entity mới (chứ không phải chính detachedKh).
//        entityManager.merge(detachedKh); // CascadeType.MERGE sẽ update KhachHang & DonHang
//        tx.commit();
//    }
//
//    public void refreshKhachHang() {
//        EntityTransaction tx = entityManager.getTransaction();
//        tx.begin();
//
//        KhachHang kh = entityManager.find(KhachHang.class, 1);
//        entityManager.refresh(kh); // CascadeType.REFRESH cũng áp dụng cho donHangs
//
//        tx.commit();
//    }
//
//    public void deleteKhachHang() {
//        EntityTransaction tx = entityManager.getTransaction();
//        tx.begin();
//
//        KhachHang kh = entityManager.find(KhachHang.class, 1);
//        entityManager.detach(kh); // CascadeType.DETACH áp dụng cho donHangs
//
//        tx.commit();
//    }


    @Transactional
    public void persistKhachHangWithDonHang() {
        KhachHang kh = new KhachHang();
        kh.setTenKH("Nguyen Van A");

        DonHang dh1 = new DonHang();
        dh1.setMaDon("DH001");
        dh1.setMaKH(kh);

        DonHang dh2 = new DonHang();
        dh2.setMaDon("DH002");
        dh2.setMaKH(kh);

        kh.getDonHangs().add(dh1);
        kh.getDonHangs().add(dh2);

        entityManager.persist(kh);
        // no need for tx.begin()/commit() - Spring handles transaction
    }

    @Transactional
    public void removeKhachHang() {
        KhachHang kh = entityManager.find(KhachHang.class, 1);
        entityManager.remove(kh);
        // CascadeType.REMOVE will delete related DonHang
    }

    @Transactional
    public void mergeKhachHang() {
        KhachHang detachedKh = new KhachHang();
        detachedKh.setId(1);
        detachedKh.setTenKH("Update name");

        DonHang newDonHang = new DonHang();
        newDonHang.setMaDon("DH003");
        newDonHang.setMaKH(detachedKh);

        detachedKh.getDonHangs().add(newDonHang);

        entityManager.merge(detachedKh);
        // CascadeType.MERGE applies to DonHang too
    }

    @Transactional
    public void refreshKhachHang() {
        KhachHang kh = entityManager.find(KhachHang.class, 1);
        entityManager.refresh(kh);
        // CascadeType.REFRESH applies to DonHang
    }

    @Transactional
    public void deleteKhachHang() {
        KhachHang kh = entityManager.find(KhachHang.class, 1);
        entityManager.detach(kh);
        // CascadeType.DETACH applies to DonHang
    }


    @Transactional
    public void removeDonHangFromKhachHang(int khachHangId, int donHangId) {
        KhachHang kh = entityManager.find(KhachHang.class, khachHangId);
        if (kh == null) return;

        // Tìm DonHang trong danh sách của KhachHang
        DonHang toRemove = null;
        for (DonHang dh : kh.getDonHangs()) {
            if (dh.getId().equals(donHangId)) {
                toRemove = dh;
                break;
            }
        }

        if (toRemove != null) {
            // Xóa DonHang khỏi collection (orphanRemoval sẽ tự xóa DB)
            kh.removeDonHang(toRemove);
        }
    }


}
