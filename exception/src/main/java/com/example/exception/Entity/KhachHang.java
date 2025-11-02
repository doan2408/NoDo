package com.example.validate.Entity;

import com.example.validate.Enum.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;


@Entity
@NamedEntityGraph(
        name = "KhachHang.donHangs",
        attributeNodes = {@NamedAttributeNode("donHangs")}
)
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

    @Column(name = "MaCode", length = 10)
    private String maCode;

    @Column(name = "TenKH", length = 100)
    private String tenKH;

    @Column(name = "sdt", length = 10)
    private String sdt;

    @Enumerated(EnumType.STRING)
    @Column(name = "GhiChu")
    private Status ghiChu;

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

    @Column(name = "NgayTao")
    private OffsetDateTime ngayTao;

    @Column(name = "MaKH_GioiThieu")
    private Integer makhGioithieu;

    @OneToMany(mappedBy = "maKH",
            targetEntity = DonHang.class,
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<DonHang> donHangs = new LinkedHashSet<>();

    /*
     TODO [Reverse Engineering] create field to map the 'CapNhat' column
     Available actions: Define target Java type | Uncomment as is | Remove column mapping
        @Column(name = "CapNhat", columnDefinition = "timestamp not null")
        private Object capNhat;
    */

    public void addDonHang(DonHang donHang) {
        donHangs.add(donHang);
        donHang.setMaKH(this);
    }

    public void removeDonHang(DonHang donHang) {
        donHangs.remove(donHang);
        donHang.setMaKH(null);
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

    public Set<DonHang> getDonHangs() {
        return donHangs;
    }

    public void setDonHangs(Set<DonHang> donHangs) {
        this.donHangs = donHangs;
    }
}