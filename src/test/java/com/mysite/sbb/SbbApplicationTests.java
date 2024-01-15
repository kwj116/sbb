package com.mysite.sbb;

import com.mysite.sbb.Repository.AnswerRepository;
import com.mysite.sbb.Repository.QuestionRepository;
import com.mysite.sbb.domain.Answer;
import com.mysite.sbb.domain.Question;
import com.mysite.sbb.service.QuestionService;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SbbApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private AnswerRepository answerRepository;
	@Autowired
	private QuestionService questionService;

	@Test
	void testJpa() {
		Question q = questionRepository.findBySubject("스프링부트 모델 질문입니다.");
		assertEquals(4,q.getId());
	}

	@Test
	@DisplayName("데이터 저장")
	void testSave() {
		Question q = new Question();
		q.setSubject("첫번째 질문");
		q.setContent("첫번째 질문입니다.");
		q.setCreateDate(LocalDateTime.now());
		questionRepository.save(q);
	}

	@Test
	void testFindBySubAndContent() {
		Question q = questionRepository.findBySubjectAndContent(
				"스프링부트 모델 질문입니다.","id는 자동으로 생성되나요?");
		assertEquals(3,q.getId());
	}

	@Test
	void testFindBySubLike() {
		List<Question> qlist = questionRepository.findBySubjectLike("스%");//배열
		Question q = qlist.get(0);//배열안의 첫번째 요소
		assertEquals("스프링부트 모델 질문입니다.", q.getSubject());
		System.out.println("q = " + q);
		System.out.println("qlist = " + qlist);
	}

	@Test
	@DisplayName("제목 수정하기")
	void modifySub() {
		Optional<Question> oq = questionRepository.findById(4);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		q.setSubject("수정된 제목");
		questionRepository.save(q);

		System.out.println("q = " + q);
		System.out.println("qlist = " + oq);
	}

	@Test
	@DisplayName("데이터 삭제")
	void testDel() {
		Optional<Question> oq = questionRepository.findById(4);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		questionRepository.delete(q);
		assertEquals(0,questionRepository.count());
	}

	@Test
	@DisplayName("답변 데이터 생성 후 저장")
	void testCreateAndSave() {
		Optional<Question> oq = questionRepository.findById(4);
		assertTrue(oq.isPresent());
		Question q = oq.get();

		Answer answer = new Answer();
		answer.setContent("첫번째 답변입니다.");
		answer.setQuestion(q);
		answer.setCreateDate(LocalDateTime.now());
		answerRepository.save(answer);
	}

	@Test
	@DisplayName("답변 조회")
	void testAnswer() {
		Optional<Answer> oa = answerRepository.findById(1);
		assertTrue(oa.isPresent());
		Answer a = oa.get();
		assertEquals(4,a.getQuestion().getId());
	}

	@Test
	@DisplayName("답변에 연결된 질문 찾기, 질문에 달린 답변 찾기")
	@Transactional
	void testLink() {
		Optional<Question> oq = questionRepository.findById(4);
		assertTrue(oq.isPresent());
		Question q = oq.get();

		List<Answer> answerList = q.getAnswerList();

		assertEquals(1,answerList.size());
		assertEquals("첫번째 답변입니다.", answerList.get(0).getContent());

	}

	@Test
	void createDummy() {
		for (int i = 1; i <= 300; i++) {
			String subject = String.format("테스트 글:(%03d)",i);
			String content = "...";
			questionService.create(subject,content,null);
		}
	}
}
