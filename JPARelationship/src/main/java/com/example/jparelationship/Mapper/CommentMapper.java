package com.example.jparelationship.Mapper;

import com.example.jparelationship.Dto.CommentDTO;
import com.example.jparelationship.Entity.CommentMapped;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "id.postId", target = "postId")
    @Mapping(source = "id.userId", target = "userId")
    CommentDTO toDTO(CommentMapped commentMapped);


    @Mapping(source = "postId", target = "id.postId")
    @Mapping(source = "userId", target = "id.userId")
    CommentMapped toEntity(CommentDTO commentDTO);
}
