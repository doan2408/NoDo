package com.example.handlehttp.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Set;


@Entity
@Table(name = "KhachHang")
//@JsonIgnoreProperties({"ngayTao", "donHangs"})
public class KhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaKH", nullable = false)
    private Integer id;

    @Column(name = "MaKH_Lon")
    private Long makhLon;

    @Column(name = "MaKH_Nho")
    private Short makhNho;

    @Column(name = "MaKH_NhoHon", columnDefinition = "tinyint")
    private Short makhNhohon;

    @Size(max = 10)
    @Column(name = "MaCode", length = 10)
    private String maCode;

    @Size(max = 100)
    @Column(name = "TenKH", length = 100)
    private String tenKH;

    @Lob
    @Column(name = "GhiChu")
    private String ghiChu;

    @Size(max = 100)
    @Nationalized
    @Column(name = "TenUnicode", length = 100)
    private String tenUnicode;

    @Column(name = "SoDu")
    private Double soDu;

    @Column(name = "Diem")
    private Double diem;

    @Column(name = "TyLe", precision = 10, scale = 2)
    private BigDecimal tyLe;

    @Column(name = "GiaTri", precision = 12, scale = 4)
    private BigDecimal giaTri;

    @Column(name = "NgaySinh")
    private LocalDate ngaySinh;

    @Column(name = "GioTao")
    private LocalTime gioTao;

    @JsonIgnoreProperties("ngayTao")
    @Column(name = "NgayTao")
    private Instant ngayTao;

//    @JsonIgnore
    @OneToMany(mappedBy = "maKH")
    private Set<DonHang> donHangs = new LinkedHashSet<>();

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

    public @Size(max = 10) String getMaCode() {
        return maCode;
    }

    public void setMaCode(@Size(max = 10) String maCode) {
        this.maCode = maCode;
    }

    public @Size(max = 100) String getTenKH() {
        return tenKH;
    }

    public void setTenKH(@Size(max = 100) String tenKH) {
        this.tenKH = tenKH;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public @Size(max = 100) String getTenUnicode() {
        return tenUnicode;
    }

    public void setTenUnicode(@Size(max = 100) String tenUnicode) {
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

    public Instant getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Instant ngayTao) {
        this.ngayTao = ngayTao;
    }

    public Set<DonHang> getDonHangs() {
        return donHangs;
    }

    public void setDonHangs(Set<DonHang> donHangs) {
        this.donHangs = donHangs;
    }


    /*
 TODO [Reverse Engineering] create field to map the 'CapNhat' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "CapNhat", columnDefinition = "timestamp not null")
    private Object capNhat;
*/
}