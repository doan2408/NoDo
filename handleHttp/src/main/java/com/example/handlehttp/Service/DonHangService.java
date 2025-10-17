package com.example.handlehttp.Service;

import com.example.handlehttp.Model.DonHang;
import com.example.handlehttp.Repository.DonHangRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonHangService {
    private int count = 0;
    private final DonHangRepository donHangRepository;

    public DonHangService(DonHangRepository donHangRepository) {
        this.donHangRepository = donHangRepository;
    }

    public List<DonHang> getDonHang() {
        count++;
        System.out.println("count" + count);
        return donHangRepository.findAll();
    }

    public DonHang findById(int id) {
        return donHangRepository.findById(id).get();
    }

    public DonHang updateDonHang(DonHang donHang) {
        System.out.println("hihi");
        return null;
    }
}
