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
        try {
            Wedding wedding = weddingRepository.findById(id).orElseThrow(() -> new WeddingException("No Such Wedding"));
            List<TableOfContent> tableOfContent = tableOfContentRepository.findByWeddingOrderByNumAsc(wedding);
            List<TableOfContentDto> toc = tableOfContent.stream().map(t -> tocMapper.mapTableToDto(t)).collect(Collectors.toList());
            return toc;
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    public List<TableOfContentDto> addTableOfContentElements(TableOfContentDto tableOfContentDto) {
        try {
            TableOfContent tableOfContent = tocMapper.mapTableDtoToTable(tableOfContentDto);
            tableOfContent = tableOfContentRepository.save(tableOfContent);
            List<TableOfContent> tableOfContentList = tableOfContentRepository.findByWeddingOrderByNumAsc(tableOfContent.getWedding());
            List<TableOfContentDto> toc = tableOfContentList.stream().map(t -> tocMapper.mapTableToDto(t)).collect(Collectors.toList());
            return toc;
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    public List<TableOfContentDto> updateTableOfContentElements(TableOfContentDto tableOfContentDto) {
        try {
            TableOfContent tableOfContent = tableOfContentRepository.findById(tableOfContentDto.getId()).orElseThrow(() -> new WeddingException("No Table Of Content Element Found With This Id"));
            tableOfContent.setEndTime(tableOfContentDto.getEndTime());
            tableOfContent.setNum(tableOfContentDto.getNum());
            tableOfContent.setStartTime(tableOfContentDto.getStartTime());
            tableOfContent.setTitle(tableOfContentDto.getTitle());
            tableOfContent.setPersonInCharge(tableOfContentDto.getPersonInCharge());
            tableOfContent = tableOfContentRepository.saveAndFlush(tableOfContent);
            List<TableOfContent> tableOfContentList = tableOfContentRepository.findByWeddingOrderByNumAsc(tableOfContent.getWedding());
            List<TableOfContentDto> toc = tableOfContentList.stream().map(t -> tocMapper.mapTableToDto(t)).collect(Collectors.toList());
            return toc;
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    public List<TableOfContentDto> removeTableOfContentElement(Long id) {
        try {
            TableOfContent tableOfContent = tableOfContentRepository.findById(id).orElseThrow(() -> new WeddingException("No Table Of Content Element Associated with this wedding"));
            tableOfContentRepository.delete(tableOfContent);
            List<TableOfContent> tableOfContentList = tableOfContentRepository.findByWeddingOrderByNumAsc(tableOfContent.getWedding());
            List<TableOfContentDto> toc = tableOfContentList.stream().map(t -> tocMapper.mapTableToDto(t)).collect(Collectors.toList());
            return toc;
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }
}
