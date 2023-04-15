package com.slinkdigital.wedding.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.slinkdigital.wedding.domain.Post;
import com.slinkdigital.wedding.domain.Wedding;
import com.slinkdigital.wedding.dto.Author;
import com.slinkdigital.wedding.dto.PostDto;
import com.slinkdigital.wedding.exception.WeddingException;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@AllArgsConstructor
public class PostMapper {

    private final WeddingMapper weddingMapper;

    public Post mapDtoToPost(PostDto postDto) {
        return Post.builder()
                .caption(postDto.getCaption())
                .fileUrl(postDto.getFileUrl())
                .id(postDto.getId())
                .wedding(weddingMapper.mapWeddingDtoToWedding(postDto.getWedding()))
                .build();
    }

    public PostDto mapPostToDto(Post post) {
        return PostDto.builder()
                .caption(post.getCaption())
                .fileUrl(post.getFileUrl())
                .id(post.getId())
                .wedding(weddingMapper.mapWeddingToDto(post.getWedding()))
                .duration(getDuration(post.getCreatedAt()))
                .author(new Author(post.getWedding().getId(),post.getWedding().getTitle(),post.getWedding().getCoverFileUrl()))
                .build();
    }

    public String getDuration(Instant instant) {
        return TimeAgo.using(instant.toEpochMilli());
    }

    public PostDto mapWeddingToPostDto(Wedding wedding) {
        return PostDto.builder()
                .caption(wedding.getTitle())
                .fileUrl(wedding.getCoverFileUrl())
                .wedding(weddingMapper.mapWeddingToDto(wedding))
                .duration(getDuration(wedding.getCreatedAt().toInstant(ZoneOffset.UTC)))
                .author(new Author(wedding.getId(),wedding.getTitle(),wedding.getCoverFileUrl()))
                .build();
    }

    public PostDto getJson(String postDto) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            PostDto postJson = objectMapper.readValue(postDto, PostDto.class);
            return postJson;
        } catch (IOException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

}
