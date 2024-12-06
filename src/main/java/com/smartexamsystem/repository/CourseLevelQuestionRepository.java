package com.smartexamsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartexamsystem.entity.CourseLevelQuestionMap;

public interface CourseLevelQuestionRepository extends JpaRepository<CourseLevelQuestionMap, Long> {

	List<CourseLevelQuestionMap> findByCourseIdAndLevelId(Long courseId, Long levelId);

}

