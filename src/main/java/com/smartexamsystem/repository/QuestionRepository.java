package com.smartexamsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartexamsystem.entity.Question; // Import Course from the entity package, assuming it's located there

public interface QuestionRepository extends JpaRepository<Question, Long> {

	
	 List<Question> findByCourseId(long courseId);

    // Custom methods can be defined here if needed
}

