package com.slinkdigital.wedding.service;

import com.slinkdigital.wedding.domain.Gallery;
import com.slinkdigital.wedding.domain.Photo;
import com.slinkdigital.wedding.domain.Wedding;
import com.slinkdigital.wedding.dto.GalleryDto;
import com.slinkdigital.wedding.dto.PhotoDto;
import com.slinkdigital.wedding.exception.WeddingException;
import com.slinkdigital.wedding.mapper.GalleryMapper;
import com.slinkdigital.wedding.repository.GalleryRepository;
import com.slinkdigital.wedding.repository.PhotoRepository;
import com.slinkdigital.wedding.repository.WeddingRepository;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
public class GalleryService {

    private final GalleryRepository galleryRepository;
    private final PhotoRepository photoRepository;
    private final WeddingRepository weddingRepository;
    private final GalleryMapper galleryMapper;
    private final HttpServletRequest request;
    private final FileService fileService;

    public List<Photo> addPreWeddingPhotos(Long id, List<MultipartFile> gallery) {
        Wedding wedding = weddingRepository.findById(id).orElseThrow(() -> new WeddingException("No Such Wedding"));
        Long loggedInUser = getLoggedInUserId();
        if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
            throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
        } else {
            List<String> preWeddingPhotoUrls = fileService.uploadFiles(gallery);
            List<Photo> newPreWeddingPhotos = new ArrayList<>();
            preWeddingPhotoUrls.forEach(preWeddingPhotoUrl -> {
                Photo p = Photo.builder().name(preWeddingPhotoUrl).build();
                p = photoRepository.save(p);
                newPreWeddingPhotos.add(p);
            });
            Gallery g = galleryRepository.findByWedding(wedding).orElse(
                    Gallery.builder()
                            .preWeddingPhotos(newPreWeddingPhotos)
                            .wedding(wedding)
                            .build()
            );
            List<Photo> initialPreWeddingPhotoUrls = g.getPreWeddingPhotos();
            if (initialPreWeddingPhotoUrls.size() >= 4 || initialPreWeddingPhotoUrls.size() + newPreWeddingPhotos.size() >= 4) {
                throw new WeddingException("Sorry, You can only have a maximum of four photos");
            } else {
                newPreWeddingPhotos.forEach(photo -> {
                    initialPreWeddingPhotoUrls.add(photo);
                });
                g.setPreWeddingPhotos(initialPreWeddingPhotoUrls);
            }
            galleryRepository.saveAndFlush(g);
            return initialPreWeddingPhotoUrls;
        }
        
    }

    public GalleryDto addWeddingPhotos(Long id, List<MultipartFile> gallery) {
        Wedding wedding = weddingRepository.findById(id).orElseThrow(() -> new WeddingException("No Such Wedding"));
        Long loggedInUser = getLoggedInUserId();
        if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
            throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
        } else {
            List<String> galleryUrls = fileService.uploadFiles(gallery);
            List<Photo> weddingPhotos = new ArrayList<>();
            galleryUrls.forEach(galleryUrl -> {
                Photo p = Photo.builder().name(galleryUrl).build();
                p = photoRepository.save(p);
                weddingPhotos.add(p);
            });
            Gallery g;
            if (galleryRepository.findByWedding(wedding).isEmpty()) {
                g = Gallery.builder()
                        .weddingPhotos(weddingPhotos)
                        .wedding(wedding)
                        .build();
            } else {
                g = galleryRepository.findByWedding(wedding).get();
                g.setWeddingPhotos(weddingPhotos);
            }
            g = galleryRepository.save(g);
            return galleryMapper.mapGalleryToGalleryDto(g);
        }
    }

    public GalleryDto addPostWeddingPhotos(Long id, List<MultipartFile> gallery) {
        Wedding wedding = weddingRepository.findById(id).orElseThrow(() -> new WeddingException("No Such Wedding"));
        Long loggedInUser = getLoggedInUserId();
        if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
            throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
        } else {
            List<String> galleryUrls = fileService.uploadFiles(gallery);
            List<Photo> postWeddingPhotos = new ArrayList<>();
            galleryUrls.forEach(galleryUrl -> {
                Photo p = Photo.builder().name(galleryUrl).build();
                p = photoRepository.save(p);
                postWeddingPhotos.add(p);
            });
            Gallery g;
            if (galleryRepository.findByWedding(wedding).isEmpty()) {
                g = Gallery.builder()
                        .postWeddingPhotos(postWeddingPhotos)
                        .wedding(wedding)
                        .build();
            } else {
                g = galleryRepository.findByWedding(wedding).get();
                g.setPostWeddingPhotos(postWeddingPhotos);
            }
            g = galleryRepository.save(g);
            return galleryMapper.mapGalleryToGalleryDto(g);
        }
    }

    private Long getLoggedInUserId() {
        return Long.parseLong(request.getHeader("x-id"));
    }

}
