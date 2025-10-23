package com.example.mappingdto.Dto;


import com.example.mappingdto.Entity.DonHang;
import com.example.mappingdto.Enum.Status;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhachHangDTO {
    private Integer id;
    private Long makhLon;
    private Short makhNho;
    private Short makhNhohon;
    private String maCode;
    private String tenKH;
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
    private Set<DonHangDTO> donHangs;

    public KhachHangDTO (Long makhLon, Short maKhNho, Short maKhNhoLon, String tenKh) {
        this.makhLon = makhLon;
        this.makhNho = maKhNho;
        this.makhNhohon = maKhNhoLon;
        this.tenKH = tenKh;
    }
}

