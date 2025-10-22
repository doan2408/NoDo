package com.example.jparelationship.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "Comment_Mapped")
public class CommentMapped {

    @EmbeddedId
    private CommentId id;

    @MapsId("postId")
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PostId", nullable = false)
    private Post post;

    @MapsId("userId")
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "UserId", nullable = false)
    private User user;

    @NotNull
    @Nationalized
    @Lob
    @Column(name = "CommentText", nullable = false)
    private String commentText;

    @NotNull
    @ColumnDefault("getdate()")
    @Column(name = "CreatedAt", nullable = false)
    private Instant createdAt;

}