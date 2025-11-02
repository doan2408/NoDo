package com.example.nodotest.Dto.Request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryRequest {
    @NotBlank(message = "{category.name.notblank}")
    @Size(max = 100, message = "{category.name.size}")
    private String name;  // Tên loại sản phẩm

    @NotBlank(message = "{category.code.notblank}")
    @Size(max = 50, message = "{category.code.size}")
    private String categoryCode;  // Mã loại, duy nhất

    @Size(max = 200, message = "{category.description.size}")
    private String description;  // Mô tả loại sản phẩm

    private List<MultipartFile> images;  // Các ảnh của loại sản phẩm

    private List<String> replaceUuids; // list uuids of old images need keep

    public CategoryRequest(String name, String categoryCode, String description, List<MultipartFile> images) {
        this.name = name;
        this.categoryCode = categoryCode;
        this.description = description;
        this.images = images;
    }

    public @NotBlank(message = "{category.name.notblank}") @Size(max = 100, message = "{category.name.size}") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "{category.name.notblank}") @Size(max = 100, message = "{category.name.size}") String name) {
        this.name = name;
    }

    public @NotBlank(message = "{category.code.notblank}") @Size(max = 50, message = "{category.code.size}") String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(@NotBlank(message = "{category.code.notblank}") @Size(max = 50, message = "{category.code.size}") String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public @Size(max = 200, message = "{category.description.size}") String getDescription() {
        return description;
    }

    public void setDescription(@Size(max = 200, message = "{category.description.size}") String description) {
        this.description = description;
    }

    public List<MultipartFile> getImages() {
        return images;
    }

    public void setImages(List<MultipartFile> images) {
        this.images = images;
    }

    public List<String> getReplaceUuids() {
        return replaceUuids;
    }

    public void setReplaceUuids(List<String> replaceUuids) {
        this.replaceUuids = replaceUuids;
    }
}
