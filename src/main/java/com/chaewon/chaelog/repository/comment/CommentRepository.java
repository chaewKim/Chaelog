package com.chaewon.chaelog.repository.comment;

import com.chaewon.chaelog.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {

}
