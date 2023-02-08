package com.slinkdigital.wedding.mapper.impl;

import com.slinkdigital.wedding.domain.TableOfContent;
import com.slinkdigital.wedding.dto.TableOfContentDto;
import com.slinkdigital.wedding.mapper.TocMapper;
import com.slinkdigital.wedding.mapper.WeddingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
public class TocMapperImpl implements TocMapper{
    
    private final WeddingMapper weddingMapper;

    @Override
    public TableOfContentDto mapTableToDto(TableOfContent toc) {
        return TableOfContentDto.builder()
                .id(toc.getId())
                .num(toc.getNum())
                .title(toc.getTitle())
                .wedding(weddingMapper.mapWeddingToDto(toc.getWedding()))
                .personInCharge(toc.getPersonInCharge())
                .startTime(toc.getStartTime())
                .endTime(toc.getEndTime())
                .build();
    }

    @Override
    public TableOfContent mapTableDtoToTable(TableOfContentDto toc) {
        return TableOfContent.builder()
                .id(toc.getId())
                .num(toc.getNum())
                .title(toc.getTitle())
                .wedding(weddingMapper.mapWeddingDtoToWedding(toc.getWedding()))
                .personInCharge(toc.getPersonInCharge())
                .startTime(toc.getStartTime())
                .endTime(toc.getEndTime())
                .build();
    }    
}
