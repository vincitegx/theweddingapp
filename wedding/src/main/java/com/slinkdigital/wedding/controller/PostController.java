package com.slinkdigital.wedding.controller;

import com.slinkdigital.wedding.dto.ApiResponse;
import com.slinkdigital.wedding.dto.PostDto;
import com.slinkdigital.wedding.mapper.PostMapper;
import com.slinkdigital.wedding.service.PostService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<ApiResponse> getWeddingPosts(@PathVariable(value = "weddingId") Long id) {
        List<PostDto> posts = postService.getAllPostForWedding(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("posts", posts))
                        .message("List Of Posts Successful")
                        .status(OK)
                        .build()
        );
    }
    
    @GetMapping("posts/{id}")
    public ResponseEntity<ApiResponse> getPost(@PathVariable Long id) {
        PostDto post = postService.getPost(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("post", post))
                        .message("Post Successful")
                        .status(OK)
                        .build()
        );
    }

    @PostMapping(path = "posts", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse> addPost(@RequestParam("postDto") String postDto,
            @RequestParam("file") MultipartFile file) {
        PostDto post = postMapper.getJson(postDto);
        post = postService.addPost(post, file);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("post", post))
                        .message("Post Added Successfully")
                        .status(OK)
                        .build()
        );
    }

    @PutMapping("posts")
    public ResponseEntity<ApiResponse> editPost(@RequestBody PostDto post) {
        post = postService.editPost(post);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("post", post))
                        .message("Post Updated Successfully")
                        .status(OK)
                        .build()
        );
    }
    
    @DeleteMapping("posts/{id}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Long id) {
        Map<String, String> result = postService.removePost(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("isDeleted", true))
                        .message(result.get("success"))
                        .status(OK)
                        .build()
        );
    }    
    
}
