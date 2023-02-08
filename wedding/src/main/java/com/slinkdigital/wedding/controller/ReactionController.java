package com.slinkdigital.wedding.controller;

import com.slinkdigital.wedding.dto.ApiResponse;
import com.slinkdigital.wedding.dto.ReactionDto;
import com.slinkdigital.wedding.service.ReactionService;
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
@RequestMapping("api/ws/v1/weddings")
@RequiredArgsConstructor
public class ReactionController {
    
    private final ReactionService reactionService;

    @GetMapping("{weddingId}/reactions")
    public ResponseEntity<ApiResponse> getReactionsForWedding(@PathVariable(value = "weddingId") Long id) {
        List<ReactionDto> reactions = reactionService.getReactionsForWedding(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("reactions", reactions))
                        .message("List Of Reaction Successful")
                        .status(OK)
                        .build()
        );
    }

    @PostMapping("reactions")
    public ResponseEntity<ApiResponse> addReaction(@RequestBody ReactionDto reactionDto) {
        List<ReactionDto> reactions = reactionService.addReaction(reactionDto);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("reactions", reactions))
                        .message("Reaction Added Successfully")
                        .status(OK)
                        .build()
        );
    }

    @PutMapping("reactions")
    public ResponseEntity<ApiResponse> updateReaction(@RequestBody ReactionDto reactionDto) {
        List<ReactionDto> reactions = reactionService.updateReaction(reactionDto);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("reactions", reactions))
                        .message("Reaction Updated Successfully")
                        .status(OK)
                        .build()
        );
    }
    
    @DeleteMapping("reactions/{id}")
    public ResponseEntity<ApiResponse> removeReaction(@PathVariable Long id) {
        List<ReactionDto> reactions = reactionService.removeReaction(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("reactions", reactions))
                        .message("Reaction Removed Successfully")
                        .status(OK)
                        .build()
        );
    }    
}
