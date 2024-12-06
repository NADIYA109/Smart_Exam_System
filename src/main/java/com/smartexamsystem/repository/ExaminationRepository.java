package com.smartexamsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartexamsystem.entity.Choice; // Import Course from the entity package, assuming it's located there
import com.smartexamsystem.entity.ExaminationScore;

public interface ExaminationRepository extends JpaRepository<ExaminationScore, Long> {

	  List<ExaminationScore> findByUserId(Long userId);
    // Custom methods can be defined here if needed
}
