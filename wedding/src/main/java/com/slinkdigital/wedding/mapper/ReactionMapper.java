package com.slinkdigital.wedding.mapper;

import com.slinkdigital.wedding.domain.Reaction;
import com.slinkdigital.wedding.dto.ReactionDto;
import java.time.Instant;

/**
 *
 * @author TEGA
 */
public interface ReactionMapper {
    
    ReactionDto mapReactionToDto(Reaction reaction);
    
    String getDuration(Instant instant);
}
