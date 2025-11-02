package com.example.validate.Dto.Request;


import com.example.mappingdto.Enum.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhachHangRequest {
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
    private Set<DonHangRequest> donHangs;

    public KhachHangRequest(Long makhLon, Short maKhNho, Short maKhNhoLon, String tenKh) {
        this.makhLon = makhLon;
        this.makhNho = maKhNho;
        this.makhNhohon = maKhNhoLon;
        this.tenKH = tenKh;
    }
}

