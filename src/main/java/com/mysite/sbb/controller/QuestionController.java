package com.mysite.sbb.controller;

import com.mysite.sbb.form.AnswerForm;
import com.mysite.sbb.form.CommentForm;
import com.mysite.sbb.domain.Answer;
import com.mysite.sbb.domain.Question;
import com.mysite.sbb.domain.SiteUser;
import com.mysite.sbb.form.QuestionForm;
import com.mysite.sbb.service.QuestionService;
import com.mysite.sbb.service.UserSecurityService;
import com.mysite.sbb.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;
    private final UserSecurityService userSecurityService;
    private final UserService userService;
    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(value = "page", defaultValue ="0") int page,
                       @RequestParam(value = "keyword",defaultValue = "") String keyword,
                       Authentication authentication) {
        Page<Question> paging = questionService.getList(page,keyword);
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            SiteUser user = userService.getUser(username);
            model.addAttribute("currentUserId", user.getId());
            model.addAttribute("username", username);
        }
        model.addAttribute("paging", paging);
        model.addAttribute("keyword", keyword);
        return "question_list";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") int id,
                         AnswerForm answerForm,
                         CommentForm commentForm) {
        Question question = questionService.getQuestion(id);
        questionService.countView(question);
        model.addAttribute("question",question);
        return "question_detail";
    }

    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")//로그인이 필요한 메서드,로그아웃 상태에서 호출되면 로그인 페이지로 이동됨.
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }


    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String questionCreate(@Valid QuestionForm questionForm,
                                 BindingResult bindingResult,
                                 Model model,
                                 Principal principal) {
        SiteUser siteUser = userService.getUser(principal.getName());
        if (bindingResult.hasErrors()) {
            model.addAttribute("questionForm", questionForm);
            return "question_form";
        }
        questionService.create(questionForm.getSubject(), questionForm.getContent(),siteUser);
        return "redirect:/question/list";
    }

    @GetMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String questionModify(QuestionForm questionForm,
                                 Principal principal,
                                 @PathVariable("id") int id) {
        Question question = questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정 권한이 없습니다.");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question_form";
    }

    @PostMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String modify(@Valid QuestionForm questionForm,
                         BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정 권한이 없습니다.");
        }
        Question question = questionService.getQuestion(id);
        questionService.modify(question,questionForm.getSubject(),questionForm.getContent());
        return String.format("redirect:/question/detail/%s",id);
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(Principal principal,@PathVariable("id") int id) {
        Question question = questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제 권한이 없습니다.");
        }
        questionService.delete(question);
        return "redirect:/";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") int id) {
        Question question = questionService.getQuestion(id);
        SiteUser siteUser = userService.getUser(principal.getName());
        questionService.vote(question,siteUser);
        return String.format("redirect:/question/detail/%s", id);
    }

}
