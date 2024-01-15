package com.mysite.sbb.Repository;

import com.mysite.sbb.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
}
