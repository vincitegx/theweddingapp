package com.slinkdigital.wedding.mapper;

import com.slinkdigital.wedding.domain.Comment;
import com.slinkdigital.wedding.dto.CommentDto;
import java.time.Instant;

/**
 *
 * @author TEGA
 */
public interface CommentMapper {
    CommentDto mapCommentToDto(Comment comment);
    String getDuration(Instant instant);
}
