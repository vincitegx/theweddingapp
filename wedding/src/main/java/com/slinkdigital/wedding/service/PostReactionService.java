package com.slinkdigital.wedding.service;

import com.slinkdigital.wedding.domain.Post;
import com.slinkdigital.wedding.domain.PostReaction;
import com.slinkdigital.wedding.dto.PostReactionDto;
import com.slinkdigital.wedding.exception.WeddingException;
import com.slinkdigital.wedding.mapper.PostReactionMapper;
import com.slinkdigital.wedding.repository.PostReactionRepository;
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
@Transactional
@RequiredArgsConstructor
public class PostReactionService {

    private final PostReactionRepository postReactionRepository;
    private final PostReactionMapper postReactionMapper;
    private final PostRepository postRepository;

    public void removePostReaction(Long id) {
        PostReaction postReaction = postReactionRepository.findById(id).orElseThrow(() -> new WeddingException("No such reaction"));
        postReactionRepository.delete(postReaction);
    }

    public PostReactionDto editPostReaction(PostReactionDto postReactionDto) {
        PostReaction postReaction = postReactionRepository.findById(postReactionDto.getId()).orElseThrow(() -> new WeddingException("No such reaction"));
        postReaction.setType(postReactionDto.getType());
        postReaction = postReactionRepository.saveAndFlush(postReaction);
        return postReactionMapper.mapReactionToDto(postReaction);
    }

    public PostReactionDto addPostReaction(PostReactionDto postReactionDto) {
        PostReaction postReaction = PostReaction.builder()
                .createdAt(Instant.now())
                .build();
        postReaction = postReactionRepository.save(postReaction);
        return postReactionMapper.mapReactionToDto(postReaction);
    }

    public PostReactionDto getPostReaction(Long id) {
        PostReaction postReaction = postReactionRepository.findById(id).orElseThrow(() -> new WeddingException("No such reaction"));
        return postReactionMapper.mapReactionToDto(postReaction);
    }

    public List<PostReactionDto> getReactionsForPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new WeddingException("No post associated with this id"));
        List<PostReaction> postReactions = postReactionRepository.findByPost(post);
        List<PostReactionDto> postReactionDtos = new ArrayList<>();
        postReactions.forEach(pr -> {
            postReactionDtos.add(postReactionMapper.mapReactionToDto(pr));
        });
        return postReactionDtos;
    }
}
