package com.example.nodotest.Entity;

import jakarta.persistence.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "product_code", nullable = false, unique = true, length = 100)
    private String productCode;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "status", columnDefinition = "ENUM('0', '1') DEFAULT '1'")
    private String status;  // "1" = active, "0" = deleted

    @Column(name = "created_date")
    @Temporal(TemporalType.DATE)
    private Date createdDate;

    @Column(name = "modified_date")
    @Temporal(TemporalType.DATE)
    private Date modifiedDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "modified_by", length = 100)
    private String modifiedBy;

    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<ProductCategory> productCategories = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<ProductImage> productImages = new HashSet<>();

    // Helper method to add image
    public void addImage(ProductImage image) {
        if(!productImages.contains(image)) {
            productImages.add(image);
            image.setProduct(this);
        }
    }

    public void addCategory(Category category) {
        ProductCategory productCategory = new ProductCategory();
        ProductCategoryKey key = new ProductCategoryKey();
        productCategory.setId(key);
        productCategory.setProduct(this);
        productCategory.setCategory(category);
        productCategory.setCreatedDate(new Date(new java.util.Date().getTime()));
        productCategory.setModifiedDate(new Date(new java.util.Date().getTime()));
        this.productCategories.add(productCategory);
    }

    public void removeCategory(Category category) {
        productCategories.removeIf(pc -> pc.getCategory().equals(category));
    }

    public Product() {
    }

    public Product(Long id, String name, String description, Double price, String productCode, Long quantity, String status, Date createdDate, Date modifiedDate, String createdBy, String modifiedBy, Set<ProductCategory> productCategories, Set<ProductImage> productImages) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.productCode = productCode;
        this.quantity = quantity;
        this.status = status;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.productCategories = productCategories;
        this.productImages = productImages;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Set<ProductCategory> getProductCategories() {
        return productCategories;
    }

    public void setProductCategories(Set<ProductCategory> productCategories) {
        this.productCategories = productCategories;
    }

    public Set<ProductImage> getProductImages() {
        return productImages;
    }

    public void setProductImages(Set<ProductImage> productImages) {
        this.productImages = productImages;
    }
}
