package com.slinkdigital.wedding.service;

import com.slinkdigital.wedding.constant.CoupleStatus;
import com.slinkdigital.wedding.constant.WeddingType;
import com.slinkdigital.wedding.domain.Couple;
import com.slinkdigital.wedding.domain.CoupleRequest;
import com.slinkdigital.wedding.domain.Wedding;
import com.slinkdigital.wedding.dto.WeddingDto;
import com.slinkdigital.wedding.exception.WeddingException;
import com.slinkdigital.wedding.mapper.WeddingMapper;
import com.slinkdigital.wedding.repository.CoupleRepository;
import com.slinkdigital.wedding.repository.CoupleRequestRepository;
import com.slinkdigital.wedding.repository.WeddingRepository;
import com.slinkdigital.wedding.util.SecureRandomStringGenerator;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
@Transactional
public class WeddingService {

    private final WeddingRepository weddingRepository;
    private final CoupleRepository coupleRepository;
    private final CoupleRequestRepository coupleRequestRepository;
    private final SecureRandomStringGenerator secureRandomStringGenerator;
    private final HttpServletRequest request;
    private final FileService fileService;
    private final WeddingMapper weddingMapper;
    private final PostService postService;

    public Page<WeddingDto> getAllPublishedWeddings(String weddingStatus, String title, Pageable pageable) {
        Date today = new Date();
        List<Wedding> wedding;
        if (null == weddingStatus) {
            wedding = weddingRepository.findByIsPublishedAndWeddingTypeAndTitleContains(true, WeddingType.PUBLIC, title, pageable);
        } else {
            switch (weddingStatus) {
                case "PAST" -> {
                    wedding = weddingRepository.findAllByIsPublishedAndWeddingTypeAndTitleContainsAndDateOfWeddingBeforeOrderByDateOfWeddingAsc(true, WeddingType.PUBLIC, title, today, pageable);
                }
                case "FUTURE" -> {
                    wedding = weddingRepository.findAllByIsPublishedAndWeddingTypeAndTitleContainsAndDateOfWeddingAfterOrderByDateOfWeddingAsc(true, WeddingType.PUBLIC, title, today, pageable);
                }
                default -> {
                    wedding = weddingRepository.findAllByIsPublishedAndWeddingTypeAndTitleContainsAndDateOfWeddingEqualsOrderByDateOfWeddingAsc(true, WeddingType.PUBLIC, title, today, pageable);
                }
            }
        }
        List<WeddingDto> weddingDto = wedding.stream().map(w -> {
            return this.weddingMapper.mapWeddingToDto(w);
        }).collect(Collectors.toList());
        Page<WeddingDto> weddingDtos = new PageImpl(weddingDto);
        return weddingDtos;
    }

    public Page<WeddingDto> getAllWeddings(String title, Pageable pageable) {
        //admin endpoint, to be secured
        Page<Wedding> wedding = weddingRepository.findByTitleContains(title, pageable);
        List<WeddingDto> weddingDto = wedding.stream().map(w -> {
            return this.weddingMapper.mapWeddingToDto(w);
        }).collect(Collectors.toList());
        Page<WeddingDto> weddingDtos = new PageImpl(weddingDto);
        return weddingDtos;
    }

    public List<WeddingDto> getAllWeddingsByUser(Long userid) {
        List<WeddingDto> weddingDtos = new ArrayList<>();
        List<Wedding> wedding = weddingRepository.findByAuthorIdOrSpouseId(userid, userid);
        if (!wedding.isEmpty()) {
            List<Wedding> w = wedding;
            w.forEach((result) -> {
                weddingDtos.add(weddingMapper.mapWeddingToDto(result));
            });
        }
        return weddingDtos;
    }

    public List<WeddingDto> getWeddingRequestsToUser() {
        String email = getLoggedInUserEmail();
        List<CoupleRequest> coupleRequest = coupleRequestRepository.findByEmail(email);
        List<WeddingDto> weddingDtos = new ArrayList<>();
        coupleRequest.forEach(req -> {
            weddingDtos.add(weddingMapper.mapWeddingToDto(req.getWedding()));
        });
        return weddingDtos;
    }

    public WeddingDto getWeddingById(Long id) {
        Wedding wedding = weddingRepository.findById(id).orElseThrow(() -> new WeddingException("Sorry, No Such Wedding Found !!!"));
        return weddingMapper.mapWeddingToDto(wedding);
    }

    public WeddingDto getWeddingByCode(String code) {
        Wedding wedding = weddingRepository.findByCode(code).orElseThrow(() -> new WeddingException("Sorry, No Such Wedding Found !!!"));
        return weddingMapper.mapWeddingToDto(wedding);
    }

