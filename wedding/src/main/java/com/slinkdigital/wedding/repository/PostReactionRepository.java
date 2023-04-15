package com.slinkdigital.wedding.repository;

import com.slinkdigital.wedding.domain.Post;
import com.slinkdigital.wedding.domain.PostReaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author TEGA
 */
@Repository
public interface PostReactionRepository extends JpaRepository<PostReaction, Long>{
    List<PostReaction> findByPost(Post post);
}
