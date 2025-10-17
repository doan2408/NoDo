package com.example.handlehttp.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "DonHang", uniqueConstraints = @UniqueConstraint(columnNames = {"maDon", "moTa"}))
//@JsonIgnoreProperties(value = "maKH", ignoreUnknown = true)
@JsonIgnoreProperties(value = {"ngayDat", "gioDat", "moTa"}, allowGetters = true, allowSetters = true)
public class DonHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDH", nullable = false)
    private Integer id;

    @Size(max = 8)
    @Column(name = "MaDon", length = 8, unique = true)
    private String maDon;

    @Size(max = 200)
    @Nationalized
    @Column(name = "MoTa", length = 200)
    private String moTa;

    @Column(name = "TongTien", precision = 12, scale = 2)
    private BigDecimal tongTien;

    @ColumnDefault("getdate()")
    @Column(name = "NgayDat")
    private Instant ngayDat;

    @Column(name = "GioDat")
    private LocalTime gioDat;

    //    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "MaKH")
    private KhachHang maKH;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public @Size(max = 8) String getMaDon() {
        return maDon;
    }

    public void setMaDon(@Size(max = 8) String maDon) {
        this.maDon = maDon;
    }

    public @Size(max = 200) String getMoTa() {
        return moTa;
    }

    public void setMoTa(@Size(max = 200) String moTa) {
        this.moTa = moTa;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }

    public Instant getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(Instant ngayDat) {
        this.ngayDat = ngayDat;
    }

    public LocalTime getGioDat() {
        return gioDat;
    }

    public void setGioDat(LocalTime gioDat) {
        this.gioDat = gioDat;
    }

    public KhachHang getMaKH() {
        return maKH;
    }

    public void setMaKH(KhachHang maKH) {
        this.maKH = maKH;
    }
}