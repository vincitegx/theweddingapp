package com.slinkdigital.wedding.repository;

import com.slinkdigital.wedding.domain.Comment;
import com.slinkdigital.wedding.domain.Wedding;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author TEGA
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{
    List<Comment> findByWedding(Wedding wedding);
}
