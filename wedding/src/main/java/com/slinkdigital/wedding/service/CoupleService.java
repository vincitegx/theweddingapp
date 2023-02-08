package com.slinkdigital.wedding.service;

import com.slinkdigital.wedding.dto.CoupleDto;
import com.slinkdigital.wedding.dto.SpouseRequest;
import java.util.Map;
import java.util.Set;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author TEGA
 */
public interface CoupleService {
    
    CoupleDto findByUserId(Long userId);
    
    CoupleDto addCoupleAuthorInfo(CoupleDto coupleDto, MultipartFile file);
    
    CoupleDto addCoupleSpouseInfo(CoupleDto coupleDto, MultipartFile file);
    
    CoupleDto updateCoupleSpouseInfo(CoupleDto couple);

    CoupleDto updateCoupleAuthorInfo(CoupleDto couple);
    
    CoupleDto updateCoupleAuthorImage(CoupleDto couple, MultipartFile file);

    CoupleDto updateCoupleSpouseImage(CoupleDto couple, MultipartFile file);
    
    Map<String, String> sendCoupleRequest(SpouseRequest emailRequest);
    
    Map<String, String> removeCoupleRequest(SpouseRequest emailRequest);
    
    Map<String, String> removeCoupleSpouse(CoupleDto coupleDto);
    
    Set<CoupleDto> getWeddingCouple(Long id);
    
    boolean acceptAuthorRequest(String requestToken);
    
    boolean rejectAuthorRequest(String requestToken);

    CoupleDto viewAuthorRequest(String requestToken);
}
