package com.example.jparelationship.Entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CommentId implements Serializable {
    private int postId;
    private int userId;

    public CommentId() {
    }

    public CommentId(int userId, int postId) {
        this.userId = userId;
        this.postId = postId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentId commentId = (CommentId) o;
        return postId == commentId.postId && userId == commentId.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, userId);
    }
}
