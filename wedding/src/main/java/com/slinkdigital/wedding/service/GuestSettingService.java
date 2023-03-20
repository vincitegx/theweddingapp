package com.slinkdigital.wedding.service;

import com.slinkdigital.wedding.domain.GuestSetting;
import com.slinkdigital.wedding.domain.Wedding;
import com.slinkdigital.wedding.dto.GuestSettingDto;
import com.slinkdigital.wedding.exception.WeddingException;
import com.slinkdigital.wedding.mapper.GuestSettingMapper;
import com.slinkdigital.wedding.repository.GuestSettingRepository;
import com.slinkdigital.wedding.repository.WeddingRepository;
import java.util.Objects;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author TEGA
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class GuestSettingService {

    private final GuestSettingRepository guestSettingRepository;
    private final WeddingRepository weddingRepository;
    private final GuestSettingMapper guestSettingMapper;
    private final HttpServletRequest request;

//    
//    public boolean changeGuestFormStatus(GuestSettingDto guestSettingDto) {
//        try {
//            boolean isGuestFormStausChanged;
//            Wedding w = weddingRepository.findById(guestSettingDto.getWedding().getId()).orElseThrow(() -> new WeddingException("No such wedding associated to this id"));
//            GuestSetting guestSetting = guestSettingRepository.findByWedding(w).orElseThrow(() -> new WeddingException("No Wedding is associated to this guest"));
//            guestSetting.setGuestFormOpened(guestSettingDto.isGuestFormOpened());
//            guestSettingRepository.save(guestSetting);
//            isGuestFormStausChanged = true;
//            return isGuestFormStausChanged;
//        } catch (WeddingException ex) {
//            throw new WeddingException(ex.getMessage());
//        }
//    }
//
//    
//    public boolean changeMaxNumberOfAllowedGuests(GuestSettingDto guestSettingDto) {
//        try {
//            boolean isMaxNumberOfAllowedGuestsChanged;
//            Wedding w = weddingRepository.findById(guestSettingDto.getWedding().getId()).orElseThrow(() -> new WeddingException("No such wedding associated to this id"));
//            GuestSetting guestSetting = guestSettingRepository.findByWedding(w).orElseThrow(() -> new WeddingException("No Wedding is associated to this guest"));
//            guestSetting.setMaxNumberOfUnknownGuests(guestSettingDto.getMaxNumberOfUnknownGuests());
//            guestSettingRepository.save(guestSetting);
//            isMaxNumberOfAllowedGuestsChanged = true;
//            return isMaxNumberOfAllowedGuestsChanged;
//        } catch (WeddingException ex) {
//            throw new WeddingException(ex.getMessage());
//        }
//    }
    public GuestSettingDto addGuestSetting(GuestSettingDto setting) {
        Long loggedInUser = getLoggedInUserId();
        Wedding wedding = weddingRepository.findById(setting.getWedding().getId()).orElseThrow(() -> new WeddingException("No wedding associated to this id"));
        if (!Objects.equals(loggedInUser, wedding.getAuthorId()) && !Objects.equals(loggedInUser, wedding.getSpouseId())) {
            throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
        } else {
            GuestSetting gs = guestSettingMapper.mapDtoToSetting(setting);
            gs = guestSettingRepository.saveAndFlush(gs);
            return guestSettingMapper.mapSettingToDto(gs);
        }
    }

    public GuestSettingDto changeGuestSetting(GuestSettingDto setting) {
        Long loggedInUser = getLoggedInUserId();
        Wedding wedding = weddingRepository.findById(setting.getWedding().getId()).orElseThrow(() -> new WeddingException("No wedding associated to this id"));
        if (!Objects.equals(loggedInUser, wedding.getAuthorId()) && !Objects.equals(loggedInUser, wedding.getSpouseId())) {
            throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
        } else {
            GuestSetting gs = guestSettingRepository.findById(setting.getId()).orElseThrow(() -> new WeddingException("No Guest Setting Associated to this id"));
            gs.setGuestFormOpened(setting.isGuestFormOpened());
            gs.setMaxNumberOfUnknownGuests(setting.getMaxNumberOfUnknownGuests());
            gs = guestSettingRepository.saveAndFlush(gs);
            return guestSettingMapper.mapSettingToDto(gs);
        }
    }

    private Long getLoggedInUserId() {
        try {
            return Long.parseLong(request.getHeader("x-id"));
        } catch (WeddingException ex) {
            throw new WeddingException("You Need To Be Logged In !!!");
        }
    }
}
