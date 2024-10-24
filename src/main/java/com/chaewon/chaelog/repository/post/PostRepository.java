package com.chaewon.chaelog.repository.post;

import com.chaewon.chaelog.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

}
