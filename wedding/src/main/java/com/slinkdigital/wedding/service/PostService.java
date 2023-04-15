package com.slinkdigital.wedding.service;

import com.slinkdigital.wedding.domain.Post;
import com.slinkdigital.wedding.domain.Wedding;
import com.slinkdigital.wedding.dto.PostDto;
import com.slinkdigital.wedding.exception.WeddingException;
import com.slinkdigital.wedding.mapper.PostMapper;
import com.slinkdigital.wedding.mapper.WeddingMapper;
import com.slinkdigital.wedding.repository.PostRepository;
import com.slinkdigital.wedding.repository.WeddingRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private final WeddingMapper weddingMapper;
    private final HttpServletRequest request;
    private final WeddingRepository weddingRepository;
    private final FileService fileService;
    private final KafkaTemplate<String, PostDto> kafkaTemplate;

    public PostDto addPost(PostDto postDto, MultipartFile file) {
        Wedding wedding = weddingRepository.findById(postDto.getWedding().getId()).orElseThrow(() -> new WeddingException("No Such Wedding"));
        Long loggedInUser = getLoggedInUserId();
        if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
            throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
        } else if (!wedding.getIsPublished()) {
            throw new WeddingException("You have to publish this wedding first");
        } else {
            String fileUrl;
            try {
                fileUrl = fileService.uploadFile(file);
                postDto.setFileUrl(fileUrl);
            } catch (IOException ex) {
                Logger.getLogger(PostService.class.getName()).log(Level.SEVERE, null, ex);
            }

            Post post = postMapper.mapDtoToPost(postDto);
            post.setCreatedAt(Instant.now());
            post = postRepository.save(post);
            postDto = postMapper.mapPostToDto(post);
            kafkaTemplate.send("feed_topic", postDto);
            return postDto;
        }
    }

    public List<PostDto> getAllPostForWedding(Long id) {
        Wedding wedding = weddingRepository.findById(id).orElseThrow(() -> new WeddingException("No Such Wedding"));
        List<Post> posts = postRepository.findByWedding(wedding);
        List<PostDto> postDto = new ArrayList<>(posts.size());
        posts.forEach(p -> {
            postDto.add(postMapper.mapPostToDto(p));
        });
        return postDto;
    }

    public PostDto getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new WeddingException("No Such Post Associated To This Id"));
        PostDto postDto = postMapper.mapPostToDto(post);
        return postDto;
    }

    public PostDto convertWeddingToPost(Wedding wedding) {
        PostDto postDto = new PostDto();
        String fileUrl = "default-wedding.jpg";
        if (wedding.getCoverFileUrl() != null) {
            fileUrl = wedding.getCoverFileUrl();
        }
        postDto.setFileUrl(fileUrl);
        postDto.setCaption(wedding.getTitle());
        postDto.setWedding(weddingMapper.mapWeddingToDto(wedding));
        Post post = postMapper.mapDtoToPost(postDto);
        post.setCreatedAt(Instant.now());
        post = postRepository.save(post);
        // Send asynchronous notification with the PostDto details to the Feed microservice using Kafka
        postDto = postMapper.mapPostToDto(post);
        kafkaTemplate.send("feed_topic", postDto);
        return postDto;
    }

    public PostDto editPost(PostDto postDto) {
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
    }

    public void removePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new WeddingException("No post associated with this Id"));
        postRepository.delete(post);
    }

    private Long getLoggedInUserId() {
        return Long.parseLong(request.getHeader("x-id"));
    }
}
