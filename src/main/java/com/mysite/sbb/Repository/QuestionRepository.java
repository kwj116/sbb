package com.mysite.sbb.Repository;

import com.mysite.sbb.domain.Question;
import com.mysite.sbb.domain.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question,Integer> {
    Question findBySubject(String subject);
    Question findBySubjectAndContent(String subject, String content);
    List<Question> findBySubjectLike(String subject);
    Page<Question> findAll(Pageable pageable);

    @Query("select q from Question q where q.author.id = :authorId")
    List<Question> findByAuthorId(@Param("authorId") int id);
    Page<Question> findAll(Specification<Question> spec, Pageable pageable);

    //@Query는 테이블 기준이 아닌 엔티티 기준으로 작성
    @Query("select distinct q from Question q left outer join SiteUser"
    +" u1 on q.author=u1 left outer join Answer a on a.question=q"
    +" left outer join SiteUser u2 on a.author=u2 where"
    +" q.subject like %:keyword%"
    +" or q.content like %:keyword%"
    +" or u1.username like %:keyword%"
    +" or a.content like %:keyword%"
    +" or u2.username like %:keyword%")
    Page<Question> findAllByKeyword(@Param("keyword")String keyword, Pageable pageable);

    @Query("SELECT q, u FROM Question q JOIN SiteUser u ON q.author.id = u.id WHERE q.id = :questionId")
    List<Question> findAllByAuthorId(@Param("questionId") int questionId);


}
