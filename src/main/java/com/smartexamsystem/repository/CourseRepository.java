package com.smartexamsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartexamsystem.entity.Course; // Import Course from the entity package, assuming it's located there

public interface CourseRepository extends JpaRepository<Course, Long> {


    // Custom methods can be defined here if needed
}

