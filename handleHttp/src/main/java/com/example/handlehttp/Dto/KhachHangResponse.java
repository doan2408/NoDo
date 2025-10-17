package com.example.handlehttp.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KhachHangResponse {
    private Integer id;

    private Long makhLon;

    private Short makhNho;

    private Short makhNhohon;

    private String maCode;

    private String tenKh;

    private String ghiChu;

    private String tenUnicode;

    private Double soDu;

    private Double diem;

    private BigDecimal tyLe;

    private BigDecimal giaTri;

    private LocalDate ngaySinh;

    private LocalTime gioTao;

    private Instant ngayTao;

    public KhachHangResponse(Double soDu, BigDecimal tyLe) {
        this.soDu = soDu;
        this.tyLe = tyLe;
    }
}
