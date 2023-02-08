package com.slinkdigital.wedding.controller;

import com.slinkdigital.wedding.dto.ApiResponse;
import com.slinkdigital.wedding.dto.GalleryDto;
import com.slinkdigital.wedding.service.GalleryService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author TEGA
 */
@RestController
@RequestMapping("api/ws/v1/weddings")
@RequiredArgsConstructor
@Slf4j
public class GalleryController {
    
    private final GalleryService galleryService;
    
    @PostMapping(path = "pre-wedding-photos", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse> addPreWeddingPhotos(@RequestParam("weddingId") Long id, @RequestParam("gallery") List<MultipartFile> gallery) {
        GalleryDto galleryDto  = galleryService.addPreWeddingPhotos(id,gallery);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("gallery", galleryDto))
                        .message("Pre Wedding Photos Added Successfully")
                        .status(OK)
                        .build()
        );
    }
    
    @PostMapping(path = "wedding-photos", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse> addWeddingPhotos(@RequestParam("weddingId") Long id, @RequestParam("gallery") List<MultipartFile> gallery) {
        GalleryDto galleryDto  = galleryService.addWeddingPhotos(id,gallery);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("gallery", galleryDto))
                        .message("Wedding Photos Added Successfully")
                        .status(OK)
                        .build()
        );
    }
    
    @PostMapping(path = "post-wedding-photos", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse> addPostWeddingPhotos(@RequestParam("weddingId") Long id, @RequestParam("gallery") List<MultipartFile> gallery) {
        GalleryDto galleryDto  = galleryService.addPostWeddingPhotos(id,gallery);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("gallery", galleryDto))
                        .message("Post Wedding Photos Added Successfully")
                        .status(OK)
                        .build()
        );
    }
    
}
