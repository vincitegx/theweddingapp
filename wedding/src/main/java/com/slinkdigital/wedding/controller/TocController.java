package com.slinkdigital.wedding.controller;

import com.slinkdigital.wedding.dto.ApiResponse;
import com.slinkdigital.wedding.dto.TableOfContentDto;
import com.slinkdigital.wedding.service.TableOfContentService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author TEGA
 */
@RestController
@RequestMapping("api/ws/v1/weddings/toc")
@RequiredArgsConstructor
public class TocController {
    
    private final TableOfContentService tableOfContentService;
    
    @PostMapping
    public ResponseEntity<ApiResponse> addTableOfContentElements(@Valid @RequestBody TableOfContentDto tableOfContentDto){
        List<TableOfContentDto> tableOfContent = tableOfContentService.addTableOfContentElements(tableOfContentDto);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("toc", tableOfContent))
                        .message("Toc Successful")
                        .status(OK)
                        .build()
        );
    }
    
    @PutMapping
    public ResponseEntity<ApiResponse> updateTableOfContentElement(@Valid @RequestBody TableOfContentDto tableOfContentDto){
        List<TableOfContentDto> tableOfContent = tableOfContentService.updateTableOfContentElements(tableOfContentDto);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("toc", tableOfContent))
                        .message("Toc Update Successful")
                        .status(OK)
                        .build()
        );
    }
    
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> removeTableOfContentElement(@PathVariable Long id){
        List<TableOfContentDto> tableOfContent = tableOfContentService.removeTableOfContentElement(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("toc", tableOfContent))
                        .message("Toc Delete Successful")
                        .status(OK)
                        .build()
        );
    }
    
    @GetMapping("{id}")
    public ResponseEntity<ApiResponse> getTableOfContents(@PathVariable Long id){
        List<TableOfContentDto> tableOfContentElements = tableOfContentService.getTableOfContentForWedding(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("toc", tableOfContentElements))
                        .message("Table Of Content Successful")
                        .status(OK)
                        .build()
        );
    }
}
