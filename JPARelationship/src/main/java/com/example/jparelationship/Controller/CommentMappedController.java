package com.example.jparelationship.Controller;

import com.example.jparelationship.Dto.CommentDTO;
import com.example.jparelationship.Service.CommentMappedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentMappedController {
    private final CommentMappedService commentMappedService;

    public CommentMappedController(CommentMappedService commentMappedService) {
        this.commentMappedService = commentMappedService;
    }

    @GetMapping
    public ResponseEntity<List<CommentDTO>> getAllComments(){
        return ResponseEntity.ok(commentMappedService.getAllComments());
    }

    @GetMapping("/searchPostId")
    public ResponseEntity<List<CommentDTO>> getAllCommentsByPostId(@RequestParam("postId") int postId){
        return ResponseEntity.ok(commentMappedService.searchByPostId(postId));
    }

    @PostMapping
    public ResponseEntity<CommentDTO> post(@RequestBody CommentDTO commentDTO) {
        return ResponseEntity.ok(commentMappedService.addComent(commentDTO));
    }

    @PutMapping
    public ResponseEntity<CommentDTO> update(@RequestBody CommentDTO commentDTO) {
        return ResponseEntity.ok(commentMappedService.editComent(commentDTO));
    }

}
