package com.smartexamsystem.service;

import java.util.List;

import com.smartexamsystem.entity.Course;
import com.smartexamsystem.entity.Question;

public interface CourseService {


	public  Course saveCourse(Course course);

	public List<Course> getAllCourse();

	public Course getCourseById(long id);

	public boolean deleteCourse(long id);
	
	public Integer getTotalLevels(long courseId);
}
