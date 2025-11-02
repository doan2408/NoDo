package com.example.nodotest.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
public class CategoryImageRequest {

    private String name;
    private String uuid;
    private String url;
    private String status;

    private List<MultipartFile> image;  // Ảnh của loại sản phẩm
//    private boolean isPrimary;    // Đánh dấu ảnh chính (true/false)


    public CategoryImageRequest(String name, String uuid, String url, String status) {
        this.name = name;
        this.uuid = uuid;
        this.url = url;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<MultipartFile> getImage() {
        return image;
    }

    public void setImage(List<MultipartFile> image) {
        this.image = image;
    }
}