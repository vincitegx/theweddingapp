package com.slinkdigital.wedding.service.impl;

import com.slinkdigital.wedding.domain.Gallery;
import com.slinkdigital.wedding.domain.Photo;
import com.slinkdigital.wedding.domain.Wedding;
import com.slinkdigital.wedding.dto.GalleryDto;
import com.slinkdigital.wedding.exception.WeddingException;
import com.slinkdigital.wedding.mapper.GalleryMapper;
import com.slinkdigital.wedding.repository.GalleryRepository;
import com.slinkdigital.wedding.repository.PhotoRepository;
import com.slinkdigital.wedding.repository.WeddingRepository;
import com.slinkdigital.wedding.service.FileService;
import com.slinkdigital.wedding.service.GalleryService;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
public class GalleryServiceImpl implements GalleryService {

    private final GalleryRepository galleryRepository;
    private final PhotoRepository photoRepository;
    private final WeddingRepository weddingRepository;
    private final GalleryMapper galleryMapper;
    private final HttpServletRequest request;
    private final FileService fileService;

    @Override
    public GalleryDto addPreWeddingPhotos(Long id, List<MultipartFile> gallery) {
        try {
            Wedding wedding = weddingRepository.findById(id).orElseThrow(() -> new WeddingException("No Such Wedding"));
            Long loggedInUser = getLoggedInUserId();
            if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
                throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
            } else {
                List<String> galleryUrls = fileService.uploadFiles(gallery);
                List<Photo> preWeddingPhotos = new ArrayList<>();
                galleryUrls.forEach(galleryUrl -> {
                    Photo p = Photo.builder().name(galleryUrl).build();
                    p = photoRepository.save(p);
                    preWeddingPhotos.add(p);
                });
                Gallery g;
                if(galleryRepository.findByWedding(wedding).isEmpty()){
                    g = Gallery.builder()
                        .preWeddingPhotos(preWeddingPhotos)
                        .wedding(wedding)
                        .build();
                }else{
                    g = galleryRepository.findByWedding(wedding).get();
                    g.setPreWeddingPhotos(preWeddingPhotos);
                }
                g = galleryRepository.save(g);
                return galleryMapper.mapGalleryToGalleryDto(g);
            }            
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    @Override
    public GalleryDto addWeddingPhotos(Long id, List<MultipartFile> gallery) {
        try {
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
                if(galleryRepository.findByWedding(wedding).isEmpty()){
                    g = Gallery.builder()
                        .weddingPhotos(weddingPhotos)
                        .wedding(wedding)
                        .build();
                }else{
                    g = galleryRepository.findByWedding(wedding).get();
                    g.setWeddingPhotos(weddingPhotos);
                }
                g = galleryRepository.save(g);
                return galleryMapper.mapGalleryToGalleryDto(g);
            }            
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    @Override
    public GalleryDto addPostWeddingPhotos(Long id, List<MultipartFile> gallery) {
        try {
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
                if(galleryRepository.findByWedding(wedding).isEmpty()){
                    g = Gallery.builder()
                        .postWeddingPhotos(postWeddingPhotos)
                        .wedding(wedding)
                        .build();
                }else{
                    g = galleryRepository.findByWedding(wedding).get();
                    g.setPostWeddingPhotos(postWeddingPhotos);
                }
                g = galleryRepository.save(g);
                return galleryMapper.mapGalleryToGalleryDto(g);
            }            
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    private Long getLoggedInUserId() {
        try {
            return Long.parseLong(request.getHeader("x-id"));
        } catch (RuntimeException ex) {
            throw new WeddingException("You Need To Be Logged In !!!");
        }
    }

}
