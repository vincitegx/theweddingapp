package com.slinkdigital.wedding.service;

import com.slinkdigital.wedding.constant.WeddingStatus;
import com.slinkdigital.wedding.dto.WeddingDto;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author TEGA
 */
public interface WeddingService {

    List<WeddingDto> getAllWeddingsByUser(Long userid);

    WeddingDto addWeddingInfo(WeddingDto weddingDto, MultipartFile coverFile);

    Map<String, String> publishWedding(Long id);

    WeddingDto getWeddingByCode(String code);

    WeddingDto getWeddingById(Long id);

    List<WeddingDto> getWeddingRequestsToUser();

    boolean deleteWedding(Long id);

//    List<WeddingDto> getAllPublishedWeddingsByUser(Long userid);

    WeddingDto updateWeddingInfo(WeddingDto weddingDto);

    WeddingDto updateCoverFile(WeddingDto weddingDto, MultipartFile coverFile);
    
    WeddingDto deleteCoverFile(WeddingDto weddingDto, MultipartFile coverFile);
    
    void removeGalleryImage(WeddingDto weddingDto, MultipartFile coverFile);

    Page<WeddingDto> getAllPublishedWeddings(String orElse, PageRequest of);

    Page<WeddingDto> getAllWeddings(String orElse, PageRequest of);

    Page<WeddingDto> getWeddingsWithStatus(WeddingStatus weddingStatus, String orElse, PageRequest of);
}
