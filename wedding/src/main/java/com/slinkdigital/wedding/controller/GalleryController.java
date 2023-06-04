package com.slinkdigital.wedding.controller;

import com.slinkdigital.wedding.dto.PhotoDto;
import com.slinkdigital.wedding.service.GalleryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author TEGA
 */
@RestController
@RequestMapping("api/ws/v1/weddings/photos")
@RequiredArgsConstructor
public class GalleryController {
    
    private final GalleryService galleryService;
    
    @PostMapping(path = "pre", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public List<PhotoDto> addPreWeddingPhotos(@RequestParam("weddingId") Long id, @RequestParam("gallery") List<MultipartFile> gallery) {
        return galleryService.addPreWeddingPhotos(id,gallery);
    }
    
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public List<PhotoDto> addWeddingPhotos(@RequestParam("weddingId") Long id, @RequestParam("gallery") List<MultipartFile> gallery) {
        return galleryService.addWeddingPhotos(id,gallery);
    }
    
    @PostMapping(path = "post", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public List<PhotoDto> addPostWeddingPhotos(@RequestParam("weddingId") Long id, @RequestParam("gallery") List<MultipartFile> gallery) {
        return galleryService.addPostWeddingPhotos(id,gallery);
    }
    
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removePhoto(@RequestParam Long id) {
        galleryService.removePhoto(id);
    }
}
