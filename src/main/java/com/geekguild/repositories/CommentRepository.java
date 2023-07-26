package com.geekguild.repositories;

//import com.geekguild.models.Group;
import com.geekguild.models.Comments;
import com.geekguild.models.Portfolio;
import com.geekguild.models.Post;
import com.geekguild.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comments, Long> {


    List<Comments> findByPost(Post post);
}
