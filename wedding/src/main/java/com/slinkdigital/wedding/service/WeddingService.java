package com.slinkdigital.wedding.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.slinkdigital.wedding.constant.CoupleStatus;
import com.slinkdigital.wedding.constant.WeddingStatus;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
@Slf4j
public class WeddingService {

    private final WeddingRepository weddingRepository;
    private final CoupleRepository coupleRepository;
    private final CoupleRequestRepository coupleRequestRepository;
    private final SecureRandomStringGenerator secureRandomStringGenerator;
    private final HttpServletRequest request;
    private final FileService fileService;
    private final WeddingMapper weddingMapper;
    private final PostService postService;

    public Page<WeddingDto> getAllPublishedWeddings(String title, PageRequest pageRequest) {
        try {
            Page<Wedding> wedding = weddingRepository.findByIsPublishedAndWeddingTypeAndTitleContains(true, WeddingType.PUBLIC, title, pageRequest);
            Page<WeddingDto> weddingDtos = (Page) weddingMapper.mapWeddingToDto((Wedding) wedding);
            return weddingDtos;
        } catch (RuntimeException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    public Page<WeddingDto> getWeddingsWithStatus(WeddingStatus weddingStatus, String title, PageRequest pageRequest) {
        try {
            Page<Wedding> wedding = weddingRepository.findByIsPublishedAndWeddingTypeAndTitleContains(true, WeddingType.PUBLIC, title, pageRequest);

            if (null == weddingStatus) {
                throw new WeddingException("No wedding status found");
            } else {
                switch (weddingStatus) {
                    case PRESENT -> {
//                        wedding = weddingRepository.findByIsPublishedAndWeddingTypeAndTitleContainsAndDateOfWedding(Boolean.TRUE, WeddingType.PUBLIC, title, Date.from(Instant.now()), of);
                        wedding = (Page<Wedding>) wedding.stream().filter(w -> Date.from(Instant.now()).equals(w.getDateOfWedding())).limit(pageRequest.getPageSize()).toList();
//                Objects.equals(w.getDateOfWedding(),Date.from(Instant.now()))
//                new Date().equals(w.getDateOfWedding()));
                    }
                    case FUTURE -> {
                        wedding = (Page<Wedding>) wedding.stream().filter(w -> Date.from(Instant.now()).after(w.getDateOfWedding())).limit(pageRequest.getPageSize()).toList();
                    }
                    default -> {
                        wedding = (Page<Wedding>) wedding.stream().filter(w -> Date.from(Instant.now()).before(w.getDateOfWedding())).limit(pageRequest.getPageSize()).toList();
                    }
                }
            }
            Page<WeddingDto> weddingDtos = (Page) weddingMapper.mapWeddingToDto((Wedding) wedding);
            return weddingDtos;
        } catch (RuntimeException ex) {
            throw new WeddingException("No wedding status found");
        }
    }

    public Page<WeddingDto> getAllWeddings(String title, PageRequest pageRequest) {
        try {
            List<WeddingDto> w = new ArrayList<>();
            Page<WeddingDto> weddingDtos = new PageImpl<>(w);
            Page<Wedding> wedding = weddingRepository.findByTitleContains(title, pageRequest);
            if (!wedding.isEmpty()) {
                weddingDtos = (Page<WeddingDto>) weddingMapper.mapWeddingToDto((Wedding) wedding);
            }
            return weddingDtos;
        } catch (RuntimeException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    public List<WeddingDto> getAllWeddingsByUser(Long userid) {
        try {
            List<WeddingDto> weddingDtos = new ArrayList<>();
            List<Wedding> wedding = weddingRepository.findByAuthorIdOrSpouseId(userid, userid);
            if (!wedding.isEmpty()) {
                List<Wedding> w = wedding;
                w.forEach((result) -> {
                    weddingDtos.add(weddingMapper.mapWeddingToDto(result));
                });
            }
            return weddingDtos;
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

//    
//    public List<WeddingDto> getAllPublishedWeddingsByUser(Long userid) {
//        try {
//            List<WeddingDto> weddingDtos = new ArrayList<>();
//            List<Wedding> wedding = weddingRepository.findByIsPublishedAndAuthorIdOrSpouseId(true, userid, userid);
//            if (!wedding.isEmpty()) {
//                List<Wedding> w = wedding;
//                w.forEach((result) -> {
//                    weddingDtos.add(weddingMapper.mapWeddingToDto(result));
//                });
//            }
//            return weddingDtos;
//        } catch (RuntimeException ex) {
//            throw new WeddingException(ex.getMessage());
//        }
//    }
    public List<WeddingDto> getWeddingRequestsToUser() {
        try {
            String email = getLoggedInUserEmail();
            List<CoupleRequest> coupleRequest = coupleRequestRepository.findByEmail(email);
            List<WeddingDto> weddingDtos = new ArrayList<>();
            coupleRequest.forEach(req -> {
                weddingDtos.add(weddingMapper.mapWeddingToDto(req.getWedding()));
            });
            return weddingDtos;
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    public WeddingDto getWeddingById(Long id) {
        try {
            Wedding wedding = weddingRepository.findById(id).orElseThrow(() -> new WeddingException("Sorry, No Such Wedding Found !!!"));
            return weddingMapper.mapWeddingToDto(wedding);
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    public WeddingDto getWeddingByCode(String code) {
        try {
            Wedding wedding = weddingRepository.findByCode(code).orElseThrow(() -> new WeddingException("Sorry, No Such Wedding Found !!!"));
            return weddingMapper.mapWeddingToDto(wedding);
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    public WeddingDto addWeddingInfo(WeddingDto weddingDto, MultipartFile coverFile) {
        try {
            if (weddingDto.getId() == null) {
                Long loggedInUser = getLoggedInUserId();
                if (loggedInUser == null ? weddingDto.getAuthorId() != null : !loggedInUser.equals(weddingDto.getAuthorId())) {
                    throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
                } else {
                    weddingDto.setCoupleStatus(CoupleStatus.REQUEST_NOT_SENT);
                    weddingDto.setCreatedAt(LocalDateTime.now());
                    weddingDto.setPublished(false);
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
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    private Long getLoggedInUserId() {
        try {
            return Long.parseLong(request.getHeader("x-id"));
        } catch (RuntimeException ex) {
            throw new WeddingException("You Need To Be Logged In !!!");
        }
    }

    private String getLoggedInUserEmail() {
        try {
            String email = request.getHeader("x-email");
            return email;
        } catch (RuntimeException ex) {
            throw new WeddingException("You Need To Be Logged In !!!");
        }
    }

    public Map<String, String> publishWedding(Long id) throws JsonProcessingException {
        try {
            Map<String, String> result = new HashMap<>();
            Wedding wedding = weddingRepository.findById(id).orElseThrow(() -> new WeddingException("No Such Wedding"));
            Long loggedInUser = getLoggedInUserId();
            if (wedding.getAuthorId() == null ? loggedInUser != null : !wedding.getAuthorId().equals(loggedInUser)) {
                throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
            } else if (wedding.isPublished()) {
                throw new WeddingException("This wedding has already been published!!!");
            } else if (!(CoupleStatus.ACCEPTED.name().equalsIgnoreCase(wedding.getCoupleStatus().toString()))) {
                throw new WeddingException("Cannot publish wedding as spouse has not been added!!!");
            } else {
                String weddingCode = secureRandomStringGenerator.apply(10);
                wedding.setCode(weddingCode);
                wedding.setPublished(true);
                wedding.setPublishDate(Date.from(Instant.now()));
                wedding = weddingRepository.saveAndFlush(wedding);
                if (wedding.getWeddingType() == WeddingType.PUBLIC) {
                    wedding.setCreatedAt(LocalDateTime.now());
                    postService.convertWeddingToPost(wedding);
                }
                result.put("success", "This wedding has been published successfully, This is the code: " + weddingCode);
                return result;
            }
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    public WeddingDto updateWeddingInfo(WeddingDto weddingDto) {
        try {
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
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    public boolean deleteWedding(Long id) {
        try {
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
                return true;
            }
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    public WeddingDto updateCoverFile(WeddingDto weddingDto, MultipartFile coverFile) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public WeddingDto deleteCoverFile(WeddingDto weddingDto, MultipartFile coverFile) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void removeGalleryImage(WeddingDto weddingDto, MultipartFile coverFile) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
