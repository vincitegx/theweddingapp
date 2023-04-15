package com.slinkdigital.wedding.controller;

import com.slinkdigital.wedding.dto.PostDto;
import com.slinkdigital.wedding.mapper.PostMapper;
import com.slinkdigital.wedding.service.PostService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author TEGA
 */
@RestController
@RequestMapping("api/ws/v1/weddings")
@RequiredArgsConstructor
public class PostController {
    
    private final PostService postService;
    private final PostMapper postMapper;
    
    @GetMapping("{weddingId}/posts")
    @ResponseStatus(HttpStatus.OK)
    public List<PostDto> getWeddingPosts(@PathVariable(value = "weddingId") Long id) {
        return postService.getAllPostForWedding(id);
    }
    
    @GetMapping("posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostDto getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    @PostMapping(path = "posts", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto addPost(@RequestParam("postDto") String postDto,
            @RequestParam("file") MultipartFile file) {
        PostDto post = postMapper.getJson(postDto);
        return postService.addPost(post, file);
    }

    @PutMapping("posts")
    @ResponseStatus(HttpStatus.OK)
    public PostDto editPost(@RequestBody PostDto post) {
        return postService.editPost(post);
    }
    
    @DeleteMapping("posts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long id) {
        postService.removePost(id);
    }        
}
