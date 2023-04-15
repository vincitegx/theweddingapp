package com.slinkdigital.wedding.controller;

import com.slinkdigital.wedding.dto.ReactionDto;
import com.slinkdigital.wedding.service.ReactionService;
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
@RequestMapping("api/ws/v1/weddings")
@RequiredArgsConstructor
public class ReactionController {
    
    private final ReactionService reactionService;

    @GetMapping("{weddingId}/reactions")
    @ResponseStatus(HttpStatus.OK)
    public List<ReactionDto> getReactionsForWedding(@PathVariable(value = "weddingId") Long id) {
        return reactionService.getReactionsForWedding(id);
    }

    @PostMapping("reactions")
    @ResponseStatus(HttpStatus.CREATED)
    public ReactionDto addReaction(@RequestBody ReactionDto reactionDto) {
        return reactionService.addReaction(reactionDto);
    }

    @PutMapping("reactions")
    @ResponseStatus(HttpStatus.OK)
    public List<ReactionDto> updateReaction(@RequestBody ReactionDto reactionDto) {
        return reactionService.updateReaction(reactionDto);
    }
    
    @DeleteMapping("reactions/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeReaction(@PathVariable Long id) {
        reactionService.removeReaction(id);
    }    
}
