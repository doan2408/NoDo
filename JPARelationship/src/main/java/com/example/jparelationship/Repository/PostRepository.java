package com.example.jparelationship.Repository;

import com.example.jparelationship.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}
