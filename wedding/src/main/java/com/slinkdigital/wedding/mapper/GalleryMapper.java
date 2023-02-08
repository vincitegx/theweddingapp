package com.slinkdigital.wedding.mapper;

import com.slinkdigital.wedding.domain.Gallery;
import com.slinkdigital.wedding.dto.GalleryDto;

/**
 *
 * @author TEGA
 */
public interface GalleryMapper {
    
    GalleryDto mapGalleryToGalleryDto(Gallery gallery);
    
}
