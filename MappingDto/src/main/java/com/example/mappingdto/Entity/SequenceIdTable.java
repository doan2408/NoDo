package com.example.mappingdto.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sequence_id_table")
public class SequenceIdTable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen")
    @SequenceGenerator(name = "seq_gen", sequenceName = "seq_id", allocationSize = 1)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

}