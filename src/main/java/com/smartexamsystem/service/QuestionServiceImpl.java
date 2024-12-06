package com.smartexamsystem.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartexamsystem.entity.Choice;
import com.smartexamsystem.entity.CourseLevelQuestionMap;
import com.smartexamsystem.entity.Question;
import com.smartexamsystem.entity.Course;
import com.smartexamsystem.entity.Level;
import com.smartexamsystem.repository.ChoiceRepository;
import com.smartexamsystem.repository.CourseLevelQuestionRepository;
import com.smartexamsystem.repository.QuestionRepository;
import com.smartexamsystem.repository.LevelRepository;
import com.smartexamsystem.repository.CourseRepository;

@Service
public class QuestionServiceImpl implements QuestionService {

	@Autowired
	private QuestionRepository questionRepo;

	@Autowired
	private ChoiceRepository choiceRepository;

	@Autowired
	private CourseLevelQuestionRepository courseLevelQuestionRepository;

	@Autowired
	private LevelRepository levelRepository;

	@Autowired
	private CourseRepository courseRespository;

	@Override
	public List<Question> getAllQuestionsByCourseId(long id) {

		return questionRepo.findByCourseId(id);

	}

	@Override
	public Question createQuestion(Question question) {
		Question newQuestion = questionRepo.save(question);
		return newQuestion;
	}

	public Choice createChoice(Long questionId, Choice choice, Boolean isCorrect) {
		Question question = questionRepo.findById(questionId)
				.orElseThrow(() -> new IllegalArgumentException("Question not found with id: " + questionId));
		choice.setQuestion(question);
		choice.setCorrect(isCorrect);
		return choiceRepository.save(choice);
	}

	@Override
	public List<Question> findByCourseIdAndLevelId(Long courseId, Long levelId) {
		List<CourseLevelQuestionMap> mappings = courseLevelQuestionRepository.findByCourseIdAndLevelId(courseId,
				levelId);
		return mappings.stream().map(mapping -> mapping.getQuestion()).collect(Collectors.toList());

	}

	@Override
	public Question assignQuestion(Long courseId, Long levelId, Long questionId) {

		Course course = courseRespository.findById(courseId)
				.orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + courseId));

		Level level = levelRepository.findById(levelId)
				.orElseThrow(() -> new IllegalArgumentException("Level not found with id: " + levelId));

		Question question = questionRepo.findById(questionId)
				.orElseThrow(() -> new IllegalArgumentException("Question not found with id: " + questionId));

		// Create a new mapping
		CourseLevelQuestionMap courseLevelQuestionMap = new CourseLevelQuestionMap();
		// Set the course, level, and question for the new mapping
		courseLevelQuestionMap.setCourse(course);
		courseLevelQuestionMap.setLevel(level);
		courseLevelQuestionMap.setQuestion(question);
		// Save the new mapping
		courseLevelQuestionRepository.save(courseLevelQuestionMap);

		return courseLevelQuestionMap.getQuestion();

	}
	
	@Override
	public List<Choice> getChoicesByQuestionId(Long questionId) {
		 return choiceRepository.findByQuestionId(questionId);
	}
	
	

}
