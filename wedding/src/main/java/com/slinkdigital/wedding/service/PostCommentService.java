package com.slinkdigital.wedding.service;

import com.slinkdigital.wedding.domain.Post;
import com.slinkdigital.wedding.domain.PostComment;
import com.slinkdigital.wedding.dto.PostCommentDto;
import com.slinkdigital.wedding.exception.WeddingException;
import com.slinkdigital.wedding.mapper.PostCommentMapper;
import com.slinkdigital.wedding.repository.PostCommentRepository;
import com.slinkdigital.wedding.repository.PostRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final PostCommentMapper postCommentMapper;
    private final PostRepository postRepository;

    public void removePostComment(Long id) {
        PostComment postComment = postCommentRepository.findById(id).orElseThrow(() -> new WeddingException("No such comment"));
        postCommentRepository.delete(postComment);
    }

    public PostCommentDto editPostComment(PostCommentDto postCommentDto) {
        PostComment postComment = postCommentRepository.findById(postCommentDto.getId()).orElseThrow(() -> new WeddingException("No such comment"));
        postComment.setMessage(postComment.getMessage());
        postComment = postCommentRepository.saveAndFlush(postComment);
        return postCommentMapper.mapPostCommentToDto(postComment);
    }

    public PostCommentDto addPostComment(PostCommentDto postCommentDto) {
        postCommentDto.setCreatedAt(Instant.now());
        PostComment postComment = postCommentMapper.mapDtoToPostComment(postCommentDto);
        postComment = postCommentRepository.save(postComment);
        return postCommentMapper.mapPostCommentToDto(postComment);
    }

    public PostCommentDto getPostComment(Long id) {
        PostComment postComment = postCommentRepository.findById(id).orElseThrow(() -> new WeddingException("No post associated with this id"));
        return postCommentMapper.mapPostCommentToDto(postComment);
    }

    public List<PostCommentDto> getPostCommentsForPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new WeddingException("No post associated with this id"));
        List<PostComment> postComments = postCommentRepository.findByPost(post);
        List<PostCommentDto> postCommentDtos = new ArrayList<>();
        postComments.forEach(pc -> {
            postCommentDtos.add(postCommentMapper.mapPostCommentToDto(pc));
        });
        return postCommentDtos;
    }

}
