package com.slinkdigital.wedding.service;

import java.util.List;

/**
 *
 * @author TEGA
 */
public interface BlogService {

    List<String> getBlogCodesByWeddingId(Long id);

    List<String> addBlogCodeForWedding(Long id, String code);
    
}
