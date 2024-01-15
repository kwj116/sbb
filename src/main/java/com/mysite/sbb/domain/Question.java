package com.mysite.sbb.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "Text")
    private String content;

    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    //질문 하나에 답변이 여러개 달릴 수 있으므로, OneToMany, 질문이 삭제되면 모든 답변이 삭제되도록 cascade.REMOVE
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

    @ManyToOne
    private SiteUser author;

    @ManyToMany//추천이 서로 중복되지 않도록 Set자료형 사용
    Set<SiteUser> voter;
    
    private Integer view;

}