package com.slinkdigital.wedding.controller;

import com.slinkdigital.wedding.dto.ApiResponse;
import com.slinkdigital.wedding.dto.CommentDto;
import com.slinkdigital.wedding.service.CommentService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
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
@RequestMapping("api/ws/v1/weddings")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("{weddingId}/comments")
    public ResponseEntity<ApiResponse> getCommentsForWedding(@PathVariable(value = "weddingId") Long id) {
        List<CommentDto> comments = commentService.getCommentsForWedding(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("comments", comments))
                        .message("List Of Comments Successful")
                        .status(OK)
                        .build()
        );
    }

    @PostMapping("comments")
    public ResponseEntity<ApiResponse> addComment(@Valid @RequestBody CommentDto commentDto) {
        List<CommentDto> comments = commentService.addComment(commentDto);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("comments", comments))
                        .message("Comment Added Successfully")
                        .status(OK)
                        .build()
        );
    }
    
    @PutMapping("comments")
    public ResponseEntity<ApiResponse> updateComment(@Valid @RequestBody CommentDto commentDto) {
        List<CommentDto> comments = commentService.updateComment(commentDto);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("comments", comments))
                        .message("Comment updated Successfully")
                        .status(OK)
                        .build()
        );
    }

    @DeleteMapping("comments/{id}")
    public ResponseEntity<ApiResponse> removeComment(@PathVariable Long id) {
        List<CommentDto> comments = commentService.removeComment(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("comments", comments))
                        .message("Comment Removed Successfully")
                        .status(OK)
                        .build()
        );
    }
}
