package com.slinkdigital.wedding.controller;

import com.slinkdigital.wedding.dto.CoupleDto;
import com.slinkdigital.wedding.dto.SpouseRequest;
import com.slinkdigital.wedding.mapper.CoupleMapper;
import com.slinkdigital.wedding.service.CoupleService;
import java.util.Set;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    @ResponseStatus(HttpStatus.OK)
    public Set<CoupleDto> getWeddingCouples(@PathVariable(value = "weddingId") Long id) {
        return coupleService.getWeddingCouple(id);
    }

    @PostMapping(path = "couple/author", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public CoupleDto addCoupleAuthor(@RequestParam("coupleDto") String coupleDto,
            @RequestParam("file") MultipartFile file) {
        CoupleDto couple = coupleMapper.getJson(coupleDto);
        return coupleService.addCoupleAuthorInfo(couple, file);
    }

    @PostMapping(path = "couple/spouse", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public CoupleDto addCoupleSpouse(@RequestParam("coupleDto") String coupleDto,
            @RequestParam("file") MultipartFile file) {
        CoupleDto couple = coupleMapper.getJson(coupleDto);
        return coupleService.addCoupleSpouseInfo(couple, file);
    }

    @PutMapping(path = "couple/author")
    @ResponseStatus(HttpStatus.OK)
    public CoupleDto updateCoupleAuthor(@RequestBody CoupleDto coupleDto) {
        return coupleService.updateCoupleAuthorInfo(coupleDto);
    }

    @PutMapping(path = "couple/author-image", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public CoupleDto updateCoupleAuthorImage(@RequestParam("coupleDto") String coupleDto,
            @RequestParam("file") MultipartFile file) {
        CoupleDto couple = coupleMapper.getJson(coupleDto);
        return coupleService.updateCoupleAuthorImage(couple, file);
    }

    @PutMapping(path = "couple/spouse")
    @ResponseStatus(HttpStatus.OK)
    public CoupleDto updateCoupleSpouse(@RequestParam("coupleDto") String coupleDto) {
        CoupleDto couple = coupleMapper.getJson(coupleDto);
        return coupleService.updateCoupleSpouseInfo(couple);
    }

    @PostMapping(path = "couple/spouse-image", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public CoupleDto updateCoupleSpouseImage(@RequestParam("coupleDto") String coupleDto,
            @RequestParam("file") MultipartFile file) {
        CoupleDto couple = coupleMapper.getJson(coupleDto);
        return coupleService.updateCoupleSpouseImage(couple, file);
    }

    @PostMapping("couple/request")
    @ResponseStatus(HttpStatus.OK)
    public void sendCoupleRequest(@Valid @RequestBody SpouseRequest emailRequest) {
        coupleService.sendCoupleRequest(emailRequest);
    }

    @DeleteMapping("couple/request")
    @ResponseStatus(HttpStatus.OK)
    public void removeCoupleRequest(@Valid @RequestBody SpouseRequest emailRequest) {
        coupleService.removeCoupleRequest(emailRequest);
    }

    @DeleteMapping("couple/spouse")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCoupleSpouse(@Valid @RequestBody CoupleDto coupleDto) {
        coupleService.removeCoupleSpouse(coupleDto);
    }

    @GetMapping("couple/request")
    @ResponseStatus(HttpStatus.OK)
    public CoupleDto viewAuthorRequest(@RequestParam String requestToken) {
        return coupleService.viewAuthorRequest(requestToken);
    }

    @PutMapping("couple/accept-request")
    @ResponseStatus(HttpStatus.OK)
    public void acceptAuthorRequest(@RequestParam String requestToken) {
        coupleService.acceptAuthorRequest(requestToken);
    }

    @PutMapping("couple/reject-request")
    @ResponseStatus(HttpStatus.OK)
    public void rejectAuthorRequest(@RequestParam String requestToken) {
        coupleService.rejectAuthorRequest(requestToken);
    }
}
