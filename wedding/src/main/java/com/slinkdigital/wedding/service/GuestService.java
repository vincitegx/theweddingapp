package com.slinkdigital.wedding.service;

import com.slinkdigital.wedding.constant.AvailabilityStatus;
import com.slinkdigital.wedding.constant.GuestStatus;
import com.slinkdigital.wedding.domain.Guest;
import com.slinkdigital.wedding.domain.GuestSetting;
import com.slinkdigital.wedding.domain.Wedding;
import com.slinkdigital.wedding.dto.EventDto;
import com.slinkdigital.wedding.dto.GuestDto;
import com.slinkdigital.wedding.exception.WeddingException;
import com.slinkdigital.wedding.mapper.GuestMapper;
import com.slinkdigital.wedding.mapper.WeddingMapper;
import com.slinkdigital.wedding.repository.GuestRepository;
import com.slinkdigital.wedding.repository.GuestSettingRepository;
import com.slinkdigital.wedding.repository.WeddingRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.kafka.core.KafkaTemplate;

/**
 *
 * @author TEGA
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class GuestService {

    private final GuestRepository guestRepository;
    private final GuestSettingRepository guestSettingRepository;
    private final WeddingRepository weddingRepository;
    private final HttpServletRequest request;
    private final WeddingMapper weddingMapper;
    private final GuestMapper guestMapper;
    private static final String WEDDING_NOT_PUBLISHED = "You have to publish this wedding first";
    private static final String AUTHORIZATION_ERROR = "Cannot Identify The User, Therefore operation cannot be performed";
    private static final String WEDDING_NOT_FOUND = "No Such Wedding";

    public Page<GuestDto> getAllGuestsForWedding(Long id, String search, PageRequest pageRequest) {
        Wedding w = weddingRepository.findById(id).orElseThrow(() -> new WeddingException(WEDDING_NOT_FOUND));
        Page<Guest> guests = guestRepository.findByWeddingAndNameContainsOrEmailContains(w, search, search, pageRequest);
        List<Guest> guestList = guests.toList();
        Page<GuestDto> guestDto = new PageImpl(guestList);
        guests.forEach(g -> guestDto.and(guestMapper.mapGuestToGuestDto(g)));
        return guestDto;
    }

    public GuestDto addGuest(GuestDto guestDto) {
        Wedding wedding = weddingRepository.findById(guestDto.getWedding().getId()).orElseThrow(() -> new WeddingException(WEDDING_NOT_FOUND));
        Long loggedInUser = getLoggedInUserId();
        if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
            throw new WeddingException(AUTHORIZATION_ERROR);
        } else if (!wedding.getIsPublished()) {
            throw new WeddingException(WEDDING_NOT_PUBLISHED);
        } else {
            guestDto.setCreatedAt(LocalDateTime.now());
            guestDto.setAvailabilityStatus(AvailabilityStatus.NO_RESPONSE);
            guestDto.setGuestStatus(GuestStatus.INVITED);
            Guest guest = guestMapper.mapGuestDtoToGuest(guestDto);
            guest = guestRepository.save(guest);
            return guestMapper.mapGuestToGuestDto(guest);
        }
    }

    public GuestDto guestSelfAddition(GuestDto guestDto) {
        Integer allowedGuestSize = guestRepository.findByGuestStatus(GuestStatus.ALLOWED).size();
        GuestSetting guestSetting = guestSettingRepository.findByWedding(weddingMapper.mapWeddingDtoToWedding(guestDto.getWedding())).orElseThrow(() -> new WeddingException("Guest Form Is Not Enabled"));
        if (!guestSetting.isGuestFormOpened()) {
            throw new WeddingException("Guest Form Is Closed");
        } else if (guestSetting.getMaxNumberOfUnknownGuests() != null && guestSetting.getMaxNumberOfUnknownGuests() >= allowedGuestSize) {
            throw new WeddingException("Maximum uninvited guest reached");
        } else {
            guestDto.setCreatedAt(LocalDateTime.now());
            guestDto.setGuestStatus(GuestStatus.ALLOWED);
            guestDto.setAvailabilityStatus(AvailabilityStatus.COMING);
            Guest guest = guestMapper.mapGuestDtoToGuest(guestDto);
            guest = guestRepository.saveAndFlush(guest);
            return guestMapper.mapGuestToGuestDto(guest);
        }
    }

    public GuestDto changeGuestStatus(GuestDto guestDto) {
        Wedding wedding = weddingRepository.findById(guestDto.getWedding().getId()).orElseThrow(() -> new WeddingException(WEDDING_NOT_FOUND));
        Long loggedInUser = getLoggedInUserId();
        if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
            throw new WeddingException(AUTHORIZATION_ERROR);
        } else if (!wedding.getIsPublished()) {
            throw new WeddingException(WEDDING_NOT_PUBLISHED);
        } else {
            Guest guest = guestRepository.findById(guestDto.getId()).orElseThrow(() -> new WeddingException("No Such Guest !!!"));
            guest.setGuestStatus(guestDto.getGuestStatus());
            guest = guestRepository.save(guest);
            return guestMapper.mapGuestToGuestDto(guest);
        }
    }

    public void removeGuests(List<GuestDto> guestDto) {
        Wedding wedding = weddingRepository.findById(guestDto.get(0).getWedding().getId()).orElseThrow(() -> new WeddingException(WEDDING_NOT_FOUND));
        Long loggedInUser = getLoggedInUserId();
        if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
            throw new WeddingException(AUTHORIZATION_ERROR);
        } else if (!wedding.getIsPublished()) {
            throw new WeddingException(WEDDING_NOT_PUBLISHED);
        } else {
            guestDto.forEach(g -> {
                Guest guest = guestRepository.findByIdAndWedding(g.getId(), weddingMapper.mapWeddingDtoToWedding(g.getWedding())).orElseThrow(() -> new WeddingException("No Such Guest !!!"));
                guestRepository.delete(guest);
            });
        }
    }

    private Long getLoggedInUserId() {
        return Long.parseLong(request.getHeader("x-id"));
    }
}