    public WeddingDto addWeddingInfo(WeddingDto weddingDto, MultipartFile coverFile) throws IOException {
        if (weddingDto.getId() == null) {
            Long loggedInUser = getLoggedInUserId();
            if (loggedInUser == null ? weddingDto.getAuthorId() != null : !loggedInUser.equals(weddingDto.getAuthorId())) {
                throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
            } else {
                weddingDto.setCoupleStatus(CoupleStatus.REQUEST_NOT_SENT);
                weddingDto.setCreatedAt(LocalDateTime.now());
                weddingDto.setIsPublished(false);
                String coverFileUrl;
                if (coverFile == null || coverFile.getSize() == 0) {
                    coverFileUrl = null;
                } else {
                    coverFileUrl = fileService.uploadFile(coverFile);
                }
                weddingDto.setCoverFileUrl(coverFileUrl);
                Wedding wedding = weddingRepository.save(weddingMapper.mapWeddingDtoToWedding(weddingDto));
                return weddingMapper.mapWeddingToDto(wedding);
            }
        } else {
            throw new WeddingException("Cannot save this wedding as a new wedding as it already exists");
        }
    }

    private Long getLoggedInUserId() {
        return Long.parseLong(request.getHeader("x-id"));
    }

    private String getLoggedInUserEmail() {
        return request.getHeader("x-email");
    }

    public WeddingDto publishWedding(Long id) {
        Wedding wedding = weddingRepository.findById(id).orElseThrow(() -> new WeddingException("No Such Wedding"));
        Long loggedInUser = getLoggedInUserId();
        if (wedding.getAuthorId() == null ? loggedInUser != null : !wedding.getAuthorId().equals(loggedInUser)) {
            throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
//            } else if (wedding.isPublished()) {
//                throw new WeddingException("This wedding has already been published!!!");
        } else if (!(CoupleStatus.ACCEPTED.name().equalsIgnoreCase(wedding.getCoupleStatus().toString()))) {
            throw new WeddingException("Cannot publish wedding as spouse has not been added!!!");
        } else {
            String weddingCode = secureRandomStringGenerator.apply(10);
            wedding.setCode(weddingCode);
            wedding.setIsPublished(true);
            wedding.setPublishDate(Date.from(Instant.now()));
            wedding = weddingRepository.saveAndFlush(wedding);
            if (wedding.getWeddingType() == WeddingType.PUBLIC) {
                postService.convertWeddingToPost(wedding);
            }
            return weddingMapper.mapWeddingToDto(wedding);
        }
    }

    public WeddingDto updateWeddingInfo(WeddingDto weddingDto) {
        Long loggedInUser = getLoggedInUserId();
        if (loggedInUser == null || (!loggedInUser.equals(weddingDto.getAuthorId()) && !loggedInUser.equals(weddingDto.getSpouseId()))) {
            throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
        } else {
            Wedding wedding = weddingRepository.findById(weddingDto.getId()).orElseThrow(() -> new WeddingException("No wedding associated to this id"));
            wedding.setColourOfTheDay(weddingDto.getColourOfTheDay());
            wedding.setDateOfWedding(weddingDto.getDateOfWedding());
            wedding.setWeddingType(weddingDto.getWeddingType());
            wedding.setWeddingStory(weddingDto.getWeddingStory());
            wedding.setVenue(weddingDto.getVenue());
            wedding.setTitle(weddingDto.getTitle());
            wedding = weddingRepository.saveAndFlush(wedding);
            return weddingMapper.mapWeddingToDto(wedding);
        }
    }

    public void deleteWedding(Long id) {
        Wedding wedding = weddingRepository.findById(id).orElseThrow(() -> new WeddingException("No Such Wedding"));
        Long loggedInUser = getLoggedInUserId();
        if (wedding.getAuthorId() == null ? loggedInUser != null : !wedding.getAuthorId().equals(loggedInUser)) {
            throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
        } else {
            List<Couple> coupleList = coupleRepository.findByWedding(wedding);
            if (!coupleList.isEmpty()) {
                coupleList.forEach(couple -> {
                    coupleRepository.delete(couple);
                });
            }
            Optional<List<CoupleRequest>> coupleRequestList = coupleRequestRepository.findByWedding(wedding);
            if (coupleRequestList.isPresent()) {
                coupleRequestList.get().forEach(coupleRequest -> {
                    coupleRequestRepository.delete(coupleRequest);
                });
            }
            weddingRepository.delete(wedding);
        }
    }

    public WeddingDto updateCoverFile(WeddingDto weddingDto, MultipartFile coverFile) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void deleteCoverFile(WeddingDto weddingDto, MultipartFile coverFile) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void removeGalleryImage(WeddingDto weddingDto, MultipartFile coverFile) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
