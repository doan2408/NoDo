package com.example.handlehttp.Repository;

import com.example.handlehttp.Model.UuidIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UUIDRepository extends JpaRepository<UuidIdEntity, String> {
}
