package com.example.jparelationship.Service;

import com.example.jparelationship.Dto.CommentDTO;
import com.example.jparelationship.Entity.CommentId;
import com.example.jparelationship.Entity.CommentMapped;
import com.example.jparelationship.Entity.Post;
import com.example.jparelationship.Entity.User;
import com.example.jparelationship.Mapper.CommentMapper;
import com.example.jparelationship.Repository.CommentMappedRepository;
import com.example.jparelationship.Repository.PostRepository;
import com.example.jparelationship.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentMappedService {
    private final CommentMappedRepository commentMappedRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    public CommentMappedService(CommentMappedRepository commentMappedRepository, CommentMapper commentMapper,
                                PostRepository postRepository, UserRepository userRepository) {
        this.commentMappedRepository = commentMappedRepository;
        this.commentMapper = commentMapper;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // entity -> dto
    public CommentDTO toDto(CommentMapped comment) {
        return commentMapper.toDTO(comment);
    }

    // dto -> entity
    public CommentMapped toEntity(CommentDTO dto) {
        return commentMapper.toEntity(dto);
    }

    // get all
    public List<CommentDTO> getAllComments() {
        List<CommentMapped> list = commentMappedRepository.findAll();
        return list.stream().map(this::toDto).toList();
    }

    // search by postId
    public List<CommentDTO> searchByPostId(int postId){
        List<CommentMapped> list = commentMappedRepository.findByPostId(postId);
        return list.stream().map(this::toDto).toList();
    }

    // search by userID
    public List<CommentDTO> searchByUserId(int userId) {
        List<CommentMapped> list = commentMappedRepository.findByUserId(userId);
        return list.stream().map(this::toDto).toList();
    }

    // add
    public CommentDTO addComent(CommentDTO dto) {
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(()-> new RuntimeException("User not found"));

        CommentMapped comment = commentMapper.toEntity(dto);
        comment.setPost(post);
        comment.setUser(user);

        CommentMapped saveComment = commentMappedRepository.save(comment);
        return commentMapper.toDTO(saveComment);
    }

    // update
    public CommentDTO editComent(CommentDTO dto) {
//        CommentMapped commentMapped = commentMappedRepository.findById(new CommentId(dto.getUserId(), dto.getPostId())).get();
        CommentId commentId = new CommentId(dto.getUserId(), dto.getPostId());

        CommentMapped commentMappedExist = commentMappedRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        System.out.println("Found comment: " + commentMappedExist.getCommentText());

        commentMappedExist.setCommentText(dto.getCommentText());
        CommentMapped eidted = commentMappedRepository.save(commentMappedExist);
        return commentMapper.toDTO(eidted);
    }



}
