package com.slinkdigital.wedding.mapper;

import com.slinkdigital.wedding.domain.TableOfContent;
import com.slinkdigital.wedding.dto.TableOfContentDto;
import com.slinkdigital.wedding.mapper.WeddingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
public class TocMapper {

    private final WeddingMapper weddingMapper;

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
