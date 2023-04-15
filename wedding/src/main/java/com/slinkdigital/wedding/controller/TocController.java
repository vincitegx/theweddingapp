package com.slinkdigital.wedding.controller;

import com.slinkdigital.wedding.dto.TableOfContentDto;
import com.slinkdigital.wedding.service.TableOfContentService;
import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public TableOfContentDto addTableOfContentElements(@Valid @RequestBody TableOfContentDto tableOfContentDto){
        return tableOfContentService.addTableOfContentElements(tableOfContentDto);
    }
    
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public TableOfContentDto updateTableOfContentElement(@Valid @RequestBody TableOfContentDto tableOfContentDto){
        return tableOfContentService.updateTableOfContentElements(tableOfContentDto);
    }
    
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeTableOfContentElement(@PathVariable Long id){
        tableOfContentService.removeTableOfContentElement(id);
    }
    
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<TableOfContentDto> getTableOfContents(@PathVariable Long id){
        return tableOfContentService.getTableOfContentForWedding(id);
    }
}
