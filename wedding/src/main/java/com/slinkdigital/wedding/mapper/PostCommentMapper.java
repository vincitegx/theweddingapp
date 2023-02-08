package com.slinkdigital.wedding.mapper;

import com.slinkdigital.wedding.domain.PostComment;
import com.slinkdigital.wedding.dto.PostCommentDto;

/**
 *
 * @author TEGA
 */
public interface PostCommentMapper {
    
    PostComment mapDtoToPostComment(PostCommentDto postCommentDto);
    
    PostCommentDto mapPostCommentToDto(PostComment postComment);
    
}
