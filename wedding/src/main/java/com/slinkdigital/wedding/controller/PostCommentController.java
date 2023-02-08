package com.slinkdigital.wedding.controller;

import com.slinkdigital.wedding.dto.ApiResponse;
import com.slinkdigital.wedding.dto.PostCommentDto;
import com.slinkdigital.wedding.service.PostCommentService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<ApiResponse> getPostCommentsForPost(@PathVariable(value = "postId") Long id) {
        List<PostCommentDto> comments = postCommentService.getPostCommentsForPost(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("comments", comments))
                        .message("List Of Comments Successful")
                        .status(OK)
                        .build()
        );
    }
    
    @GetMapping("posts/comments/{id}")
    public ResponseEntity<ApiResponse> getPostComment(@PathVariable Long id) {
        PostCommentDto comments = postCommentService.getPostComment(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("comments", comments))
                        .message("Comment Successful")
                        .status(OK)
                        .build()
        );
    }

    @PostMapping(path = "posts/comments")
    public ResponseEntity<ApiResponse> addPost(@RequestBody PostCommentDto postCommentDto) {
        postCommentDto = postCommentService.addPostComment(postCommentDto);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("comment", postCommentDto))
                        .message("Post Comment Added Successfully")
                        .status(OK)
                        .build()
        );
    }

    @PutMapping("posts/comments")
    public ResponseEntity<ApiResponse> editPostComment(@RequestBody PostCommentDto postCommentDto) {
        postCommentDto = postCommentService.editPostComment(postCommentDto);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("post", postCommentDto))
                        .message("Comment Updated Successfully")
                        .status(OK)
                        .build()
        );
    }
    
    @DeleteMapping("posts/comments/{id}")
    public ResponseEntity<ApiResponse> deletePostComment(@PathVariable Long id) {
        Map<String, String> result = postCommentService.removePostComment(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("isDeleted", true))
                        .message(result.get("success"))
                        .status(OK)
                        .build()
        );
    }    
    
}
