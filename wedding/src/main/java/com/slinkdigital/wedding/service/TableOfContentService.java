package com.slinkdigital.wedding.service;

import com.slinkdigital.wedding.domain.TableOfContent;
import com.slinkdigital.wedding.domain.Wedding;
import com.slinkdigital.wedding.dto.TableOfContentDto;
import com.slinkdigital.wedding.exception.WeddingException;
import com.slinkdigital.wedding.mapper.TocMapper;
import com.slinkdigital.wedding.repository.TableOfContentRepository;
import com.slinkdigital.wedding.repository.WeddingRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@AllArgsConstructor
public class TableOfContentService {

    private final TableOfContentRepository tableOfContentRepository;
    private final WeddingRepository weddingRepository;
    private final TocMapper tocMapper;

    public List<TableOfContentDto> getTableOfContentForWedding(Long id) {
        Wedding wedding = weddingRepository.findById(id).orElseThrow(() -> new WeddingException("No Such Wedding"));
        List<TableOfContent> tableOfContent = tableOfContentRepository.findByWeddingOrderByNumAsc(wedding);
        return tableOfContent.stream().map(t -> tocMapper.mapTableToDto(t)).collect(Collectors.toList());
    }

    public TableOfContentDto addTableOfContentElements(TableOfContentDto tableOfContentDto) {
        TableOfContent tableOfContent = tocMapper.mapTableDtoToTable(tableOfContentDto);
        tableOfContent = tableOfContentRepository.save(tableOfContent);
        return tocMapper.mapTableToDto(tableOfContent);
    }

    public TableOfContentDto updateTableOfContentElements(TableOfContentDto tableOfContentDto) {
        try {
            TableOfContent tableOfContent = tableOfContentRepository.findById(tableOfContentDto.getId()).orElseThrow(() -> new WeddingException("No Table Of Content Element Found With This Id"));
            tableOfContent.setEndTime(tableOfContentDto.getEndTime());
            tableOfContent.setNum(tableOfContentDto.getNum());
            tableOfContent.setStartTime(tableOfContentDto.getStartTime());
            tableOfContent.setTitle(tableOfContentDto.getTitle());
            tableOfContent.setPersonInCharge(tableOfContentDto.getPersonInCharge());
            tableOfContent = tableOfContentRepository.saveAndFlush(tableOfContent);
            return tocMapper.mapTableToDto(tableOfContent);
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    public void removeTableOfContentElement(Long id) {
        TableOfContent tableOfContent = tableOfContentRepository.findById(id).orElseThrow(() -> new WeddingException("No Table Of Content Element Associated with this wedding"));
        tableOfContentRepository.delete(tableOfContent);
    }
}
