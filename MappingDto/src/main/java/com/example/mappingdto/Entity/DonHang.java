package com.example.mappingdto.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.OffsetDateTime;

@Getter
@Setter
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
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

}