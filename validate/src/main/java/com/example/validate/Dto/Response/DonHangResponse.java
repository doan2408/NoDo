package com.example.validate.Dto.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DonHangResponse {
    private Integer id;

    private String maDon;

    private String moTa;

    private BigDecimal tongTien;

    private OffsetDateTime ngayDat;

    private LocalTime gioDat;

    private String tenKh;

    // foreign key
    private Integer maKHId;

    public DonHangResponse(String maDon, String moTa, String tenKh) {
        this.maDon = maDon;
        this.moTa = moTa;
        this.tenKh = tenKh;
    }

    public static class Builder {
        private Integer id;
        private String maDon;
        private String moTa;
        private BigDecimal tongTien;
        private OffsetDateTime ngayDat;
        private LocalTime gioDat;
        private Integer maKHId;
        private String tenKh;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder maDon(String maDon) {
            this.maDon = maDon;
            return this;
        }

        public Builder moTa(String moTa) {
            this.moTa = moTa;
            return this;
        }

        public Builder tongTien(BigDecimal tongTien) {
            this.tongTien = tongTien;
            return this;
        }

        public Builder ngayDat(OffsetDateTime ngayDat) {
            this.ngayDat = ngayDat;
            return this;
        }

        public Builder gioDat(LocalTime gioDat) {
            this.gioDat = gioDat;
            return this;
        }

        public Builder maKHId(Integer maKHId) {
            this.maKHId = maKHId;
            return this;
        }

        public Builder tenKh(String tenKh) {
            this.tenKh = tenKh;
            return this;
        }

        // build ra đối tượng thật
        public DonHangResponse build() {
            return new DonHangResponse(id, maDon, moTa, tongTien, ngayDat, gioDat, tenKh, maKHId);
        }
    }


}
