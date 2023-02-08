package com.slinkdigital.wedding.service;

import com.slinkdigital.wedding.dto.PostCommentDto;
import java.util.List;
import java.util.Map;

/**
 *
 * @author TEGA
 */
public interface PostCommentService {

    public Map<String, String> removePostComment(Long id);

    public PostCommentDto editPostComment(PostCommentDto postCommentDto);

    public PostCommentDto addPostComment(PostCommentDto postCommentDto);

    public PostCommentDto getPostComment(Long id);

    public List<PostCommentDto> getPostCommentsForPost(Long id);
    
}
