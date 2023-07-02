package com.slinkdigital.wedding.service;

import com.slinkdigital.wedding.constant.CoupleStatus;
import com.slinkdigital.wedding.domain.Couple;
import com.slinkdigital.wedding.domain.CoupleRequest;
import com.slinkdigital.wedding.domain.Wedding;
import com.slinkdigital.wedding.dto.CoupleDto;
import com.slinkdigital.wedding.dto.EventDto;
import com.slinkdigital.wedding.dto.SpouseRequest;
import com.slinkdigital.wedding.exception.WeddingException;
import com.slinkdigital.wedding.mapper.CoupleMapper;
import com.slinkdigital.wedding.mapper.WeddingMapper;
import com.slinkdigital.wedding.repository.CoupleRepository;
import com.slinkdigital.wedding.repository.CoupleRequestRepository;
import com.slinkdigital.wedding.repository.WeddingRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CoupleService {

    private final CoupleRepository coupleRepository;
    private final WeddingRepository weddingRepository;
    private final CoupleRequestRepository coupleRequestRepository;
    private final HttpServletRequest request;
    private final FileService fileService;
    private final UserService userService;
    private final WeddingMapper weddingMapper;
    private final CoupleMapper coupleMapper;
    @Value("${spouse.request.expiration.time.days}")
    private Long spouseRequestExpirationInDays;
    private final KafkaTemplate<String, EventDto> kakfaTemplate;
    private static final String REQUEST_TOKEN_ERROR = "Invalid Request Token";
    private static final String AUTHORIZATION_ERROR = "Cannot Identify The User, Therefore operation cannot be performed";
    private static final String WEDDING_NOT_FOUND = "No Such Wedding";

    public CoupleDto findByUserId(Long userId) {
        Couple couple = coupleRepository.findByUserId(userId).orElseThrow(() -> new WeddingException("No such couple with id"));
        CoupleDto coupleDto = coupleMapper.mapCoupleToCoupleDto(couple);
        return coupleDto;
    }

    public CoupleDto addCoupleAuthorInfo(CoupleDto coupleDto, MultipartFile file) {
        Wedding wedding = weddingRepository.findById(coupleDto.getWedding().getId()).orElseThrow(() -> new WeddingException(WEDDING_NOT_FOUND));
        if (coupleDto.getUserId() == null ? wedding.getAuthorId() != null : !coupleDto.getUserId().equals(wedding.getAuthorId())) {
            throw new WeddingException(AUTHORIZATION_ERROR);
        } else {
            coupleDto.setWedding(weddingMapper.mapWeddingToDto(wedding));
            coupleDto.setCreatedAt(LocalDateTime.now());
            String imageUrl;
            try {
                imageUrl = fileService.uploadFile(file);
                coupleDto.setImageUrl(imageUrl);
            } catch (IOException ex) {
                Logger.getLogger(CoupleService.class.getName()).log(Level.SEVERE, null, ex);
            }
            Couple c = coupleRepository.save(coupleMapper.mapCoupleDtoToCouple(coupleDto));
            return coupleMapper.mapCoupleToCoupleDto(c);
        }
    }

    public CoupleDto addCoupleSpouseInfo(CoupleDto coupleDto, MultipartFile file) {
        Wedding wedding = weddingRepository.findById(coupleDto.getWedding().getId()).orElseThrow(() -> new WeddingException(WEDDING_NOT_FOUND));
        Long loggedInUser = getLoggedInUserId();
        if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
            throw new WeddingException(AUTHORIZATION_ERROR);
        } else {
            String imageUrl;
            try {
                imageUrl = fileService.uploadFile(file);
                coupleDto.setImageUrl(imageUrl);
            } catch (IOException ex) {
                Logger.getLogger(CoupleService.class.getName()).log(Level.SEVERE, null, ex);
            }
            coupleDto.setWedding(weddingMapper.mapWeddingToDto(wedding));
            coupleDto.setCreatedAt(LocalDateTime.now());
            Couple c = coupleRepository.save(coupleMapper.mapCoupleDtoToCouple(coupleDto));
            return coupleMapper.mapCoupleToCoupleDto(c);
        }
    }

    public void sendCoupleRequest(SpouseRequest spouseRequest) {
        String authorEmail = getLoggedInUserEmail();
        Wedding wedding = weddingRepository.findById(spouseRequest.getWedding().getId()).orElseThrow(() -> new WeddingException(WEDDING_NOT_FOUND));
        Long loggedInUser = getLoggedInUserId();
        if (wedding.getSpouseId() != null) {
            throw new WeddingException("A spouse has already been set, remove spouse before setting a new one");
        } else if (wedding.getAuthorId() == null ? loggedInUser != null : !wedding.getAuthorId().equals(loggedInUser)) {
            throw new WeddingException(AUTHORIZATION_ERROR);
        } else {
            String requestToken = UUID.randomUUID().toString();
            CoupleRequest coupleRequest = CoupleRequest.builder()
                    .email(spouseRequest.getEmail())
                    .authorEmail(authorEmail)
                    .wedding(wedding)
                    .requestToken(requestToken)
                    .createdAt(LocalDateTime.now())
                    .expiresAt(LocalDateTime.now().plusDays(spouseRequestExpirationInDays))
                    .build();
            coupleRequest = coupleRequestRepository.save(coupleRequest);
            wedding.setCoupleStatus(CoupleStatus.REQUEST_SENT);
            weddingRepository.saveAndFlush(wedding);
            Map<String, String> data = new HashMap<>();
            data.put("token", coupleRequest.getRequestToken());
            EventDto eventDto = EventDto.builder().from(authorEmail).to(spouseRequest.getEmail()).data(data).build();
            kakfaTemplate.send("couple-request", eventDto);
        }
    }

    public void removeCoupleRequest(SpouseRequest spouseRequest) {
        Wedding wedding = weddingRepository.findById(spouseRequest.getWedding().getId()).orElseThrow(() -> new WeddingException("No Such Wedding"));
        Long loggedInUser = getLoggedInUserId();
        Optional<CoupleRequest> coupleRequest = coupleRequestRepository.findByEmailAndWedding(spouseRequest.getEmail(), wedding);
        if (wedding.getAuthorId() == null ? loggedInUser != null : !wedding.getAuthorId().equals(loggedInUser)) {
            throw new WeddingException(AUTHORIZATION_ERROR);
        } else if (coupleRequest.isEmpty()) {
            throw new WeddingException("No Such Couple Request Found");
        } else {
            coupleRequestRepository.delete(coupleRequest.get());
            wedding.setCoupleStatus(CoupleStatus.REQUEST_NOT_SENT);
            weddingRepository.save(wedding);
        }
    }

    public void removeCoupleSpouse(CoupleDto coupleDto) {
        Wedding wedding = weddingRepository.findById(coupleDto.getWedding().getId()).orElseThrow(() -> new WeddingException("No Such Wedding"));
        Long loggedInUser = getLoggedInUserId();
        if (wedding.getAuthorId() == null ? loggedInUser != null : !wedding.getAuthorId().equals(loggedInUser)) {
            throw new WeddingException(AUTHORIZATION_ERROR);
        } else if (coupleDto.getUserId() == null ? wedding.getSpouseId() != null : !coupleDto.getUserId().equals(wedding.getSpouseId())) {
            throw new WeddingException("Cannot Complete Request, Wrong Spouse Info !!!");
        } else {
            Couple couple = coupleRepository.findByUserIdAndWedding(coupleDto.getUserId(), wedding).orElseThrow(() -> new WeddingException("No Such Couple Found"));
            coupleRepository.delete(couple);
            wedding.setSpouseId(null);
            wedding.setCoupleStatus(CoupleStatus.REQUEST_NOT_SENT);
            weddingRepository.save(wedding);
        }
    }

    public Set<CoupleDto> getWeddingCouple(Long id) {
        Wedding wedding = weddingRepository.findById(id).orElseThrow(() -> new WeddingException(WEDDING_NOT_FOUND));
        List<Couple> coupleList = coupleRepository.findByWedding(wedding);
        Set<CoupleDto> coupleSet = new HashSet<>();
        coupleList.forEach(couple -> {
            CoupleDto coupleDto = coupleMapper.mapCoupleToCoupleDto(couple);
            coupleSet.add(coupleDto);
        });
        return coupleSet;
    }

    public CoupleDto viewAuthorRequest(String requestToken) {
        String spouseEmail = getLoggedInUserEmail();
        CoupleRequest coupleRequest = coupleRequestRepository.findByRequestToken(requestToken).orElseThrow(() -> new WeddingException(REQUEST_TOKEN_ERROR));
        if (LocalDateTime.now().isAfter(coupleRequest.getExpiresAt())) {
            throw new WeddingException("Sorry, cannot complete request as it no longer exists !!!");
        } else if (spouseEmail == null ? coupleRequest.getEmail() != null : !spouseEmail.equals(coupleRequest.getEmail())) {
            throw new WeddingException("Sorry you do not have access to this info");
        } else {
            Couple couple = coupleRepository.findByUserId(coupleRequest.getWedding().getAuthorId()).orElseThrow(() -> new WeddingException("Invalid User Id"));
            return coupleMapper.mapCoupleToCoupleDto(couple);
        }
    }

    public void acceptAuthorRequest(String requestToken) {
        CoupleRequest coupleRequest = coupleRequestRepository.findByRequestToken(requestToken).orElseThrow(() -> new WeddingException(REQUEST_TOKEN_ERROR));
        if (coupleRequest.getEmail() == null ? getLoggedInUserEmail() != null : !coupleRequest.getEmail().equals(getLoggedInUserEmail())) {
            throw new WeddingException(AUTHORIZATION_ERROR);
        } else if (LocalDateTime.now().isAfter(coupleRequest.getExpiresAt())) {
            coupleRequestRepository.delete(coupleRequest);
            throw new WeddingException("Request Time has expired !!!");
        } else {
            Wedding wedding = coupleRequest.getWedding();
            if (wedding.getCoupleStatus().equals(CoupleStatus.ACCEPTED)) {
                throw new WeddingException("Couple Request has been accepted by you or someone else already !!!");
            }
            Long spouseId = getLoggedInUserId();
            wedding.setSpouseId(spouseId);
            wedding.setCoupleStatus(CoupleStatus.ACCEPTED);
            wedding = weddingRepository.saveAndFlush(wedding);
            List<Couple> couple = coupleRepository.findByWedding(wedding);
            couple.forEach(c -> {
                if (c.getUserId() == null) {
                    c.setUserId(spouseId);
                    coupleRepository.save(c);
                }
            });
            List<Long> couples = new ArrayList<>();
            couples.add(wedding.getAuthorId());
            couples.add(wedding.getSpouseId());
            // remove spouse from couple request tb
            coupleRequestRepository.delete(coupleRequest);
            // update user roles for both author and spouse to role_couple
            userService.addRoleCouple(couples);
            EventDto eventDto = EventDto.builder().from(coupleRequest.getEmail()).to(coupleRequest.getAuthorEmail()).build();
            kakfaTemplate.send("couple-request-accepted", eventDto);
        }
    }

    public void rejectAuthorRequest(String requestToken) {
        CoupleRequest coupleRequest = coupleRequestRepository.findByRequestToken(requestToken).orElseThrow(() -> new WeddingException(REQUEST_TOKEN_ERROR));
        if (coupleRequest.getEmail() == null ? getLoggedInUserEmail() != null : !coupleRequest.getEmail().equals(getLoggedInUserEmail())) {
            throw new WeddingException(AUTHORIZATION_ERROR);
        } else {
            Wedding wedding = coupleRequest.getWedding();
            wedding.setCoupleStatus(CoupleStatus.REJECTED);
            weddingRepository.saveAndFlush(wedding);
            // remove spouse from couple request tb
            coupleRequestRepository.delete(coupleRequest);
            EventDto eventDto = EventDto.builder().from(coupleRequest.getEmail()).to(coupleRequest.getAuthorEmail()).build();
            kakfaTemplate.send("couple-request-rejected", eventDto);
        }
    }

    private Long getLoggedInUserId() {
        return Long.parseLong(request.getHeader("x-id"));
    }

    private String getLoggedInUserEmail() {
        return request.getHeader("x-email");
    }

    public CoupleDto updateCoupleSpouseInfo(CoupleDto coupleDto) {
        Couple couple = coupleRepository.findById(coupleDto.getId()).orElseThrow(() -> new WeddingException("No such couple found"));
        couple.setFamily(coupleDto.getFamily());
        couple.setFirstName(coupleDto.getFirstName());
        couple.setOtherNames(coupleDto.getOtherNames());
        couple = coupleRepository.saveAndFlush(couple);
        return coupleMapper.mapCoupleToCoupleDto(couple);

    }

    public CoupleDto updateCoupleAuthorInfo(CoupleDto coupleDto) {
        Couple couple = coupleRepository.findById(coupleDto.getId()).orElseThrow(() -> new WeddingException("No such couple found"));
        couple.setFamily(coupleDto.getFamily());
        couple.setFirstName(coupleDto.getFirstName());
        couple.setOtherNames(coupleDto.getOtherNames());
        couple = coupleRepository.saveAndFlush(couple);
        return coupleMapper.mapCoupleToCoupleDto(couple);
    }

    public CoupleDto updateCoupleAuthorImage(CoupleDto coupleDto, MultipartFile file){
        Wedding wedding = weddingRepository.findById(coupleDto.getWedding().getId()).orElseThrow(() -> new WeddingException(WEDDING_NOT_FOUND));
        if (coupleDto.getUserId() == null ? wedding.getSpouseId() != null : !coupleDto.getUserId().equals(wedding.getSpouseId())) {
            throw new WeddingException(AUTHORIZATION_ERROR);
        } else {
            String imageUrl;
            try {
                imageUrl = fileService.uploadFile(file);
                coupleDto.setImageUrl(imageUrl);
            } catch (IOException ex) {
                Logger.getLogger(CoupleService.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            Couple c = coupleRepository.saveAndFlush(coupleMapper.mapCoupleDtoToCouple(coupleDto));
            return coupleMapper.mapCoupleToCoupleDto(c);
        }
    }

    public CoupleDto updateCoupleSpouseImage(CoupleDto coupleDto, MultipartFile file) {
        Wedding wedding = weddingRepository.findById(coupleDto.getWedding().getId()).orElseThrow(() -> new WeddingException(WEDDING_NOT_FOUND));
        if (coupleDto.getUserId() == null ? wedding.getSpouseId() != null : !coupleDto.getUserId().equals(wedding.getSpouseId())) {
            throw new WeddingException(AUTHORIZATION_ERROR);
        } else {
            String imageUrl;
            try {
                imageUrl = fileService.uploadFile(file);
                coupleDto.setImageUrl(imageUrl);
            } catch (IOException ex) {
                Logger.getLogger(CoupleService.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            Couple c = coupleRepository.saveAndFlush(coupleMapper.mapCoupleDtoToCouple(coupleDto));
            return coupleMapper.mapCoupleToCoupleDto(c);
        }
    }
}
