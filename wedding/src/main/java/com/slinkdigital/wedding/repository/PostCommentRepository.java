package com.slinkdigital.wedding.repository;

import com.slinkdigital.wedding.domain.Post;
import com.slinkdigital.wedding.domain.PostComment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author TEGA
 */
@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long>{
    
    List<PostComment> findByPost(Post post);
    
}
