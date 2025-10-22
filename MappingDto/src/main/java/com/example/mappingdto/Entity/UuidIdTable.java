package com.example.mappingdto.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "uuid_id_table")
public class UuidIdTable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)  // Hibernate sẽ không tự động sinh UUID, ta cần tự tạo UUID trong @PrePersist
    @Column(columnDefinition = "CHAR(36)") // Đảm bảo rằng kiểu dữ liệu trong DB là CHAR(36)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

}