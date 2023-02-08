package com.slinkdigital.wedding.service.impl;

import com.slinkdigital.wedding.domain.Post;
import com.slinkdigital.wedding.domain.PostComment;
import com.slinkdigital.wedding.dto.PostCommentDto;
import com.slinkdigital.wedding.exception.WeddingException;
import com.slinkdigital.wedding.mapper.PostCommentMapper;
import com.slinkdigital.wedding.repository.PostCommentRepository;
import com.slinkdigital.wedding.repository.PostRepository;
import com.slinkdigital.wedding.service.PostCommentService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class PostCommentServiceImpl implements PostCommentService{

    private final PostCommentRepository postCommentRepository;
    private final PostCommentMapper postCommentMapper;
    private final PostRepository postRepository;
    
    @Override
    public Map<String, String> removePostComment(Long id) {
        try{
            PostComment postComment  = postCommentRepository.findById(id).orElseThrow(()-> new WeddingException("No such comment"));
           postCommentRepository.delete(postComment);
           Map<String, String> result = new HashMap<>();
           result.put("success", "Post Comment Deleted Successfully");
           return result;
        }catch(WeddingException ex){
            throw new WeddingException(ex.getMessage());
        }
    }

    @Override
    public PostCommentDto editPostComment(PostCommentDto postCommentDto) {
        try{
           PostComment postComment = postCommentRepository.findById(postCommentDto.getId()).orElseThrow(()-> new WeddingException("No such comment"));
           postComment.setMessage(postComment.getMessage());
           postComment = postCommentRepository.saveAndFlush(postComment);
           return postCommentMapper.mapPostCommentToDto(postComment);
        }catch(WeddingException ex){
            throw new WeddingException(ex.getMessage());
        }
    }

    @Override
    public PostCommentDto addPostComment(PostCommentDto postCommentDto) {
        try{
            postCommentDto.setCreatedAt(Instant.now());
            PostComment postComment = postCommentMapper.mapDtoToPostComment(postCommentDto);
           postComment = postCommentRepository.save(postComment);
           return postCommentMapper.mapPostCommentToDto(postComment);
        }catch(RuntimeException ex){
            throw new WeddingException(ex.getMessage());
        }
    }

    @Override
    public PostCommentDto getPostComment(Long id) {
        try{
           PostComment postComment = postCommentRepository.findById(id).orElseThrow(()-> new WeddingException("No post associated with this id"));
           return postCommentMapper.mapPostCommentToDto(postComment);
        }catch(RuntimeException ex){
            throw new WeddingException(ex.getMessage());
        }
    }

    @Override
    public List<PostCommentDto> getPostCommentsForPost(Long id) {
        try{
           Post post = postRepository.findById(id).orElseThrow(()-> new WeddingException("No post associated with this id"));
           List<PostComment> postComments = postCommentRepository.findByPost(post);
           List<PostCommentDto> postCommentDtos = new ArrayList<>();
           postComments.forEach(pc->{
               postCommentDtos.add(postCommentMapper.mapPostCommentToDto(pc));
           });
           return postCommentDtos;
        }catch(RuntimeException ex){
            throw new WeddingException(ex.getMessage());
        }
    }
    
}
