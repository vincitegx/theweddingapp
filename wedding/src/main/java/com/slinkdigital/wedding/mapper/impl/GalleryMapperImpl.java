package com.slinkdigital.wedding.mapper.impl;

import com.slinkdigital.wedding.domain.Gallery;
import com.slinkdigital.wedding.dto.GalleryDto;
import com.slinkdigital.wedding.mapper.GalleryMapper;
import com.slinkdigital.wedding.mapper.WeddingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
public class GalleryMapperImpl implements GalleryMapper{

    private final WeddingMapper weddingMapper;
    
    @Override
    public GalleryDto mapGalleryToGalleryDto(Gallery gallery) {
        return GalleryDto.builder()
                .id(gallery.getId())
                .preWeddingPhotos(gallery.getPreWeddingPhotos())
                .wedding(weddingMapper.mapWeddingToDto(gallery.getWedding()))
                .weddingPhotos(gallery.getWeddingPhotos())
                .postWeddingPhotos(gallery.getPostWeddingPhotos())
                .build();
    }
    
}
