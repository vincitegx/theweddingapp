package com.slinkdigital.wedding.repository;

import com.slinkdigital.wedding.domain.TableOfContent;
import com.slinkdigital.wedding.domain.Wedding;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author TEGA
 */
@Repository
public interface TableOfContentRepository extends JpaRepository<TableOfContent, Long>{
    List<TableOfContent> findByWeddingOrderByNumAsc(Wedding wedding);
}
