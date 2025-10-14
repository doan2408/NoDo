package com.example.handlehttp.Service;

import com.example.handlehttp.Model.DonHang;
import com.example.handlehttp.Repository.DonHangRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DonHangService {
    private final DonHangRepository donHangRepository;

    public List<DonHang> getDonHang() {
        return donHangRepository.findAll();
    }

    public DonHang updateDonHang(DonHang donHang) {
        return donHangRepository.save(donHang);
    }
}
