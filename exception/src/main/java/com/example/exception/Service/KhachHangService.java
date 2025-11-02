package com.example.validate.Service;

import com.example.validate.Dto.Request.KhachHangRequest;
import com.example.validate.Dto.Response.KhachHangResponse;
import com.example.validate.Entity.KhachHang;
import com.example.validate.Mapper.KhachHangMapper;
import com.example.validate.Repository.KhachHangRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class KhachHangService {
    private final KhachHangRepository khachHangRepository;
    private final KhachHangMapper khachHangMapper;

    public KhachHangService(KhachHangRepository khachHangRepository, KhachHangMapper khachHangMapper) {
        this.khachHangRepository = khachHangRepository;
        this.khachHangMapper = khachHangMapper;
    }

    @Transactional
    public Page<KhachHangResponse> getAll(Pageable pageable) {
        Page<KhachHang> page = khachHangRepository.findAll(pageable);

        return page.map(khachHangMapper::toDto);
    }

    public KhachHangResponse create(KhachHangRequest request) {
        KhachHang entity = khachHangMapper.toEntity(request);
        KhachHang saved = khachHangRepository.save(entity);
        return khachHangMapper.toDto(saved);
    }

    public KhachHangResponse update(Integer id, KhachHangRequest request) {
        KhachHang entity = khachHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

        khachHangMapper.updateEntityFromRequest(request, entity);
        KhachHang updated = khachHangRepository.save(entity);
        return khachHangMapper.toDto(updated);
    }

    public KhachHangResponse findById(Integer id) {
        KhachHang entity = khachHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
        return khachHangMapper.toDto(entity);
    }

    @Transactional
    public List<KhachHangResponse> findByMaCode(String maCode) {
        String input = escapeLike(maCode);

//        System.out.println("input: " + input);
        System.out.println("input: " + input);

        List<KhachHang> resultList = khachHangRepository.findByMaCode(input);
        if (resultList.isEmpty()) {
            throw new EntityNotFoundException("ma code not found");
        }
        List<KhachHangResponse> response = khachHangMapper.toDoList(resultList);
        return response;
    }

    public static String escapeLike(String param) {
        if (param == null) return null;
        return param.replace("\\\\", "\\\\\\\\") // escape dấu \\ nếu có
                .replace("_", "\\_")            // escape dấu _ nếu có
                .replace("%", "\\%");          // escape dấu % nếu có
    }


}
