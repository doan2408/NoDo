package com.example.jparelationship.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PostId", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "UserId", nullable = false)
    private User user;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "Title", nullable = false)
    private String title;

    @NotNull
    @Nationalized
    @Lob
    @Column(name = "Content", nullable = false)
    private String content;

    @OneToMany(mappedBy = "post")
    private Set<CommentIdclass> commentIdclasses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "post")
    private Set<CommentMapped> commentMappeds = new LinkedHashSet<>();

}