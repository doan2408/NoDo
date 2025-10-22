package com.example.jparelationship.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentMappedRepository extends JpaRepository<CommentMappedRepository, Integer> {
}
