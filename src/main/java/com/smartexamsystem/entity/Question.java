package com.smartexamsystem.entity;


import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long courseId;
    private String question;
   
    
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<Choice> choices;
   
   
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long courseId() {
		return courseId;
	}
	public void setCourseId(long courseId) {
		this.courseId = courseId;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	
	@Override
	public String toString() {
		return "Question [id=" + id + ", courseId=" + courseId + ", question=" + question + "]";
	}
	public void setChoices(List<Choice> choices) {
		this.choices = choices;
		
	}
	
	public List<Choice> getChoices() {
		return this.choices;
		
	}
	

}
