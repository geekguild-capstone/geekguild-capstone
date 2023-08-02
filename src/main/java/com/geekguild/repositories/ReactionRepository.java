package com.geekguild.repositories;

import com.geekguild.models.Comments;
import com.geekguild.models.Group;
import com.geekguild.models.Post;
import com.geekguild.models.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    int countByCommentAndReaction(Comments comment, String reactionType);

    @Query("SELECT r.comment.id, r.reaction, COUNT(r) FROM Reaction r WHERE r.comment IN :comments GROUP BY r.comment.id, r.reaction")
    List<Object[]> countReactionsForComments(@Param("comments") List<Comments> comments);


    @Query("SELECT r.post.id, r.reaction, COUNT(r) FROM Reaction r WHERE r.post IN :posts GROUP BY r.post.id, r.reaction")
    List<Object[]> countReactionsForPosts(@Param("posts") List<Post> posts);

    List<Reaction> findByPost(Post post);
}
