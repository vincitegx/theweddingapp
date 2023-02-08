package com.slinkdigital.wedding.repository;

import com.slinkdigital.wedding.domain.Post;
import com.slinkdigital.wedding.domain.Wedding;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author TEGA
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
    
    List<Post> findByWedding(Wedding wedding);
    
}
