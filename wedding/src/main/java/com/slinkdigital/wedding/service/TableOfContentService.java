package com.slinkdigital.wedding.service;

import com.slinkdigital.wedding.dto.TableOfContentDto;
import java.util.List;

/**
 *
 * @author TEGA
 */
public interface TableOfContentService {
    List<TableOfContentDto> getTableOfContentForWedding(Long id);

    List<TableOfContentDto> addTableOfContentElements(TableOfContentDto tableOfContentDto);

    List<TableOfContentDto> updateTableOfContentElements(TableOfContentDto tableOfContentDto);

    List<TableOfContentDto> removeTableOfContentElement(Long id);
}
