package com.slinkdigital.wedding.mapper.impl;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.slinkdigital.wedding.domain.Reaction;
import com.slinkdigital.wedding.dto.ReactionDto;
import com.slinkdigital.wedding.mapper.ReactionMapper;
import com.slinkdigital.wedding.mapper.WeddingMapper;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
public class ReactionMapperImpl implements ReactionMapper{
    
    private final WeddingMapper weddingMapper;

    @Override
    public ReactionDto mapReactionToDto(Reaction reaction) {
        return ReactionDto.builder()
                .id(reaction.getId())
                .userId(reaction.getUserId())
                .wedding(weddingMapper.mapWeddingToDto(reaction.getWedding()))
                .duration(getDuration(reaction.getCreatedAt()))
                .build();
    }

    @Override
    public String getDuration(Instant instant) {
        return TimeAgo.using(instant.toEpochMilli());
    }
}
