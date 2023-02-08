package com.slinkdigital.wedding.controller;

import com.slinkdigital.wedding.dto.ApiResponse;
import com.slinkdigital.wedding.dto.CoupleDto;
import com.slinkdigital.wedding.dto.SpouseRequest;
import com.slinkdigital.wedding.mapper.CoupleMapper;
import com.slinkdigital.wedding.service.CoupleService;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class CoupleController {

    private final CoupleService coupleService;
    private final CoupleMapper coupleMapper;

    @GetMapping("{weddingId}/couple")
    public ResponseEntity<ApiResponse> getWeddingCouples(@PathVariable(value = "weddingId") Long id) {
        Set<CoupleDto> couples = coupleService.getWeddingCouple(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("couple", couples))
                        .message("List of Couple Successful")
                        .status(OK)
                        .build()
        );
    }

    @PostMapping(path = "couple/author", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse> addCoupleAuthor(@RequestParam("coupleDto") String coupleDto,
            @RequestParam("file") MultipartFile file) {
        CoupleDto couple = coupleMapper.getJson(coupleDto);
        couple = coupleService.addCoupleAuthorInfo(couple, file);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("coupleAuthor", couple))
                        .message("Couple Author Added Successfully")
                        .status(OK)
                        .build()
        );
    }

    @PostMapping(path = "couple/spouse", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse> addCoupleSpouse(@RequestParam("coupleDto") String coupleDto,
            @RequestParam("file") MultipartFile file) {
        CoupleDto couple = coupleMapper.getJson(coupleDto);
        couple = coupleService.addCoupleSpouseInfo(couple, file);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("coupleSpouse", couple))
                        .message("Couple Spouse Added Successfully")
                        .status(OK)
                        .build()
        );
    }

    @PutMapping(path = "couple/author")
    public ResponseEntity<ApiResponse> updateCoupleAuthor(@RequestBody CoupleDto coupleDto) {
        coupleDto = coupleService.updateCoupleAuthorInfo(coupleDto);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("coupleAuthor", coupleDto))
                        .message("Update Successful")
                        .status(OK)
                        .build()
        );
    }

    @PutMapping(path = "couple/author-image", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse> updateCoupleAuthorImage(@RequestParam("coupleDto") String coupleDto,
            @RequestParam("file") MultipartFile file) {
        CoupleDto couple = coupleMapper.getJson(coupleDto);
        couple = coupleService.updateCoupleAuthorImage(couple, file);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("coupleAuthor", couple))
                        .message("Image Update Successful")
                        .status(OK)
                        .build()
        );
    }

    @PutMapping(path = "couple/spouse")
    public ResponseEntity<ApiResponse> updateCoupleSpouse(@RequestParam("coupleDto") String coupleDto) {
        CoupleDto couple = coupleMapper.getJson(coupleDto);
        couple = coupleService.updateCoupleSpouseInfo(couple);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("coupleSpouse", couple))
                        .message("Update Successful")
                        .status(OK)
                        .build()
        );
    }

    @PostMapping(path = "couple/spouse-image", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse> updateCoupleSpouseImage(@RequestParam("coupleDto") String coupleDto,
            @RequestParam("file") MultipartFile file) {
        CoupleDto couple = coupleMapper.getJson(coupleDto);
        couple = coupleService.updateCoupleSpouseImage(couple, file);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("coupleAuthor", couple))
                        .message("Update Successful")
                        .status(OK)
                        .build()
        );
    }

    @PostMapping("couple/request")
    public ResponseEntity<ApiResponse> sendCoupleRequest(@Valid @RequestBody SpouseRequest emailRequest) {
        Map<String, String> coupleRequestStatus = coupleService.sendCoupleRequest(emailRequest);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("isCoupleRequestSent", true))
                        .message(coupleRequestStatus.get("success"))
                        .status(OK)
                        .build()
        );
    }

    @DeleteMapping("couple/request")
    public ResponseEntity<ApiResponse> removeCoupleRequest(@Valid @RequestBody SpouseRequest emailRequest) {
        Map<String, String> coupleRequestStatus = coupleService.removeCoupleRequest(emailRequest);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("isCoupleRequestRemoved", true))
                        .message(coupleRequestStatus.get("success"))
                        .status(OK)
                        .build()
        );
    }

    @DeleteMapping("couple/spouse")
    public ResponseEntity<ApiResponse> removeCoupleSpouse(@Valid @RequestBody CoupleDto coupleDto) {
        Map<String, String> coupleSpouseRemovalStatus = coupleService.removeCoupleSpouse(coupleDto);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("isCoupleSpouseRemoved", true))
                        .message(coupleSpouseRemovalStatus.get("success"))
                        .status(OK)
                        .build()
        );
    }

    @GetMapping("couple/request")
    public ResponseEntity<ApiResponse> viewAuthorRequest(@RequestParam String requestToken) {
        CoupleDto couple = coupleService.viewAuthorRequest(requestToken);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("couple", couple))
                        .message("Couple Request Successful")
                        .status(OK)
                        .build()
        );
    }

    @PutMapping("couple/accept-request")
    public ResponseEntity<ApiResponse> acceptAuthorRequest(@RequestParam String requestToken) {
        coupleService.acceptAuthorRequest(requestToken);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("isCoupleRequestAccepted", true))
                        .message("Couple Request Accepted")
                        .status(OK)
                        .build()
        );
    }

    @PutMapping("couple/reject-request")
    public ResponseEntity<ApiResponse> rejectAuthorRequest(@RequestParam String requestToken) {
        coupleService.rejectAuthorRequest(requestToken);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("isCoupleRequestRejected", true))
                        .message("Couple Request Rejected")
                        .status(OK)
                        .build()
        );
    }
}
