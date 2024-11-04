package com.chaewon.chaelog.repository.comment;

import com.chaewon.chaelog.domain.Comment;
import com.chaewon.chaelog.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long>, CommentRepositoryCustom {
    List<Comment> findByPost(Post post);


}
