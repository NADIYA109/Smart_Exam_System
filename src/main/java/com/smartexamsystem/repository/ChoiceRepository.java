package com.smartexamsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartexamsystem.entity.Choice; // Import Course from the entity package, assuming it's located there

public interface ChoiceRepository extends JpaRepository<Choice, Long> {

	 List<Choice> findByQuestionId(long questionId);

    // Custom methods can be defined here if needed
}
