package com.example.handlehttp.Service;

import com.example.handlehttp.Model.UuidIdEntity;
import com.example.handlehttp.Repository.UUIDRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UUIDService {
    private final UUIDRepository uuidRepository;

    public void saveUuidEntity(UuidIdEntity entity) {
        uuidRepository.save(entity);
    }

    public UuidIdEntity getUuidEntity(String id) {
        return uuidRepository.findById(id).orElse(null);
    }
}
