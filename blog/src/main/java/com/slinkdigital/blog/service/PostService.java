package com.slinkdigital.blog.service;

import com.slinkdigital.blog.dto.PostDto;
import com.slinkdigital.blog.dto.WeddingBlogDto;
import java.util.List;

/**
 *
 * @author TEGA
 */
public interface PostService {

    PostDto save(PostDto postDto);

    PostDto update(PostDto postDto);

    List<PostDto> getAllPosts();

    List<PostDto> getAllUserPosts(String id);

    PostDto getPost(Long id);

    List<PostDto> getPostByCode(String code);

    List<PostDto> deletePost(Long id);

    public PostDto save(WeddingBlogDto weddingBlogDto);
    
}
