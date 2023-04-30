package com.slinkdigital.wedding.service;

import com.slinkdigital.wedding.constant.AvailabilityStatus;
import com.slinkdigital.wedding.constant.GuestStatus;
import com.slinkdigital.wedding.domain.Guest;
import com.slinkdigital.wedding.domain.GuestSetting;
import com.slinkdigital.wedding.domain.Wedding;
import com.slinkdigital.wedding.dto.EventDto;
import com.slinkdigital.wedding.dto.GuestDto;
import com.slinkdigital.wedding.dto.MessageGuestDto;
import com.slinkdigital.wedding.exception.WeddingException;
import com.slinkdigital.wedding.mapper.GuestMapper;
import com.slinkdigital.wedding.mapper.WeddingMapper;
import com.slinkdigital.wedding.repository.GuestRepository;
import com.slinkdigital.wedding.repository.GuestSettingRepository;
import com.slinkdigital.wedding.repository.WeddingRepository;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
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
    private final KafkaTemplate<String, EventDto> kakfaTemplate;

    public Page<GuestDto> getAllGuestsForWedding(Long id, String search, PageRequest pageRequest) {
        Wedding w = weddingRepository.findById(id).orElseThrow(() -> new WeddingException("No such wedding associated to this id"));
        Page<Guest> guests = guestRepository.findByWeddingAndNameContainsOrEmailContains(w, search, search, pageRequest);
        List<Guest> guestList = guests.toList();
        Page<GuestDto> guestDto = new PageImpl(guestList);
        guests.forEach(g -> {
            guestDto.and(guestMapper.mapGuestToGuestDto(g));
        });
        return guestDto;
    }

    public GuestDto addGuest(GuestDto guestDto) {
        Wedding wedding = weddingRepository.findById(guestDto.getWedding().getId()).orElseThrow(() -> new WeddingException("No Such Wedding"));
        Long loggedInUser = getLoggedInUserId();
        if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
            throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
        } else if (!wedding.getIsPublished()) {
            throw new WeddingException("You have to publish this wedding first");
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
            log.info(guest.toString());
            return guestMapper.mapGuestToGuestDto(guest);
        }
    }

    public GuestDto changeGuestStatus(GuestDto guestDto) {
        Wedding wedding = weddingRepository.findById(guestDto.getWedding().getId()).orElseThrow(() -> new WeddingException("No Such Wedding"));
        Long loggedInUser = getLoggedInUserId();
        if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
            throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
        } else if (!wedding.getIsPublished()) {
            throw new WeddingException("You have to publish this wedding first");
        } else {
            Guest guest = guestRepository.findById(guestDto.getId()).orElseThrow(() -> new WeddingException("No Such Guest !!!"));
            guest.setGuestStatus(guestDto.getGuestStatus());
            guest = guestRepository.save(guest);
            return guestMapper.mapGuestToGuestDto(guest);
        }
    }

    public void removeGuest(GuestDto guestDto) {
        Wedding wedding = weddingRepository.findById(guestDto.getWedding().getId()).orElseThrow(() -> new WeddingException("No Such Wedding"));
        Long loggedInUser = getLoggedInUserId();
        if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
            throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
        } else if (!wedding.getIsPublished()) {
            throw new WeddingException("You have to publish this wedding first");
        } else {
            Guest guest = guestRepository.findByIdAndWedding(guestDto.getId(), weddingMapper.mapWeddingDtoToWedding(guestDto.getWedding())).orElseThrow(() -> new WeddingException("No Such Guest !!!"));
            guestRepository.delete(guest);
        }
    }

    public void removeGuests(List<GuestDto> guestDto) {
        Wedding wedding = weddingRepository.findById(guestDto.get(0).getWedding().getId()).orElseThrow(() -> new WeddingException("No Such Wedding"));
        Long loggedInUser = getLoggedInUserId();
        if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
            throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
        } else if (!wedding.getIsPublished()) {
            throw new WeddingException("You have to publish this wedding first");
        } else {
            guestDto.forEach(g -> {
                Guest guest = guestRepository.findByIdAndWedding(g.getId(), weddingMapper.mapWeddingDtoToWedding(g.getWedding())).orElseThrow(() -> new WeddingException("No Such Guest !!!"));
                guestRepository.delete(guest);
            });
        }
    }

    public Map<String, String> sendInvitationToGuest(GuestDto guestDto) {
        Wedding wedding = weddingRepository.findById(guestDto.getWedding().getId()).orElseThrow(() -> new WeddingException("No Such Wedding"));
        Long loggedInUser = getLoggedInUserId();
        if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
            throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
        } else if (!wedding.getIsPublished()) {
            throw new WeddingException("You have to publish this wedding first");
        } else {
            Map<String, String> result = new HashMap<>();
            Guest guest = guestRepository.findById(guestDto.getId()).orElseThrow(() -> new WeddingException("No Such Guest !!!"));
            Map<String, String> data = new HashMap<>();
            data.put("weddingcode", guest.getWedding().getCode());
            EventDto eventDto = EventDto.builder().from(getLoggedInUserEmail()).to(guest.getEmail()).data(data).build();
            kakfaTemplate.send("email-to-guest", eventDto);
            result.put("success", "Email sent successfully");
            return result;
        }
    }

    public Map<String, String> sendInvitationToGuests(MessageGuestDto messageGuestDto) {
        Wedding wedding = weddingRepository.findById(messageGuestDto.getMessage().getWedding().getId()).orElseThrow(() -> new WeddingException("No Such Wedding"));
        Long loggedInUser = getLoggedInUserId();
        if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
            throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
        } else if (!wedding.getIsPublished()) {
            throw new WeddingException("You have to publish this wedding first");
        } else {
            Map<String, String> result = new HashMap<>();
            messageGuestDto.getGuests().forEach(g -> {
                Guest guest = guestRepository.findById(g.getId()).orElseThrow(() -> new WeddingException("No Such Guest !!!"));
                Map<String, String> data = new HashMap<>();
                data.put("weddingcode", g.getWedding().getCode());
                EventDto eventDto = EventDto.builder().from(getLoggedInUserEmail()).to(guest.getEmail()).data(data).build();
                kakfaTemplate.send("guest-invitation", eventDto);
            });
            result.put("success", "Email sent successfully");
            return result;
        }
    }

    public GuestDto submitInvitationResponse(GuestDto guestDto) {
        return null;
    }

    private Long getLoggedInUserId() {
        return Long.parseLong(request.getHeader("x-id"));
    }

    private String getLoggedInUserEmail() {
        return request.getHeader("x-email");
    }
}
