package com.slinkdigital.wedding.service;

import com.slinkdigital.wedding.dto.CommentDto;
import java.util.List;

/**
 *
 * @author TEGA
 */
public interface CommentService {

    List<CommentDto> getCommentsForWedding(Long id);

    List<CommentDto> addComment(CommentDto commentDto);

    List<CommentDto> removeComment(Long id);

    List<CommentDto> updateComment(CommentDto commentDto);
    
}
