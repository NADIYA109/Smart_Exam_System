package com.smartexamsystem.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.smartexamsystem.entity.Course;
import com.smartexamsystem.entity.ExaminationScore;
import com.smartexamsystem.entity.Level;
import com.smartexamsystem.entity.Question;
import com.smartexamsystem.entity.User;
import com.smartexamsystem.repository.ExaminationRepository;
import com.smartexamsystem.repository.CourseRepository;
import com.smartexamsystem.repository.LevelRepository;
import com.smartexamsystem.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class ExaminationServiceImplement implements ExaminationService {

	@Autowired
	private ExaminationRepository examinationRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CourseRepository courseRepository;
	
	@Override
	public List<ExaminationScore> getAllExaminationScores() {
		List<ExaminationScore> examinationScores = examinationRepository.findAll();
		examinationScores.forEach(score -> {
			Optional<User> userOptional = userRepository.findById(score.getUserId());
			User user = userOptional.orElse(null);
			if(user != null) {
				score.setUserDetails(user.getUsername());
			}
			
			Optional<Course> courseOptional = courseRepository.findById(score.getCourseId());
			Course course = courseOptional.orElse(null);
			if( course != null) {
			 score.setCoursename(course.getName());
			}
		});
		return examinationScores;
	}

	@Override
	public void saveExaminationScore(ExaminationScore examinationScore) {
		examinationRepository.save(examinationScore);
	}

}
