package com.example.validate.Dto.Response;


import com.example.validate.Enum.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhachHangResponse {
    private Integer id;
    private Long makhLon;
    private Short makhNho;
    private Short makhNhohon;
    private String maCode;
    private String tenKH;
    private String sdt;
    private Status ghiChu;
    private String tenUnicode;
    private Double soDu;
    private Double diem;
    private BigDecimal tyLe;
    private BigDecimal giaTri;
    private LocalDate ngaySinh;
    private LocalTime gioTao;
    private OffsetDateTime ngayTao;
    private Integer makhGioithieu;

    // example: if you want to map DonHang ids or DTOs:
    private Set<DonHangResponse> donHangs;

    public KhachHangResponse(Long makhLon, Short maKhNho, Short maKhNhoLon, String tenKh) {
        this.makhLon = makhLon;
        this.makhNho = maKhNho;
        this.makhNhohon = maKhNhoLon;
        this.tenKH = tenKh;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getMakhLon() {
        return makhLon;
    }

    public void setMakhLon(Long makhLon) {
        this.makhLon = makhLon;
    }

    public Short getMakhNho() {
        return makhNho;
    }

    public void setMakhNho(Short makhNho) {
        this.makhNho = makhNho;
    }

    public Short getMakhNhohon() {
        return makhNhohon;
    }

    public void setMakhNhohon(Short makhNhohon) {
        this.makhNhohon = makhNhohon;
    }

    public String getMaCode() {
        return maCode;
    }

    public void setMaCode(String maCode) {
        this.maCode = maCode;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public Status getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(Status ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getTenUnicode() {
        return tenUnicode;
    }

    public void setTenUnicode(String tenUnicode) {
        this.tenUnicode = tenUnicode;
    }

    public Double getSoDu() {
        return soDu;
    }

    public void setSoDu(Double soDu) {
        this.soDu = soDu;
    }

    public Double getDiem() {
        return diem;
    }

    public void setDiem(Double diem) {
        this.diem = diem;
    }

    public BigDecimal getTyLe() {
        return tyLe;
    }

    public void setTyLe(BigDecimal tyLe) {
        this.tyLe = tyLe;
    }

    public BigDecimal getGiaTri() {
        return giaTri;
    }

    public void setGiaTri(BigDecimal giaTri) {
        this.giaTri = giaTri;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public LocalTime getGioTao() {
        return gioTao;
    }

    public void setGioTao(LocalTime gioTao) {
        this.gioTao = gioTao;
    }

    public OffsetDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(OffsetDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }

    public Integer getMakhGioithieu() {
        return makhGioithieu;
    }

    public void setMakhGioithieu(Integer makhGioithieu) {
        this.makhGioithieu = makhGioithieu;
    }

    public Set<DonHangResponse> getDonHangs() {
        return donHangs;
    }

    public void setDonHangs(Set<DonHangResponse> donHangs) {
        this.donHangs = donHangs;
    }
}

