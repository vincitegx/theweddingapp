package com.slinkdigital.wedding.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.slinkdigital.wedding.dto.ApiResponse;
import com.slinkdigital.wedding.dto.WeddingDto;
import com.slinkdigital.wedding.exception.WeddingException;
import com.slinkdigital.wedding.mapper.WeddingMapper;
import com.slinkdigital.wedding.service.WeddingService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.validation.Valid;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author TEGA
 */
@RestController
@RequestMapping("api/ws/v1/weddings")
@RequiredArgsConstructor
@Slf4j
public class WeddingController {

    private final WeddingService weddingService;
    private final WeddingMapper weddingMapper;

    @GetMapping //admin : view
    public ResponseEntity<ApiResponse> getAllWeddings(
            @RequestParam Optional<String> search,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> length,
            @RequestParam Optional<String> sortBy
    ){
        Sort.Direction direction = Sort.Direction.ASC;
        Page<WeddingDto> weddings = weddingService.getAllWeddings(
        search.orElse(""),
                PageRequest.of(page.orElse(0), length.orElse(8), direction, "id")
        );
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("weddings", weddings))
                        .message("Weddings Displayed Successfully")
                        .status(OK)
                        .build()
        );
    }
    
    @GetMapping("users/{userId}")
    public ResponseEntity<ApiResponse> getWeddingsForUser(@PathVariable(value = "userId") Long userid) {
        List<WeddingDto> weddings = weddingService.getAllWeddingsByUser(userid);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("weddings", weddings))
                        .message("List Of Wedding Successful")
                        .status(OK)
                        .build()
        );
    }

    @GetMapping("user/requests")
    public ResponseEntity<ApiResponse> getWeddingRequestsToUser() {
        List<WeddingDto> weddings = weddingService.getWeddingRequestsToUser();
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("weddings", weddings))
                        .message("List Of Wedding Successful")
                        .status(OK)
                        .build()
        );
    }

    @PostMapping(path = "info", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse> addWeddingInfo(@RequestParam("weddingDto") String weddingDto, @RequestParam("coverFile") Optional<MultipartFile> coverFile) throws IOException {
        WeddingDto wedding = weddingMapper.getJson(weddingDto);
        wedding = weddingService.addWeddingInfo(wedding, coverFile.orElse(null));
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("wedding", wedding))
                        .message("Addition of wedding info successful")
                        .status(CREATED)
                        .build()
        );
    }

    @PostMapping("{id}/publish")
    public ResponseEntity<ApiResponse> publishWedding(@PathVariable Long id) {
        try {
            Map<String, String> publishStatus = weddingService.publishWedding(id);
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("isWeddingPublished", true))
                            .message(publishStatus.get("success"))
                            .status(OK)
                            .build()
            );
        } catch (JsonProcessingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    @PutMapping("info")
    public ResponseEntity<ApiResponse> updateWeddingInfo(@Valid @RequestBody WeddingDto weddingDto) {
        weddingDto = weddingService.updateWeddingInfo(weddingDto);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("weddingDto", weddingDto))
                        .message("Wedding Info Updated Successfully !!!")
                        .status(OK)
                        .build()
        );
    }

    @PutMapping("media")
    public ResponseEntity<ApiResponse> updateCoverFile(@RequestParam("weddingDto") String weddingDto, @RequestParam("coverFile") MultipartFile coverFile) {
        WeddingDto wedding = weddingMapper.getJson(weddingDto);
        wedding = weddingService.updateCoverFile(wedding, coverFile);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("weddingMedia", wedding))
                        .message("Wedding Media Updated Successfully !!!")
                        .status(OK)
                        .build()
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> deleteWedding(@PathVariable Long id) {
        boolean deleteStatus = weddingService.deleteWedding(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("isWeddingPublished", deleteStatus))
                        .message("Wedding deleted successfully !!!")
                        .status(OK)
                        .build()
        );
    }
}
