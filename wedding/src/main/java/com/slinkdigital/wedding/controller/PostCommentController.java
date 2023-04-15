package com.slinkdigital.wedding.controller;

import com.slinkdigital.wedding.dto.PostCommentDto;
import com.slinkdigital.wedding.service.PostCommentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author TEGA
 */
@RestController
@RequestMapping("api/ws/v1/weddings/posts")
@RequiredArgsConstructor
public class PostCommentController {
    
    private final PostCommentService postCommentService;
    
    @GetMapping("{postId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<PostCommentDto> getPostCommentsForPost(@PathVariable(value = "postId") Long id) {
        return postCommentService.getPostCommentsForPost(id);
    }
    
    @GetMapping("comments/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostCommentDto getPostComment(@PathVariable Long id) {
        return postCommentService.getPostComment(id);
    }

    @PostMapping(path = "comments")
    @ResponseStatus(HttpStatus.CREATED)
    public PostCommentDto addPost(@RequestBody PostCommentDto postCommentDto) {
        return postCommentService.addPostComment(postCommentDto);
    }

    @PutMapping("comments")
    @ResponseStatus(HttpStatus.OK)
    public PostCommentDto editPostComment(@RequestBody PostCommentDto postCommentDto) {
        return postCommentService.editPostComment(postCommentDto);
    }
    
    @DeleteMapping("comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePostComment(@PathVariable Long id) {
        postCommentService.removePostComment(id);
    }        
}
