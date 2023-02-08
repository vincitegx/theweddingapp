package com.slinkdigital.wedding.service;

import com.slinkdigital.wedding.dto.GiftAndSupportDto;
import java.util.List;

/**
 *
 * @author TEGA
 */
public interface GiftAndSupportService {
    
    List<GiftAndSupportDto> getAllGiftAndSupportForWedding(Long id);

    List<GiftAndSupportDto> add(GiftAndSupportDto giftAndSupportDto);

    List<GiftAndSupportDto> remove(Long id);

    List<GiftAndSupportDto> update(GiftAndSupportDto giftAndSupportDto);
    
}
