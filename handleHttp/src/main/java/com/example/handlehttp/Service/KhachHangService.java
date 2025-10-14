package com.example.handlehttp.Service;

import com.example.handlehttp.Model.KhachHang;
import com.example.handlehttp.Repository.KhachHangRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KhachHangService {
    private final KhachHangRepository khachHangRepository;

    public List<KhachHang> getClient() {
        return khachHangRepository.findAll();
    }

    public KhachHang update(KhachHang khachHang) {
        return khachHangRepository.save(khachHang);
    }

}
