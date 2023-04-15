package com.slinkdigital.wedding.controller;

import com.slinkdigital.wedding.dto.CommentDto;
import com.slinkdigital.wedding.service.CommentService;
import java.util.List;
import jakarta.validation.Valid;
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
@RequestMapping("api/ws/v1/weddings")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("{weddingId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getWeddingComments(@PathVariable(value = "weddingId") Long id) {
        return commentService.getCommentsForWedding(id);
    }

    @PostMapping("comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@Valid @RequestBody CommentDto commentDto) {
        return commentService.addComment(commentDto);
    }

    @PutMapping("comments")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateComment(@Valid @RequestBody CommentDto commentDto) {
        return commentService.updateComment(commentDto);
    }

    @DeleteMapping("comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeComment(@PathVariable Long id) {
        commentService.removeComment(id);
    }
}
