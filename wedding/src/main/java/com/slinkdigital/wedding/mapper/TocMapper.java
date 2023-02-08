package com.slinkdigital.wedding.mapper;

import com.slinkdigital.wedding.domain.TableOfContent;
import com.slinkdigital.wedding.dto.TableOfContentDto;

/**
 *
 * @author TEGA
 */
public interface TocMapper {
    
    TableOfContentDto mapTableToDto(TableOfContent toc);
    
    TableOfContent mapTableDtoToTable(TableOfContentDto toc);
}
