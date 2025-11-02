package com.example.nodotest.Dto.Request;

import com.example.nodotest.Entity.Category;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    @NotBlank(message = "{product.name.notBlank}")
    @Size(max = 200, message = "{product.name.size}")
    private String name;

    @NotBlank(message = "{product.code.notBlank}")
    @Size(max = 100, message = "{product.code.size}")
    private String productCode;

    @NotNull(message = "{product.price.notNull}")
    @DecimalMin(value = "0.0", inclusive = true, message = "{product.price.min}")
    private Double price;

    @NotNull(message = "{product.quantity.notNull}")
    @Min(value = 0, message = "{product.quantity.min}")
    private Long quantity;

    @NotEmpty(message = "{product.categoryIds.notEmpty}")
    private List<Long> categoryIds;

    private List<MultipartFile> images;

    public @NotBlank(message = "{product.name.notBlank}") @Size(max = 200, message = "{product.name.size}") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "{product.name.notBlank}") @Size(max = 200, message = "{product.name.size}") String name) {
        this.name = name;
    }

    public @NotBlank(message = "{product.code.notBlank}") @Size(max = 100, message = "{product.code.size}") String getProductCode() {
        return productCode;
    }

    public void setProductCode(@NotBlank(message = "{product.code.notBlank}") @Size(max = 100, message = "{product.code.size}") String productCode) {
        this.productCode = productCode;
    }

    public @NotNull(message = "{product.price.notNull}") @DecimalMin(value = "0.0", inclusive = true, message = "{product.price.min}") Double getPrice() {
        return price;
    }

    public void setPrice(@NotNull(message = "{product.price.notNull}") @DecimalMin(value = "0.0", inclusive = true, message = "{product.price.min}") Double price) {
        this.price = price;
    }

    public @NotNull(message = "{product.quantity.notNull}") @Min(value = 0, message = "{product.quantity.min}") Long getQuantity() {
        return quantity;
    }

    public void setQuantity(@NotNull(message = "{product.quantity.notNull}") @Min(value = 0, message = "{product.quantity.min}") Long quantity) {
        this.quantity = quantity;
    }

    public @NotEmpty(message = "{product.categoryIds.notEmpty}") List<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(@NotEmpty(message = "{product.categoryIds.notEmpty}") List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public List<MultipartFile> getImages() {
        return images;
    }

    public void setImages(List<MultipartFile> images) {
        this.images = images;
    }
}
