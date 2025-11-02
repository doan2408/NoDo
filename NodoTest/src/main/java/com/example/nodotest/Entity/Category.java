package com.example.nodotest.Entity;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "category_code", nullable = false, unique = true, length = 100)
    private String categoryCode;

    @Column(name = "description", length = 1000)
    private String description;

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

    @OneToMany(mappedBy = "category")
    private Set<ProductCategory> productCategories;


    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<CategoryImage> images = new ArrayList<>();

    // Helper method để add image
    public void addImage(CategoryImage image) {
        if (!images.contains(image)) {
            images.add(image);
            image.setCategory(this);
        }
    }

    public void removeImage(CategoryImage image) {
        images.remove(image);
        image.setCategory(null);
    }

    public Category() {

    }

    public Category(Long id, String name, String categoryCode, String description, String status, java.sql.Date createdDate, java.sql.Date modifiedDate, String createdBy, String modifiedBy, Set<ProductCategory> productCategories) {
        this.id = id;
        this.name = name;
        this.categoryCode = categoryCode;
        this.description = description;
        this.status = status;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.productCategories = productCategories;
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

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<CategoryImage> getImages() {
        return images;
    }

    public void setImages(List<CategoryImage> images) {
        this.images = images;
    }
}
