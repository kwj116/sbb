package com.mysite.sbb.Repository;

import com.mysite.sbb.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    @Query("select a from Answer a where a.author.id = :authorId")
    List<Answer> findByAuthorId(@Param("authorId") int id);
}
