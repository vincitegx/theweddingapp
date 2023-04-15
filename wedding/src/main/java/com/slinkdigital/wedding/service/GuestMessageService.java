package com.slinkdigital.wedding.service;

import com.slinkdigital.wedding.domain.Message;
import com.slinkdigital.wedding.domain.Wedding;
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

    public List<GuestMessageDto> getMessages(Long weddingId) {
        Wedding wedding = weddingRepository.findById(weddingId).orElseThrow(() -> new WeddingException("No Such Wedding"));
        Long loggedInUser = getLoggedInUserId();
        if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
            throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
        } else if (!wedding.getIsPublished()) {
            throw new WeddingException("You have to publish this wedding first");
        } else {
            List<Message> messageToGuests = messageToGuestRepository.findByWedding(wedding);
            List<GuestMessageDto> messageToGuestDtos = new ArrayList<>(messageToGuests.size());
            messageToGuests.forEach(m -> {
                messageToGuestDtos.add(msgToGuestMapper.mapMessageToGuestToDto(m));
            });
            return messageToGuestDtos;
        }
    }

    public GuestMessageDto getMessage(Long id) {
        Message messageToGuest = messageToGuestRepository.findById(id).orElseThrow(() -> new WeddingException("No such message associated to this Id"));
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
            Message messageToGuest = msgToGuestMapper.mapDtoToMessageToGuest(messageToGuestDto);
            messageToGuest = messageToGuestRepository.save(messageToGuest);
            return msgToGuestMapper.mapMessageToGuestToDto(messageToGuest);
        }
    }

    public GuestMessageDto putContent(GuestMessageDto messageToGuestDto) {
        Message messageToGuest = messageToGuestRepository.findById(messageToGuestDto.getId()).orElseThrow(() -> new WeddingException("No such message associated to this Id"));
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
        Message messageToGuest = messageToGuestRepository.findById(id).orElseThrow(() -> new WeddingException("No such message associated to this Id"));
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
        Message messageToGuest = messageToGuestRepository.findById(messageGuestDto.getMessage().getId()).orElseThrow(() -> new WeddingException("No such message associated to this Id"));
        Wedding wedding = messageToGuest.getWedding();
        Long loggedInUser = getLoggedInUserId();
        if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
            throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
        } else if (!wedding.getIsPublished()) {
            throw new WeddingException("You have to publish this wedding first");
        } else {
//                messageGuestDto.getGuestEmail().forEach(g -> {
//                    data.put("msg", messageGuestDto.getMessage());
//                    data.put("subject", messageGuestDto.getSubject());
//                    EventDto eventDto = EventDto.builder().from(messageGuestDto.getAuthorEmail()).to(g).data(data).build();
//                    kakfaTemplate.send("msg-to-guest", eventDto);
//                });
        }
    }

    private Long getLoggedInUserId() {
        return Long.parseLong(request.getHeader("x-id"));
    }

}
