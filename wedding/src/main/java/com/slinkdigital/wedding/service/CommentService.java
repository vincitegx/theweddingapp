package com.slinkdigital.wedding.service;

import com.slinkdigital.wedding.domain.Comment;
import com.slinkdigital.wedding.domain.Wedding;
import com.slinkdigital.wedding.dto.CommentDto;
import com.slinkdigital.wedding.exception.WeddingException;
import com.slinkdigital.wedding.mapper.CommentMapper;
import com.slinkdigital.wedding.mapper.WeddingMapper;
import com.slinkdigital.wedding.repository.CommentRepository;
import com.slinkdigital.wedding.repository.WeddingRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final WeddingRepository weddingRepository;
    private final WeddingMapper weddingMapper;
    private final CommentMapper commentMapper;

    public List<CommentDto> getCommentsForWedding(Long id) {
        try {
            Wedding wedding = weddingRepository.findById(id).orElseThrow(() -> new WeddingException("No wedding associated with this id"));
            List<Comment> comments = commentRepository.findByWedding(wedding);
            List<CommentDto> commentDtos = new ArrayList<>();
            comments.forEach(comment -> {
                commentDtos.add(commentMapper.mapCommentToDto(comment));
            });
            return commentDtos;
        } catch (RuntimeException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    public CommentDto addComment(CommentDto commentDto) {
        try {
            Comment comment = Comment.builder()
                    .message(commentDto.getMessage())
                    .userId(commentDto.getUserId())
                    .createdAt(Instant.now())
                    .wedding(weddingMapper.mapWeddingDtoToWedding(commentDto.getWedding()))
                    .build();
            comment = commentRepository.save(comment);
            return commentMapper.mapCommentToDto(comment);
        } catch (RuntimeException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    public void removeComment(Long id) {
        try {
            Comment comment = commentRepository.findById(id).orElseThrow(() -> new WeddingException("No such comment"));
            commentRepository.delete(comment);
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    public CommentDto updateComment(CommentDto commentDto) {
        try {
            Comment comment = commentRepository.findById(commentDto.getId()).orElseThrow(() -> new WeddingException("No such comment"));
            comment.setMessage(commentDto.getMessage());
            comment = commentRepository.saveAndFlush(comment);
            return commentMapper.mapCommentToDto(comment);
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }
}
