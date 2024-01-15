package com.mysite.sbb.service;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.Repository.CommentRepository;
import com.mysite.sbb.domain.Answer;
import com.mysite.sbb.domain.Comment;
import com.mysite.sbb.domain.Question;
import com.mysite.sbb.domain.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;



    public Comment create(SiteUser author, String content, Answer answer){
        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setContent(content);
        comment.setAnswer(answer);
        comment.setCreateDate(LocalDateTime.now());
        commentRepository.save(comment);
        return comment;
    }







}
