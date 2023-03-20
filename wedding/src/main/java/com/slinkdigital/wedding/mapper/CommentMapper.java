package com.slinkdigital.wedding.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.slinkdigital.wedding.domain.Comment;
import com.slinkdigital.wedding.dto.CommentDto;
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
public class CommentMapper {

    private final WeddingMapper weddingMapper;

    public CommentDto mapCommentToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .message(comment.getMessage())
                .userId(comment.getUserId())
                .wedding(weddingMapper.mapWeddingToDto(comment.getWedding()))
                .duration(getDuration(comment.getCreatedAt()))
                .build();
    }

    public String getDuration(Instant instant) {
        return TimeAgo.using(instant.toEpochMilli());
    }

}
