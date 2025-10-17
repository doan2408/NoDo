package com.example.handlehttp.Model;


import jakarta.persistence.*;


@Entity
@Table(name = "uuid_id_table")
public class UuidIdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)  // Hibernate sẽ không tự động sinh UUID, ta cần tự tạo UUID trong @PrePersist
    @Column(columnDefinition = "CHAR(36)") // Đảm bảo rằng kiểu dữ liệu trong DB là CHAR(36)
    private String id;

    private String name;

    // Hàm tạo ID UUID
//    @PrePersist
//    public void generateUUID() {
//        if (id == null) {
//            id = UUID.randomUUID().toString();
//        }
//    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
