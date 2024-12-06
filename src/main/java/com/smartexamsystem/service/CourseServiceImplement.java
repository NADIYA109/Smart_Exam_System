package com.smartexamsystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.smartexamsystem.entity.Course;
import com.smartexamsystem.entity.Level;
import com.smartexamsystem.entity.Question;
import com.smartexamsystem.repository.CourseLevelQuestionRepository;
import com.smartexamsystem.repository.CourseRepository;
import com.smartexamsystem.repository.LevelRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class CourseServiceImplement implements CourseService {

	@Autowired
	private CourseRepository courseRepo;

	@Autowired
	private LevelRepository levelRepository;

	@Override
	public Course saveCourse(Course course) {
		Course newCourse = courseRepo.save(course);
		return newCourse;
	}

	@Override
	public List<Course> getAllCourse() {

		return courseRepo.findAll();
	}

	@Override
	public Course getCourseById(long id) {

		return courseRepo.findById(id).get();
	}

	@Override
	public boolean deleteCourse(long id) {

		Course course = courseRepo.findById(id).get();
		if (course != null) {
			courseRepo.delete(course);
			return true;
		}
		return false;
	}

	public Integer getTotalLevels(long courseId) {

		List<Level> levels = levelRepository.findByCourseId(courseId);
		return levels.size();

	}

	public void removeSessionMessage() {
		HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest()
				.getSession();

		session.removeAttribute("msg");

	}

}
