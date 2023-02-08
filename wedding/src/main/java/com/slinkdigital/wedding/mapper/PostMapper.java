package com.slinkdigital.wedding.mapper;

import com.slinkdigital.wedding.domain.Post;
import com.slinkdigital.wedding.domain.Wedding;
import com.slinkdigital.wedding.dto.PostDto;
import java.time.Instant;

/**
 *
 * @author TEGA
 */
public interface PostMapper {
    
    Post mapDtoToPost(PostDto postDto);
    
    PostDto mapPostToDto(Post post);
    
    String getDuration(Instant instant);
    
    PostDto mapWeddingToPostDto(Wedding wedding);

    public PostDto getJson(String postDto);
}
