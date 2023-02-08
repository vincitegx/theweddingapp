package com.slinkdigital.wedding.service;

import com.slinkdigital.wedding.domain.Wedding;
import com.slinkdigital.wedding.dto.PostDto;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author TEGA
 */
public interface PostService {
    
    PostDto addPost(PostDto postDto, MultipartFile file);
    
    List<PostDto> getAllPostForWedding(Long id);
    
    PostDto convertWeddingToPost(Wedding wedding);
    
    PostDto editPost(PostDto postDto);
    
    Map<String, String> removePost(Long id);

    public PostDto getPost(Long id);
}
