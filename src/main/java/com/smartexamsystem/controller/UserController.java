package com.smartexamsystem.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.smartexamsystem.entity.Choice;
import com.smartexamsystem.entity.Course;
import com.smartexamsystem.entity.ExaminationScore;
import com.smartexamsystem.entity.Level;
import com.smartexamsystem.entity.Question;
import com.smartexamsystem.repository.UserRepository;
import com.smartexamsystem.service.CourseService;
import com.smartexamsystem.service.LevelService;
import com.smartexamsystem.service.QuestionService;
import com.smartexamsystem.service.UserService;
import com.smartexamsystem.service.ExaminationService;
import com.smartexamsystem.configuration.CustomUser;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	
	@Autowired 
	private UserRepository repo;
	
	
	@Autowired
	private CourseService courseService;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private LevelService levelService;
	
	@Autowired
	private ExaminationService examinationService;
	
	  @Autowired
	    private UserService  userService;  // Inject CourseService bean
	    
	  
	  @ModelAttribute
	  private void userDetails(Model model,Principal p) {
		  
		  String username= p.getName();
		  com.smartexamsystem.entity.User user= repo.findByUsername(username);
		  
				  model.addAttribute("username", username);
	  }

	@GetMapping("/")
	public String home(Model model) {
		List<Course> courses = courseService.getAllCourse();
		model.addAttribute("courses", courses);
		return "user/home";
	}

	@GetMapping("/start-exam/course/{id}")
	public String startExam(@PathVariable("id") Long courseId,
			@RequestParam(value = "level", required = false) Long level, Model model, HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);
		String examinationId = UUID.randomUUID().toString();
		session.setAttribute("examinationId", examinationId);
		Map<Long, Long> selectedChoices = (Map<Long, Long>) session.getAttribute("selectedChoices");


		if (level == null || level < 1) {
			List<Level> levels = levelService.getAllLevelByCourseId(courseId);
			Long levelId = levels.get(0).getId();
			level = levelId; // Default level
		}
		List<Question> questions = questionService.findByCourseIdAndLevelId(courseId, level);
		Course course = courseService.getCourseById(courseId);
	

		for (Question question : questions) {
			List<Choice> choices = questionService.getChoicesByQuestionId(question.getId());
			question.setChoices(choices);
		}

		Long nextLevelId = levelService.getNextLevelId(courseId, level);
		if (nextLevelId != null) {
			Level nextLevel = levelService.getLevelById(nextLevelId);
			model.addAttribute("nextLevel", nextLevel);
		}

		Level currentLevel = levelService.getLevelById(level);
		model.addAttribute("questions", questions);
		model.addAttribute("course", course);
		model.addAttribute("currentLevel", currentLevel);

		if(selectedChoices== null) {
			selectedChoices = new HashMap<>();
		}

		// Store the question ID and selected choice ID in the map
		for (Question question : questions) {
			selectedChoices.put(question.getId(), null); // Initialize selected choice ID to null
		}

		// Store the map in the session
		session.setAttribute("selectedChoices", selectedChoices);

		return "user/course_level_questions";
	}

	@PostMapping("/update-answer")
	@ResponseBody
	public String updateAnswer(@RequestParam Long questionId, @RequestParam Long choiceId, HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);
	    // Retrieve the map of selected choices from the session
	    Map<Long, Long> selectedChoices = (Map<Long, Long>) session.getAttribute("selectedChoices");

	    // Update the selected choice for the question
	    selectedChoices.put(questionId, choiceId);

	    // Update the session attribute
	    session.setAttribute("selectedChoices", selectedChoices);

	    return "success";
	}
	
	@GetMapping("/end-exam")
	public String submitExam( @RequestParam Long course_id, HttpServletRequest request, Model model, Authentication authentication) {
		HttpSession session = request.getSession(false);
		Integer userId = ((CustomUser) authentication.getPrincipal()).getId();
		String  examinationId = (String ) session.getAttribute("examinationId");
		session.setAttribute("score", 0);
	    // Retrieve the map of selected choices from the session
	    Map<Long, Long> selectedChoices = (Map<Long, Long>) session.getAttribute("selectedChoices");

	    
	    List<Question> questions = questionService.getAllQuestionsByCourseId(course_id);

	    if (selectedChoices != null && questions != null) {
	        Integer score = 0;

	        // Calculate the score based on selected choices and correct choices
	        for (Question question : questions) {
	            Long selectedChoiceId = selectedChoices.get(question.getId());
	            if (selectedChoiceId != null) {
	                List<Choice> choices = question.getChoices();
	                for (Choice choice : choices) {
	                    if (choice.getId().equals(selectedChoiceId) && choice.isCorrect()) {
	                        score++; // Increment score if the selected choice is correct
	                        break;
	                    }
	                }
	            }
	        }

	        // Update the session with the final score
	        session.setAttribute("score", score * 10);
	        
	        ExaminationScore examScore = new ExaminationScore();
	        examScore.setExaminationId(examinationId);
	        examScore.setScore(score * 10);
	        examScore.setUserId(userId);
	        examScore.setCourseId(course_id);
	        
	        examinationService.saveExaminationScore(examScore);

	        // Redirect to a result page or any other appropriate page
	        return "user/result";
	    } else {
	        // Handle session not found or invalid data
	        return "redirect:/";
	    }
	}


	@GetMapping("/user/home")
	public String showCoursePage(Model model) {
		List<Course> courses = courseService.getAllCourse(); // Fetch courses
		model.addAttribute("courses", courses);
		return "user/home";
	}

//	 @GetMapping("/exam-instructions")
//	    public String showExamInstructionsPage() {
//	        return "user/exam-instructions";
//	    }

	@GetMapping("/edit-profile")
	public String editProfile() {

		return "user/edit-profile";
	}
//	@GetMapping("/edit-profile/{id}")
//    public String showEditProfile(@PathVariable int id, Model model) {
//        // Retrieve the course by its ID
//        com.smartexamsystem.entity.User user = userService.getUserById(id);
//       
//        // Add the course to the model
//        model.addAttribute("user", user);
//        
//        // Return the view for editing the course
//        return "user/edit_profile";
//    }
//
//    @PostMapping("/edit-profile/{id}")
//    public String editProfile(@PathVariable int id, @ModelAttribute com.smartexamsystem.entity.User user) {
//       user.setId(id);
//       userService.saveUser(user);
//        
//         return "redirect:/user/home";
//    }
	
	
	
	@GetMapping("/change_password")
	public String loadChangePassword() {
		
		return "user/change_password";
	}

	
	

	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate(); // Invalidate the session
		}
		return "redirect:/"; // Redirect to the index page after logout
	}
}