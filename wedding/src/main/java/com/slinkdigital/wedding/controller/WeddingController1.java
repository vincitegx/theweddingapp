package com.slinkdigital.wedding.controller;

import com.slinkdigital.wedding.constant.WeddingStatus;
import com.slinkdigital.wedding.dto.ApiResponse;
import com.slinkdigital.wedding.dto.WeddingDto;
import com.slinkdigital.wedding.service.WeddingService;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<ApiResponse> getWeddingByCode(@PathVariable String code){
        WeddingDto wedding = weddingService.getWeddingByCode(code);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("wedding", wedding))
                        .message("Wedding Displayed Successfully")
                        .status(OK)
                        .build()
        );
    }
    
    @GetMapping("{id}") //profile
    public ResponseEntity<ApiResponse> getWeddingById(@PathVariable Long id) {
        WeddingDto wedding = weddingService.getWeddingById(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("wedding", wedding))
                        .message("Wedding Successful")
                        .status(OK)
                        .build()
        );
    }
        
    @GetMapping("published/future") // home page display
    public ResponseEntity<ApiResponse> getFutureWeddings(@RequestParam Optional<String> search,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> length
            ,@RequestParam Optional<String> sortBy
    ){
        Sort.Direction direction = Sort.Direction.ASC;
        Page<WeddingDto> weddings = weddingService.getWeddingsWithStatus(
                WeddingStatus.FUTURE,
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
    
    @GetMapping("published/present")
    public ResponseEntity<ApiResponse> getPresentWeddings(@RequestParam Optional<String> search,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> length,
            @RequestParam Optional<String> sortBy){
        Sort.Direction direction = Sort.Direction.ASC;
        Page<WeddingDto> weddings = weddingService.getWeddingsWithStatus(
                WeddingStatus.PRESENT,
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
    
    @GetMapping("published/past")
    public ResponseEntity<ApiResponse> getPastWeddings(@RequestParam Optional<String> search,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> length,
            @RequestParam Optional<String> sortBy){
        Sort.Direction direction = Sort.Direction.ASC;
        Page<WeddingDto> weddings = weddingService.getWeddingsWithStatus(
                WeddingStatus.PAST,
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
    
    @GetMapping("published/public")
    public ResponseEntity<ApiResponse> allPublished(@RequestParam Optional<String> search,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> length,
            @RequestParam Optional<String> sortBy){
        Sort.Direction direction = Sort.Direction.ASC;
        Page<WeddingDto> weddings = weddingService.getAllPublishedWeddings(
                search.orElse(""),
                PageRequest.of(page.orElse(0), length.orElse(8), direction, sortBy.orElse("id"))
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
}
