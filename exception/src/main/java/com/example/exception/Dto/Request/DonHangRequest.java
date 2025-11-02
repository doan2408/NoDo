package com.example.validate.Dto.Request;

import com.example.validate.CheckValidator.OnCreate;
import com.example.validate.CheckValidator.OnUpdate;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.OffsetDateTime;

@NoArgsConstructor
@AllArgsConstructor

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DonHangRequest {
    @NotNull(groups = OnUpdate.class)
    private Integer id;

    private String maDon;

    @NotBlank(groups = OnCreate.class)
    private String moTa;

    private BigDecimal tongTien;

    private OffsetDateTime ngayDat;

    private LocalTime gioDat;

    private String tenKh;

    // foreign key
    private Integer maKHId;

    public DonHangRequest(String maDon, String moTa, String tenKh) {
        this.maDon = maDon;
        this.moTa = moTa;
        this.tenKh = tenKh;
    }

    public @NotNull(groups = OnUpdate.class) Integer getId() {
        return id;
    }

    public void setId(@NotNull(groups = OnUpdate.class) Integer id) {
        this.id = id;
    }

    public String getMaDon() {
        return maDon;
    }

    public void setMaDon(String maDon) {
        this.maDon = maDon;
    }

    public @NotBlank(groups = OnCreate.class) String getMoTa() {
        return moTa;
    }

    public void setMoTa(@NotBlank(groups = OnCreate.class) String moTa) {
        this.moTa = moTa;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }

    public OffsetDateTime getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(OffsetDateTime ngayDat) {
        this.ngayDat = ngayDat;
    }

    public LocalTime getGioDat() {
        return gioDat;
    }

    public void setGioDat(LocalTime gioDat) {
        this.gioDat = gioDat;
    }

    public String getTenKh() {
        return tenKh;
    }

    public void setTenKh(String tenKh) {
        this.tenKh = tenKh;
    }

    public Integer getMaKHId() {
        return maKHId;
    }

    public void setMaKHId(Integer maKHId) {
        this.maKHId = maKHId;
    }
}
