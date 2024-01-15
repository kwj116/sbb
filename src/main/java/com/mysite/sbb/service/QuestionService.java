package com.mysite.sbb.service;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.Repository.QuestionRepository;
import com.mysite.sbb.Repository.UserRepository;
import com.mysite.sbb.domain.Answer;
import com.mysite.sbb.domain.Question;
import com.mysite.sbb.domain.SiteUser;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.ast.tree.select.SortSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;


    public Question getQuestion(int id) {
        Optional<Question> question = questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        }
        else{
            throw new DataNotFoundException("question not found");
        }
    }

    public void create(String subject, String content, SiteUser author) {
        Question question = new Question();
        question.setCreateDate(LocalDateTime.now());
        question.setSubject(subject);
        question.setContent(content);
        question.setAuthor(author);
        question.setView(0);
        questionRepository.save(question);
    }

    public void modify(Question question,String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        questionRepository.save(question);
    }

    public void delete(Question question){
        questionRepository.delete(question);
    }

    public Page<Question> getList(int page, String keyword) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Question> specification = search(keyword);
        return questionRepository.findAllByKeyword(keyword,pageable);
    }



    public void vote(Question question, SiteUser siteUser) {
        question.getVoter().add(siteUser);
        questionRepository.save(question);
    }


    public void countView(Question question){
        question.setView(question.getView()+1);
        questionRepository.save(question);
    }

    private Specification<Question> search(String keyword) {
        return new Specification<Question>() {

            //question : Root자료형,Question객체로 질문제목과 내용을 검색하기 위함
            //u1 : Question,SiteUser를 아우터조인(author로 연결되어 있음)
            @Override
            public Predicate toPredicate(Root<Question> question, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                query.distinct(true); //중복제거
                Join<Question,SiteUser> u1 = question.join("author",JoinType.LEFT);
                Join<Question, Answer> a = question.join("answerList",JoinType.LEFT);
                Join<Answer,SiteUser> u2 = a.join("author", JoinType.LEFT);
                return criteriaBuilder.or(criteriaBuilder.like(question.get("subject"), "%" + keyword + "%"),
                        criteriaBuilder.like(question.get("content"), "%" + keyword + "%"),
                        criteriaBuilder.like(u1.get("username"), "%" + keyword + "%"),
                        criteriaBuilder.like(a.get("content"), "%" + keyword + "%"),
                        criteriaBuilder.like(u2.get("username"), "%" + keyword + "%"));
            }
        };
    }




}
