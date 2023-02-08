package com.slinkdigital.wedding.service.impl;

import com.slinkdigital.wedding.domain.MessageToGuest;
import com.slinkdigital.wedding.domain.Wedding;
import com.slinkdigital.wedding.dto.EventDto;
import com.slinkdigital.wedding.dto.MessageGuestDto;
import com.slinkdigital.wedding.dto.MessageToGuestDto;
import com.slinkdigital.wedding.exception.WeddingException;
import com.slinkdigital.wedding.mapper.MsgToGuestMapper;
import com.slinkdigital.wedding.repository.MessageToGuestRepository;
import com.slinkdigital.wedding.repository.WeddingRepository;
import com.slinkdigital.wedding.service.MessageToGuestService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
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
public class MessageToGuestServiceImpl implements MessageToGuestService {

    private final MessageToGuestRepository messageToGuestRepository;
    private final MsgToGuestMapper msgToGuestMapper;
    private final WeddingRepository weddingRepository;
    private final HttpServletRequest request;
    private final KafkaTemplate<String, EventDto> kakfaTemplate;

    @Override
    public List<MessageToGuestDto> getMessages(Long weddingId) {
        try {
            Wedding wedding = weddingRepository.findById(weddingId).orElseThrow(() -> new WeddingException("No Such Wedding"));
            Long loggedInUser = getLoggedInUserId();
            if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
                throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
            } else if (!wedding.isPublished()) {
                throw new WeddingException("You have to publish this wedding first");
            } else {
                List<MessageToGuest> messageToGuests = messageToGuestRepository.findByWedding(wedding);
                List<MessageToGuestDto> messageToGuestDtos = new ArrayList<>(messageToGuests.size());
                messageToGuests.forEach(m -> {
                    messageToGuestDtos.add(msgToGuestMapper.mapMessageToGuestToDto(m));
                });
                return messageToGuestDtos;
            }
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    @Override
    public MessageToGuestDto getMessage(Long id) {
        try {
            MessageToGuest messageToGuest = messageToGuestRepository.findById(id).orElseThrow(() -> new WeddingException("No such message associated to this Id"));
            Wedding wedding = messageToGuest.getWedding();
            Long loggedInUser = getLoggedInUserId();
            if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
                throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
            } else if (!wedding.isPublished()) {
                throw new WeddingException("You have to publish this wedding first");
            } else {
                return msgToGuestMapper.mapMessageToGuestToDto(messageToGuest);
            }
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    @Override
    public MessageToGuestDto addMessage(MessageToGuestDto messageToGuestDto) {
        try {
            Wedding wedding = weddingRepository.findById(messageToGuestDto.getWedding().getId()).orElseThrow(() -> new WeddingException("No such wedding associated to this Id"));
            Long loggedInUser = getLoggedInUserId();
            if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
                throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
            } else if (!wedding.isPublished()) {
                throw new WeddingException("You have to publish this wedding first");
            } else {
                MessageToGuest messageToGuest = msgToGuestMapper.mapDtoToMessageToGuest(messageToGuestDto);
                messageToGuest = messageToGuestRepository.save(messageToGuest);
                return msgToGuestMapper.mapMessageToGuestToDto(messageToGuest);
            }
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    @Override
    public MessageToGuestDto putContent(MessageToGuestDto messageToGuestDto) {
        try {
            MessageToGuest messageToGuest = messageToGuestRepository.findById(messageToGuestDto.getId()).orElseThrow(() -> new WeddingException("No such message associated to this Id"));
            Wedding wedding = messageToGuest.getWedding();
            Long loggedInUser = getLoggedInUserId();
            if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
                throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
            } else if (!wedding.isPublished()) {
                throw new WeddingException("You have to publish this wedding first");
            } else {
                messageToGuest.setContent(messageToGuestDto.getContent());
                messageToGuest.setTitle(messageToGuestDto.getTitle());
                messageToGuest = messageToGuestRepository.save(messageToGuest);
                return msgToGuestMapper.mapMessageToGuestToDto(messageToGuest);
            }
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    @Override
    public Map<String, String> deleteMessage(Long id) {
        try {
            MessageToGuest messageToGuest = messageToGuestRepository.findById(id).orElseThrow(() -> new WeddingException("No such message associated to this Id"));
            Wedding wedding = messageToGuest.getWedding();
            Long loggedInUser = getLoggedInUserId();
            if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
                throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
            } else if (!wedding.isPublished()) {
                throw new WeddingException("You have to publish this wedding first");
            } else {
                Map<String, String> result = new HashMap<>();
                messageToGuestRepository.delete(messageToGuest);
                result.put("success", "Message deleted successfully");
                return result;
            }
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    @Override
    public Map<String, String> sendMessageToGuests(MessageGuestDto messageGuestDto) {
        try {
            Map<String, String> data = new HashMap<>();
            MessageToGuest messageToGuest = messageToGuestRepository.findById(messageGuestDto.getAuthorId()).orElseThrow(() -> new WeddingException("No such message associated to this Id"));
            Wedding wedding = messageToGuest.getWedding();
            Long loggedInUser = getLoggedInUserId();
            if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
                throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
            } else if (!wedding.isPublished()) {
                throw new WeddingException("You have to publish this wedding first");
            } else {
                messageGuestDto.getGuestEmail().forEach(g -> {
                    data.put("msg", messageGuestDto.getMessage());
                    data.put("subject", messageGuestDto.getSubject());
                    EventDto eventDto = EventDto.builder().from(messageGuestDto.getAuthorEmail()).to(g).data(data).build();
                    kakfaTemplate.send("msg-to-guest", eventDto);
                });
                data.put("success", "Email sent successfully");
                return data;
            }
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
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
