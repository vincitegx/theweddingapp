package com.slinkdigital.wedding.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.slinkdigital.wedding.domain.Post;
import com.slinkdigital.wedding.domain.Wedding;
import com.slinkdigital.wedding.dto.PostDto;
import com.slinkdigital.wedding.exception.WeddingException;
import com.slinkdigital.wedding.mapper.PostMapper;
import com.slinkdigital.wedding.repository.PostRepository;
import com.slinkdigital.wedding.repository.WeddingRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author TEGA
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final HttpServletRequest request;
    private final WeddingRepository weddingRepository;
    private final FileService fileService;
    private final KafkaTemplate<String, PostDto> kafkaTemplate;
    
    
    public PostDto addPost(PostDto postDto, MultipartFile file) {
        try {
            Wedding wedding = weddingRepository.findById(postDto.getWedding().getId()).orElseThrow(() -> new WeddingException("No Such Wedding"));
            Long loggedInUser = getLoggedInUserId();
            if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
                throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
            } else if (!wedding.isPublished()) {
                throw new WeddingException("You have to publish this wedding first");
            } else {
                String fileUrl = fileService.uploadFile(file);
                postDto.setCreatedAt(Instant.now());
                postDto.setFileUrl(fileUrl);
                Post post = postMapper.mapDtoToPost(postDto);
                post = postRepository.save(post);
                postDto = postMapper.mapPostToDto(post);
                kafkaTemplate.send("feed_topic", postDto);
                return postDto;
            }
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    public List<PostDto> getAllPostForWedding(Long id) {
        try {
            Wedding wedding = weddingRepository.findById(id).orElseThrow(() -> new WeddingException("No Such Wedding"));
            List<Post> posts = postRepository.findByWedding(wedding);
            List<PostDto> postDto = new ArrayList<>(posts.size());
            posts.forEach(p -> {
                postDto.add(postMapper.mapPostToDto(p));
            });
            return postDto;
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    public PostDto getPost(Long id) {
        try {
            Post post = postRepository.findById(id).orElseThrow(() -> new WeddingException("No Such Post Associated To This Id"));
            PostDto postDto = postMapper.mapPostToDto(post);
            return postDto;
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    public PostDto convertWeddingToPost(Wedding wedding) throws JsonProcessingException {
        try {
            wedding = weddingRepository.findById(wedding.getId()).orElseThrow(() -> new WeddingException("No Such Wedding"));
            Long loggedInUser = getLoggedInUserId();
            if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
                throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
            } else if (!wedding.isPublished()) {
                throw new WeddingException("You have to publish this wedding first");
            } else {
                PostDto postDto = PostDto.builder().build();
                postDto.setCreatedAt(Instant.now());
                String fileUrl = "default-wedding.jpg";
                if (wedding.getCoverFileUrl() != null) {
                    fileUrl = wedding.getCoverFileUrl();
                }
                postDto.setFileUrl(fileUrl);
                Post post = postMapper.mapDtoToPost(postDto);
                post = postRepository.save(post);
                // Send asynchronous notification with the PostDto details to the Feed microservice using Kafka
//            ObjectMapper objectMapper = new ObjectMapper();
//            String postJson = objectMapper.writeValueAsString(postDto);
                postDto = postMapper.mapPostToDto(post);
                kafkaTemplate.send("feed_topic", postDto);
                return postDto;
            }
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    public PostDto editPost(PostDto postDto) {
        try {
            Post post = postRepository.findById(postDto.getId()).orElseThrow(() -> new WeddingException("No post associated with this Id"));
            Wedding wedding = post.getWedding();
            Long loggedInUser = getLoggedInUserId();
            if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
                throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
            } else {
                post.setCaption(postDto.getCaption());
                post = postRepository.saveAndFlush(post);
                return postMapper.mapPostToDto(post);
            }
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    public Map<String, String> removePost(Long id) {
        try {
            Map<String, String> result = new HashMap<>();
            Post post = postRepository.findById(id).orElseThrow(() -> new WeddingException("No post associated with this Id"));
            postRepository.delete(post);
            result.put("success", "Post Deleted Successfully");
            return result;
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    private Long getLoggedInUserId() {
        try {
            return Long.parseLong(request.getHeader("x-id"));
        } catch (WeddingException ex) {
            throw new WeddingException("You Need To Be Logged In !!!");
        }
    }
}
