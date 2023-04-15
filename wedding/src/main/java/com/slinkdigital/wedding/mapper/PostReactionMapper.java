package com.slinkdigital.wedding.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.slinkdigital.wedding.domain.PostReaction;
import com.slinkdigital.wedding.dto.PostReactionDto;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
public class PostReactionMapper {
    
    private final PostMapper postMapper;

    public PostReactionDto mapReactionToDto(PostReaction reaction) {
        return PostReactionDto.builder()
                .id(reaction.getId())
                .userId(reaction.getUserId())
                .postDto(postMapper.mapPostToDto(reaction.getPost()))
                .duration(getDuration(reaction.getCreatedAt()))
                .build();
    }

    public String getDuration(Instant instant) {
        return TimeAgo.using(instant.toEpochMilli());
    }
    
}
