package com.example.handlehttp.Service;

import com.example.handlehttp.Model.SequenceIdEntity;
import com.example.handlehttp.Repository.SequenceRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SequenceService {
    private final SequenceRepo sequenceRepo;

    @Transactional
    public void saveSequenceIdEntity() {
        SequenceIdEntity entity = new SequenceIdEntity();
        entity.setId(1000L);
        entity.setName("Sequence ID Entity");
        sequenceRepo.save(entity);
    }

    public SequenceIdEntity getSequenceIdEntity(Integer id) {
        return sequenceRepo.findById(id).orElse(null);
    }
}
