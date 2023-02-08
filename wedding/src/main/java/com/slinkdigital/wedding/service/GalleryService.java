package com.slinkdigital.wedding.service;

import com.slinkdigital.wedding.dto.GalleryDto;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author TEGA
 */
public interface GalleryService {

    GalleryDto addPreWeddingPhotos(Long id, List<MultipartFile> gallery);

    GalleryDto addWeddingPhotos(Long id, List<MultipartFile> gallery);

    GalleryDto addPostWeddingPhotos(Long id, List<MultipartFile> gallery);
    
}
