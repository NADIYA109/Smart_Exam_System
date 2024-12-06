package com.smartexamsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartexamsystem.entity.Level; // Import Course from the entity package, assuming it's located there

public interface LevelRepository extends JpaRepository<Level, Long> {

	
	 List<Level> findByCourseId(long courseId);

    // Custom methods can be defined here if needed
}


