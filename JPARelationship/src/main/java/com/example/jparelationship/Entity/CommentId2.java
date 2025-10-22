package com.example.jparelationship.Entity;

import java.io.Serializable;

public class CommentId2 implements Serializable {
    private int postId;
    private int userId;

    public CommentId2() {
    }

    public CommentId2(int postId, int userId) {
        this.postId = postId;
        this.userId = userId;
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
}
