package com.example.nodotest.Entity;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "Category_Image")
public class CategoryImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "category_id", updatable = false, insertable = false)
    private Long categoryId;  // Thêm field này để tránh lazy loading

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;  // Liên kết với Category

    @Column(name = "name", nullable = false, length = 255)
    private String name;  // Tên ảnh

    @Column(name = "url", nullable = false, length = 1000)
    private String url;  // Đường dẫn hoặc URL ảnh

    @Column(name = "uuid", nullable = false, unique = true, length = 36)
    private String uuid;  // UUID của ảnh (đảm bảo duy nhất)

    @Column(name = "status", columnDefinition = "ENUM('0', '1') DEFAULT '1'")
    private String status;  // Trạng thái ảnh (1 = active, 0 = deleted)

    @Column(name = "created_date")
    @Temporal(TemporalType.DATE)
    private Date createdDate;  // Ngày tạo ảnh

    @Column(name = "modified_date")
    @Temporal(TemporalType.DATE)
    private Date modifiedDate;  // Ngày sửa ảnh

    @Column(name = "created_by", length = 100)
    private String createdBy;  // Người tạo ảnh

    @Column(name = "modified_by", length = 100)
    private String modifiedBy;  // Người sửa ảnh

    public CategoryImage() {
    }


    public CategoryImage(Long id, Category category, String name, String url, String uuid, String status, Date createdDate, Date modifiedDate, String createdBy, String modifiedBy) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.url = url;
        this.uuid = uuid;
        this.status = status;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
}
