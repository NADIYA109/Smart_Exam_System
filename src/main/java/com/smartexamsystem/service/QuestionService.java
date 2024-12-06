package com.smartexamsystem.service;

import java.util.List;


import com.smartexamsystem.entity.Choice;
import com.smartexamsystem.entity.Course;
import com.smartexamsystem.entity.Question;

public interface QuestionService {


	public List<Question> getAllQuestionsByCourseId(long id);
	
	public Question createQuestion(Question question);
	
	public Choice createChoice(Long questionId, Choice choice, Boolean isCorrect);
	
    List<Question> findByCourseIdAndLevelId(Long courseId, Long levelId);

    Question assignQuestion(Long courseId, Long levelId, Long questionId);

    public List<Choice> getChoicesByQuestionId(Long questionId);

}
