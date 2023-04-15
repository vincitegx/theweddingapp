package com.slinkdigital.wedding.controller;

import com.slinkdigital.wedding.dto.PostReactionDto;
import com.slinkdigital.wedding.service.PostReactionService;
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
public class PostReactionController {
    
    private final PostReactionService postReactionService;

    @GetMapping("{postId}/reactions")
    @ResponseStatus(HttpStatus.OK)
    public List<PostReactionDto> getReactionsForPost(@PathVariable(value = "postId") Long id) {
        return postReactionService.getReactionsForPost(id);
    }

    @PostMapping("reactions")
    @ResponseStatus(HttpStatus.CREATED)
    public PostReactionDto addReaction(@RequestBody PostReactionDto reactionDto) {
        return postReactionService.addPostReaction(reactionDto);
    }

    @PutMapping("reactions")
    @ResponseStatus(HttpStatus.OK)
    public PostReactionDto updateReaction(@RequestBody PostReactionDto reactionDto) {
        return postReactionService.editPostReaction(reactionDto);
    }
    
    @DeleteMapping("reactions/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeReaction(@PathVariable Long id) {
        postReactionService.removePostReaction(id);
    }    
}
