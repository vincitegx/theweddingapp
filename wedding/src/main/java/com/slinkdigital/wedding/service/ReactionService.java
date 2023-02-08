package com.slinkdigital.wedding.service;

import com.slinkdigital.wedding.dto.ReactionDto;
import java.util.List;

/**
 *
 * @author TEGA
 */
public interface ReactionService {

    List<ReactionDto> getReactionsForWedding(Long id);

    List<ReactionDto> addReaction(ReactionDto reactionDto);

    List<ReactionDto> removeReaction(Long id);

    List<ReactionDto> updateReaction(ReactionDto reactionDto);
    
}
