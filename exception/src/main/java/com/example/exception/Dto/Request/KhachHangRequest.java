package com.example.validate.Dto.Request;

import com.example.validate.Annotation.ValidPhone;
import com.example.validate.CheckValidator.OnCreate;
import com.example.validate.CheckValidator.OnUpdate;
import com.example.validate.Enum.Status;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
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
    @NotNull(groups = OnUpdate.class, message = "ID không được null khi update")
    private Integer id;

    @NotNull(message = "Mã khách hàng lớn không được null")
    private Long makhLon;

    @Min(value = 1, message = "Mã khách nhỏ phải lớn hơn 0")
    private Short makhNho;

    private Short makhNhohon;

    @NotBlank(message = "Mã code không được để trống")
    private String maCode;

    @NotBlank(groups = OnCreate.class, message = "Tên khách hàng bắt buộc khi tạo")
    @Size(min = 2, max = 50, message = "Tên KH từ 2 đến 50 ký tự")
    private String tenKH;

    @NotBlank(message = "Số điện thoại không được để trống")
    @ValidPhone(message = "Số điện thoại không đúng định dạng")
    private String sdt;

    @NotNull
    private Status ghiChu;

    @NotBlank
    private String tenUnicode;

    @PositiveOrZero(message = "Số dư không được âm")
    private Double soDu;

    @Positive(message = "Điểm phải > 0")
    private Double diem;

    @Digits(integer = 5, fraction = 2, message = "Tỷ lệ chỉ cho phép tối đa 5 số nguyên và 2 số thập phân")
    private BigDecimal tyLe;

    @DecimalMin(value = "0.0", message = "Giá trị phải >= 0")
    private BigDecimal giaTri;

    @Past(message = "Ngày sinh phải ở quá khứ")
    private LocalDate ngaySinh;

    private LocalTime gioTao;

    @PastOrPresent(message = "Ngày tạo phải là quá khứ hoặc hiện tại")
    private OffsetDateTime ngayTao;

    private Integer makhGioithieu;

    // example: if you want to map DonHang ids or DTOs:
    @Valid
    private Set<DonHangRequest> donHangs;

    public KhachHangRequest(Long makhLon, Short maKhNho, Short maKhNhoLon, String tenKh) {
        this.makhLon = makhLon;
        this.makhNho = maKhNho;
        this.makhNhohon = maKhNhoLon;
        this.tenKH = tenKh;
    }

    public @NotNull(groups = OnUpdate.class, message = "ID không được null khi update") Integer getId() {
        return id;
    }

    public void setId(@NotNull(groups = OnUpdate.class, message = "ID không được null khi update") Integer id) {
        this.id = id;
    }

    public @NotNull(message = "Mã khách hàng lớn không được null") Long getMakhLon() {
        return makhLon;
    }

    public void setMakhLon(@NotNull(message = "Mã khách hàng lớn không được null") Long makhLon) {
        this.makhLon = makhLon;
    }

    public @Min(value = 1, message = "Mã khách nhỏ phải lớn hơn 0") Short getMakhNho() {
        return makhNho;
    }

    public void setMakhNho(@Min(value = 1, message = "Mã khách nhỏ phải lớn hơn 0") Short makhNho) {
        this.makhNho = makhNho;
    }

    public Short getMakhNhohon() {
        return makhNhohon;
    }

    public void setMakhNhohon(Short makhNhohon) {
        this.makhNhohon = makhNhohon;
    }

    public @NotBlank(message = "Mã code không được để trống") String getMaCode() {
        return maCode;
    }

    public void setMaCode(@NotBlank(message = "Mã code không được để trống") String maCode) {
        this.maCode = maCode;
    }

    public @NotBlank(groups = OnCreate.class, message = "Tên khách hàng bắt buộc khi tạo") @Size(min = 2, max = 50, message = "Tên KH từ 2 đến 50 ký tự") String getTenKH() {
        return tenKH;
    }

    public void setTenKH(@NotBlank(groups = OnCreate.class, message = "Tên khách hàng bắt buộc khi tạo") @Size(min = 2, max = 50, message = "Tên KH từ 2 đến 50 ký tự") String tenKH) {
        this.tenKH = tenKH;
    }

    public @NotBlank(message = "Số điện thoại không được để trống") String getSdt() {
        return sdt;
    }

    public void setSdt(@NotBlank(message = "Số điện thoại không được để trống") String sdt) {
        this.sdt = sdt;
    }

    public @NotNull Status getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(@NotNull Status ghiChu) {
        this.ghiChu = ghiChu;
    }

    public @NotBlank String getTenUnicode() {
        return tenUnicode;
    }

    public void setTenUnicode(@NotBlank String tenUnicode) {
        this.tenUnicode = tenUnicode;
    }

    public @PositiveOrZero(message = "Số dư không được âm") Double getSoDu() {
        return soDu;
    }

    public void setSoDu(@PositiveOrZero(message = "Số dư không được âm") Double soDu) {
        this.soDu = soDu;
    }

    public @Positive(message = "Điểm phải > 0") Double getDiem() {
        return diem;
    }

    public void setDiem(@Positive(message = "Điểm phải > 0") Double diem) {
        this.diem = diem;
    }

    public @Digits(integer = 5, fraction = 2, message = "Tỷ lệ chỉ cho phép tối đa 5 số nguyên và 2 số thập phân") BigDecimal getTyLe() {
        return tyLe;
    }

    public void setTyLe(@Digits(integer = 5, fraction = 2, message = "Tỷ lệ chỉ cho phép tối đa 5 số nguyên và 2 số thập phân") BigDecimal tyLe) {
        this.tyLe = tyLe;
    }

    public @DecimalMin(value = "0.0", message = "Giá trị phải >= 0") BigDecimal getGiaTri() {
        return giaTri;
    }

    public void setGiaTri(@DecimalMin(value = "0.0", message = "Giá trị phải >= 0") BigDecimal giaTri) {
        this.giaTri = giaTri;
    }

    public @Past(message = "Ngày sinh phải ở quá khứ") LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(@Past(message = "Ngày sinh phải ở quá khứ") LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public LocalTime getGioTao() {
        return gioTao;
    }

    public void setGioTao(LocalTime gioTao) {
        this.gioTao = gioTao;
    }

    public @PastOrPresent(message = "Ngày tạo phải là quá khứ hoặc hiện tại") OffsetDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(@PastOrPresent(message = "Ngày tạo phải là quá khứ hoặc hiện tại") OffsetDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }

    public Integer getMakhGioithieu() {
        return makhGioithieu;
    }

    public void setMakhGioithieu(Integer makhGioithieu) {
        this.makhGioithieu = makhGioithieu;
    }

    public @Valid Set<DonHangRequest> getDonHangs() {
        return donHangs;
    }

    public void setDonHangs(@Valid Set<DonHangRequest> donHangs) {
        this.donHangs = donHangs;
    }
}

