package com.slinkdigital.wedding.controller;

import com.slinkdigital.wedding.constant.WeddingStatus;
import com.slinkdigital.wedding.dto.WeddingDto;
import com.slinkdigital.wedding.service.WeddingService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author TEGA
 */
@RestController
@RequestMapping("api/wu/v1/weddings")
@RequiredArgsConstructor
public class WeddingController1 {
    
    private final WeddingService weddingService;
    
    @GetMapping("/web/{code}")
    @ResponseStatus(HttpStatus.OK)
    public WeddingDto getWeddingByCode(@PathVariable String code){
        return weddingService.getWeddingByCode(code);
    }
    
    @GetMapping("{id}") //profile
    @ResponseStatus(HttpStatus.OK)
    public WeddingDto getWeddingById(@PathVariable Long id) {
        return weddingService.getWeddingById(id);
    }
        
    @GetMapping // home page display
    @ResponseStatus(HttpStatus.OK)
    public Page<WeddingDto> getAllPublishedWeddings(@RequestParam Optional<String> search,
            @RequestParam Optional<String> sortBy,Pageable pageable){
        return weddingService.getAllPublishedWeddings(
                sortBy.orElse(WeddingStatus.FUTURE.name()),
                search.orElse(""),
                pageable
        );
    }
}
