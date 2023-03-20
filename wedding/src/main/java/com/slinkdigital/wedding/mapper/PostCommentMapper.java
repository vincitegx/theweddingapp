package com.slinkdigital.wedding.mapper;

import com.slinkdigital.wedding.domain.PostComment;
import com.slinkdigital.wedding.dto.PostCommentDto;
import com.slinkdigital.wedding.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
public class PostCommentMapper {

    private final PostMapper postMapper;

    public PostComment mapDtoToPostComment(PostCommentDto postCommentDto) {
        return PostComment.builder()
                .id(postCommentDto.getId())
                .message(postCommentDto.getMessage())
                .post(postMapper.mapDtoToPost(postCommentDto.getPost()))
                .createdAt(postCommentDto.getCreatedAt())
                .build();
    }

    public PostCommentDto mapPostCommentToDto(PostComment postComment) {
        return PostCommentDto.builder()
                .id(postComment.getId())
                .message(postComment.getMessage())
                .createdAt(postComment.getCreatedAt())
                .post(postMapper.mapPostToDto(postComment.getPost()))
                .build();
    }

}
