package com.slinkdigital.wedding.service;

import com.slinkdigital.wedding.domain.GuestMessage;
import com.slinkdigital.wedding.domain.Wedding;
import com.slinkdigital.wedding.dto.EventDto;
import com.slinkdigital.wedding.dto.MessageGuestDto;
import com.slinkdigital.wedding.dto.GuestMessageDto;
import com.slinkdigital.wedding.exception.WeddingException;
import com.slinkdigital.wedding.mapper.MsgToGuestMapper;
import com.slinkdigital.wedding.repository.WeddingRepository;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.slinkdigital.wedding.repository.MessageRepository;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.kafka.core.KafkaTemplate;

/**
 *
 * @author TEGA
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class GuestMessageService {

    private final MessageRepository messageToGuestRepository;
    private final MsgToGuestMapper msgToGuestMapper;
    private final WeddingRepository weddingRepository;
    private final HttpServletRequest request;
    private final KafkaTemplate<String, EventDto> kakfaTemplate;

    public List<GuestMessageDto> getMessages(Long weddingId) {
        Wedding wedding = weddingRepository.findById(weddingId).orElseThrow(() -> new WeddingException("No Such Wedding"));
        Long loggedInUser = getLoggedInUserId();
        if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
            throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
        } else if (!wedding.getIsPublished()) {
            throw new WeddingException("You have to publish this wedding first");
        } else {
            List<GuestMessage> messageToGuests = messageToGuestRepository.findByWedding(wedding);
            List<GuestMessageDto> messageToGuestDtos = new ArrayList<>(messageToGuests.size());
            messageToGuests.forEach(m -> {
                messageToGuestDtos.add(msgToGuestMapper.mapMessageToGuestToDto(m));
            });
            return messageToGuestDtos;
        }
    }

    public GuestMessageDto getMessage(Long id) {
        GuestMessage messageToGuest = messageToGuestRepository.findById(id).orElseThrow(() -> new WeddingException("No such message associated to this Id"));
        Wedding wedding = messageToGuest.getWedding();
        Long loggedInUser = getLoggedInUserId();
        if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
            throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
        } else if (!wedding.getIsPublished()) {
            throw new WeddingException("You have to publish this wedding first");
        } else {
            return msgToGuestMapper.mapMessageToGuestToDto(messageToGuest);
        }
    }

    public GuestMessageDto addMessage(GuestMessageDto messageToGuestDto) {
        Wedding wedding = weddingRepository.findById(messageToGuestDto.getWedding().getId()).orElseThrow(() -> new WeddingException("No such wedding associated to this Id"));
        Long loggedInUser = getLoggedInUserId();
        if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
            throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
        } else if (!wedding.getIsPublished()) {
            throw new WeddingException("You have to publish this wedding first");
        } else {
            messageToGuestDto.setCreatedAt(LocalDateTime.now());
            GuestMessage messageToGuest = msgToGuestMapper.mapDtoToMessageToGuest(messageToGuestDto);
            messageToGuest = messageToGuestRepository.save(messageToGuest);
            return msgToGuestMapper.mapMessageToGuestToDto(messageToGuest);
        }
    }

    public GuestMessageDto putContent(GuestMessageDto messageToGuestDto) {
        GuestMessage messageToGuest = messageToGuestRepository.findById(messageToGuestDto.getId()).orElseThrow(() -> new WeddingException("No such message associated to this Id"));
        Wedding wedding = messageToGuest.getWedding();
        Long loggedInUser = getLoggedInUserId();
        if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
            throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
        } else if (!wedding.getIsPublished()) {
            throw new WeddingException("You have to publish this wedding first");
        } else {
            messageToGuest.setContent(messageToGuestDto.getContent());
            messageToGuest.setTitle(messageToGuestDto.getTitle());
            messageToGuest = messageToGuestRepository.save(messageToGuest);
            return msgToGuestMapper.mapMessageToGuestToDto(messageToGuest);
        }
    }

    public void deleteMessage(Long id) {
        GuestMessage messageToGuest = messageToGuestRepository.findById(id).orElseThrow(() -> new WeddingException("No such message associated to this Id"));
        Wedding wedding = messageToGuest.getWedding();
        Long loggedInUser = getLoggedInUserId();
        if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
            throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
        } else if (!wedding.getIsPublished()) {
            throw new WeddingException("You have to publish this wedding first");
        } else {
            messageToGuestRepository.delete(messageToGuest);
        }
    }

    public void sendMessageToGuests(MessageGuestDto messageGuestDto) {
        GuestMessage messageToGuest = messageToGuestRepository.findById(messageGuestDto.getMessage().getId()).orElseThrow(() -> new WeddingException("No such message associated to this Id"));
        Wedding wedding = messageToGuest.getWedding();
        Long loggedInUser = getLoggedInUserId();
        if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
            throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
        } else if (!wedding.getIsPublished()) {
            throw new WeddingException("You have to publish this wedding first");
        } else {
            String userEmail = getLoggedInUserEmail();
                messageGuestDto.getGuests().forEach(g -> {
                    Map<String, String> data = new HashMap<>();
                    data.put("msg", messageGuestDto.getMessage().getContent());
                    data.put("subject", messageGuestDto.getMessage().getTitle());
                    EventDto eventDto = EventDto.builder().from(userEmail).to(g.getEmail()).data(data).build();
                    kakfaTemplate.send("msg-to-guest", eventDto);
                });
        }
    }

    private Long getLoggedInUserId() {
        return Long.parseLong(request.getHeader("x-id"));
    }
    
    private String getLoggedInUserEmail() {
        return request.getHeader("x-email");
    }
}
