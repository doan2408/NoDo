package com.example.jparelationship.Repository;

import com.example.jparelationship.Entity.CommentId;
import com.example.jparelationship.Entity.CommentMapped;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentMappedRepository extends JpaRepository<CommentMapped, Integer> {

    Optional<CommentMapped> findById(CommentId id);

//    @Query("select dh from CommentMapped dh " +
//            "where dh.user.id = ?1 and dh.post.id = ?2")
//    CommentMapped getCommentById(int user, int post);

    // Tìm kiếm theo postId
    List<CommentMapped> findByPostId(int postId);

    // Tìm kiếm theo userId
    List<CommentMapped> findByUserId(int userId);
}
