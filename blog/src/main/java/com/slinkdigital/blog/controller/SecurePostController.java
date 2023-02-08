package com.slinkdigital.blog.controller;

import com.slinkdigital.blog.dto.ApiResponse;
import com.slinkdigital.blog.dto.PostDto;
import com.slinkdigital.blog.dto.WeddingBlogDto;
import com.slinkdigital.blog.service.PostService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author TEGA
 */
@RestController
@RequestMapping("/api/v1/secure/blog")
@RequiredArgsConstructor
public class SecurePostController {
    
    private final PostService postService;
    
    @PostMapping("/admin/add")
    public ResponseEntity<ApiResponse> createPost(@RequestBody PostDto postDto) {
        postDto = postService.save(postDto);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("post", postDto))
                        .message("Post Added Successfully")
                        .status(OK)
                        .build()
        );
    }
    
    @PostMapping("/wedding/add")
    public ResponseEntity<ApiResponse> addWeddingPost(@RequestBody WeddingBlogDto weddingBlogDto) {
        PostDto postDto = postService.save(weddingBlogDto);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("post", postDto))
                        .message("Post Added Successfully")
                        .status(OK)
                        .build()
        );
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllPosts() {
        List<PostDto> postDtos =postService.getAllPosts();
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("posts", postDtos))
                        .message("Posts Displayed Successfully")
                        .status(OK)
                        .build()
        );
    }
    
    @GetMapping("/all/user/{userId}")
    public ResponseEntity<ApiResponse> getAllPosts(@PathVariable String id) {
        List<PostDto> postDtos = postService.getAllUserPosts(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("posts", postDtos))
                        .message("Post Added Successfully")
                        .status(OK)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getPost(@PathVariable Long id) {
        PostDto postDto = postService.getPost(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("post", postDto))
                        .message("Post Added Successfully")
                        .status(OK)
                        .build()
        );
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse> getPostByCode(String code) {
        List<PostDto> postDtos = postService.getPostByCode(code);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("post", postDtos))
                        .message("Post Displayed Successfully")
                        .status(OK)
                        .build()
        );
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updatePost(@RequestBody PostDto postDto) {
        postDto = postService.update(postDto);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("post", postDto))
                        .message("Post Updated Successfully")
                        .status(OK)
                        .build()
        );
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deletePost(Long id) {
        List<PostDto> postDtos = postService.deletePost(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("post", postDtos))
                        .message("Post Added Successfully")
                        .status(OK)
                        .build()
        );
    }
}
