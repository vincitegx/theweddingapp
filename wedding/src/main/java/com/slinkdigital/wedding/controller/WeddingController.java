package com.slinkdigital.wedding.controller;

import com.slinkdigital.wedding.dto.WeddingDto;
import com.slinkdigital.wedding.mapper.WeddingMapper;
import com.slinkdigital.wedding.service.WeddingService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
@Slf4j
public class WeddingController {

    private final WeddingService weddingService;
    private final WeddingMapper weddingMapper;

    @GetMapping //admin : view
    @ResponseStatus(HttpStatus.OK)
    public Page<WeddingDto> getAllWeddings(@RequestParam Optional<String> search,Pageable pageable){
        return weddingService.getAllWeddings(search.orElse(""), pageable);
    }
    
//    @PreAuthorize("@myAuthorizationService.hasRoleCouple(#roles)")
    @GetMapping("users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<WeddingDto> getWeddingsForUser(@PathVariable(value = "userId") Long userid, @RequestHeader("x-roles") List<String> roles) {
        return weddingService.getAllWeddingsByUser(userid);
    }

    @GetMapping("user/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<WeddingDto> getWeddingRequestsToUser() {
        return weddingService.getWeddingRequestsToUser();
    }

    @PostMapping(path = "info", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public WeddingDto addWeddingInfo(@RequestParam("weddingDto") String weddingDto, @RequestParam("coverFile") Optional<MultipartFile> coverFile) throws IOException {
        WeddingDto wedding = weddingMapper.getJson(weddingDto);
        return weddingService.addWeddingInfo(wedding, coverFile.orElse(null));
    }

    @PostMapping("{id}/publish")
    @ResponseStatus(HttpStatus.OK)
    public WeddingDto publishWedding(@PathVariable Long id) {
        return weddingService.publishWedding(id);
    }

    @PutMapping("info")
    @ResponseStatus(HttpStatus.OK)
    public WeddingDto updateWeddingInfo(@Valid @RequestBody WeddingDto weddingDto) {
        return weddingService.updateWeddingInfo(weddingDto);
    }

    @PutMapping("media")
    @ResponseStatus(HttpStatus.OK)
    public WeddingDto updateCoverFile(@RequestParam("weddingDto") String weddingDto, @RequestParam("coverFile") MultipartFile coverFile) {
        WeddingDto wedding = weddingMapper.getJson(weddingDto);
        return weddingService.updateCoverFile(wedding, coverFile);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWedding(@PathVariable Long id) {
        weddingService.deleteWedding(id);
    }
}
