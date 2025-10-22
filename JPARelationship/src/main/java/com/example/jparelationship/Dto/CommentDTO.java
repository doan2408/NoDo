package com.example.jparelationship.Dto;

import java.time.LocalDateTime;

public class CommentDTO {

    private int postId;  // Phần của CompositeKey
    private int userId;  // Phần của CompositeKey

    private String commentText;
    private LocalDateTime createdAt;
    // Constructor
    public CommentDTO() {}

    public CommentDTO( int postId, int userId, String commentText, LocalDateTime createdAt) {
        this.postId = postId;
        this.userId = userId;
        this.commentText = commentText;
        this.createdAt = createdAt;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}



