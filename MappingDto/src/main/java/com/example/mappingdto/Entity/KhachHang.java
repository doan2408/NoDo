package com.example.mappingdto.Entity;

import com.example.mappingdto.Enum.Status;
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

@Getter
@Setter
@Entity
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
    @OneToMany(mappedBy = "maKH", targetEntity = DonHang.class)
    private Set<DonHang> donHangs = new LinkedHashSet<>();

/*
 TODO [Reverse Engineering] create field to map the 'CapNhat' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "CapNhat", columnDefinition = "timestamp not null")
    private Object capNhat;
*/
}