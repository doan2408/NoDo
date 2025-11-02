package com.example.validate.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.OffsetDateTime;


@Entity
@NamedEntityGraph(
        name = "Customer.orders",
        attributeNodes = @NamedAttributeNode("maKH")
)
public class DonHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDH", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "MaKH", nullable = false)
    private KhachHang maKH;

    @Column(name = "MaDon", length = 8)
    private String maDon;

    @Nationalized
    @Column(name = "MoTa", length = 200)
    private String moTa;

    @Column(name = "TongTien", precision = 12, scale = 2)
    private BigDecimal tongTien;

    @Column(name = "NgayDat")
    private OffsetDateTime ngayDat;

    @Column(name = "GioDat")
    private LocalTime gioDat;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public KhachHang getMaKH() {
        return maKH;
    }

    public void setMaKH(KhachHang maKH) {
        this.maKH = maKH;
    }

    public String getMaDon() {
        return maDon;
    }

    public void setMaDon(String maDon) {
        this.maDon = maDon;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
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
}