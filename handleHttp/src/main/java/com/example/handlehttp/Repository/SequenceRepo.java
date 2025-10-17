package com.example.handlehttp.Repository;

import com.example.handlehttp.Model.SequenceIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface SequenceRepo extends JpaRepository<SequenceIdEntity, Integer> {
}
