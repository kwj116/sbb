package com.mysite.sbb.controller;

import com.mysite.sbb.form.AnswerForm;
import com.mysite.sbb.form.CommentForm;
import com.mysite.sbb.domain.Answer;
import com.mysite.sbb.domain.Comment;
import com.mysite.sbb.domain.Question;
import com.mysite.sbb.domain.SiteUser;
import com.mysite.sbb.service.AnswerService;
import com.mysite.sbb.service.CommentService;
import com.mysite.sbb.service.QuestionService;
import com.mysite.sbb.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;
    private final QuestionService questionService;
    private final AnswerService answerService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createComment(Model model,
                                Principal principal,
                                @Valid CommentForm commentForm,
                                BindingResult bindingResult,
                                @PathVariable("id")int id,
                                @RequestParam("questionId") int q_id,
                                AnswerForm answerForm
                                ){
        SiteUser siteUser = userService.getUser(principal.getName());
        Answer answer = answerService.getAnswer(id);
        Question question = questionService.getQuestion(q_id);
        if (bindingResult.hasErrors()){
            model.addAttribute("question",question);
            return "question_detail";
        }
        Comment comment = commentService.create(siteUser,commentForm.getContent(),answer);
        return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
    }

}
