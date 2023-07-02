package com.slinkdigital.wedding.service;

import com.slinkdigital.wedding.domain.Gallery;
import com.slinkdigital.wedding.domain.Photo;
import com.slinkdigital.wedding.domain.Wedding;
import com.slinkdigital.wedding.dto.PhotoDto;
import com.slinkdigital.wedding.exception.WeddingException;
import com.slinkdigital.wedding.repository.GalleryRepository;
import com.slinkdigital.wedding.repository.PhotoRepository;
import com.slinkdigital.wedding.repository.WeddingRepository;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;
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
    private final HttpServletRequest request;
    private final FileService fileService;
    private static final String WEDDING_NOT_PUBLISHED = "You have to publish this wedding first";
    private static final String AUTHORIZATION_ERROR = "Cannot Identify The User, Therefore operation cannot be performed";
    private static final String WEDDING_NOT_FOUND = "No Such Wedding";
    private static final String PHOTO_UPLOAD_LIMIT_MSG = "Sorry you can only have a maximum of four photos";

    public List<PhotoDto> addPreWeddingPhotos(Long id, List<MultipartFile> gallery) {
        Wedding wedding = weddingRepository.findById(id).orElseThrow(() -> new WeddingException(WEDDING_NOT_FOUND));
        Long loggedInUser = getLoggedInUserId();
        if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
            throw new WeddingException(AUTHORIZATION_ERROR);
        } else {
            Gallery g = galleryRepository.findByWedding(wedding).orElse(new Gallery());
            List<Photo> initialPreWeddingPhotoUrls = g.getPreWeddingPhotos();
            if (initialPreWeddingPhotoUrls.size() > 4 || initialPreWeddingPhotoUrls.size() + gallery.size() > 4) {
                throw new WeddingException(PHOTO_UPLOAD_LIMIT_MSG);
            } else {
                List<String> preWeddingPhotoUrls = fileService.uploadFiles(gallery);
                List<Photo> newPreWeddingPhotos = new ArrayList<>();
                preWeddingPhotoUrls.forEach(preWeddingPhotoUrl -> {
                    Photo p = Photo.builder().name(preWeddingPhotoUrl).build();
                    p = photoRepository.save(p);
                    newPreWeddingPhotos.add(p);
                });
                newPreWeddingPhotos.forEach(photo -> {
                    initialPreWeddingPhotoUrls.add(photo);
                });
                g.setPreWeddingPhotos(initialPreWeddingPhotoUrls);
                galleryRepository.saveAndFlush(g);
                return initialPreWeddingPhotoUrls.stream().map(photo -> {
                    return new PhotoDto(photo.getId(), photo.getName());
                }).collect(Collectors.toList());
            }
        }
    }

    public List<PhotoDto> addWeddingPhotos(Long id, List<MultipartFile> gallery) {
        Wedding wedding = weddingRepository.findById(id).orElseThrow(() -> new WeddingException(WEDDING_NOT_FOUND));
        Long loggedInUser = getLoggedInUserId();
        if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
            throw new WeddingException(AUTHORIZATION_ERROR);
        } else {
            Gallery g = galleryRepository.findByWedding(wedding).orElse(new Gallery());
            List<Photo> initialWeddingPhotoUrls = g.getWeddingPhotos();
            if (initialWeddingPhotoUrls.size() > 4 || initialWeddingPhotoUrls.size() + gallery.size() > 4) {
                throw new WeddingException(PHOTO_UPLOAD_LIMIT_MSG);
            } else {
                List<String> weddingPhotoUrls = fileService.uploadFiles(gallery);
                List<Photo> newWeddingPhotos = new ArrayList<>();
                weddingPhotoUrls.forEach(weddingPhotoUrl -> {
                    Photo p = Photo.builder().name(weddingPhotoUrl).build();
                    p = photoRepository.save(p);
                    newWeddingPhotos.add(p);
                });
                newWeddingPhotos.forEach(photo -> {
                    initialWeddingPhotoUrls.add(photo);
                });
                g.setPreWeddingPhotos(initialWeddingPhotoUrls);
                galleryRepository.saveAndFlush(g);
                List<PhotoDto> photoDtos = initialWeddingPhotoUrls.stream().map(photo -> {
                    return new PhotoDto(photo.getId(), photo.getName());
                }).collect(Collectors.toList());
                return photoDtos;
            }
        }
    }

    public List<PhotoDto> addPostWeddingPhotos(Long id, List<MultipartFile> gallery) {
        Wedding wedding = weddingRepository.findById(id).orElseThrow(() -> new WeddingException(WEDDING_NOT_FOUND));
        Long loggedInUser = getLoggedInUserId();
        if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
            throw new WeddingException(AUTHORIZATION_ERROR);
        } else {
            Gallery g = galleryRepository.findByWedding(wedding).orElse(new Gallery());
            List<Photo> initialPostWeddingPhotoUrls = g.getPostWeddingPhotos();
            if (initialPostWeddingPhotoUrls.size() > 4 || initialPostWeddingPhotoUrls.size() + gallery.size() > 4) {
                throw new WeddingException(PHOTO_UPLOAD_LIMIT_MSG);
            } else {
                List<String> postWeddingPhotoUrls = fileService.uploadFiles(gallery);
                List<Photo> newPostWeddingPhotos = new ArrayList<>();
                postWeddingPhotoUrls.forEach(weddingPhotoUrl -> {
                    Photo p = Photo.builder().name(weddingPhotoUrl).build();
                    p = photoRepository.save(p);
                    newPostWeddingPhotos.add(p);
                });
                newPostWeddingPhotos.forEach(photo -> {
                    initialPostWeddingPhotoUrls.add(photo);
                });
                g.setPreWeddingPhotos(initialPostWeddingPhotoUrls);
                galleryRepository.saveAndFlush(g);
                List<PhotoDto> photoDtos = initialPostWeddingPhotoUrls.stream().map(photo -> {
                    return new PhotoDto(photo.getId(), photo.getName());
                }).collect(Collectors.toList());
                return photoDtos;
            }
        }
    }

    public void removePhoto(Long id) {
        Photo p = photoRepository.findById(id).orElseThrow(()->new WeddingException("No such photo associated with id"));
        photoRepository.delete(p);
    }

    private Long getLoggedInUserId() {
        return Long.parseLong(request.getHeader("x-id"));
    }
}
