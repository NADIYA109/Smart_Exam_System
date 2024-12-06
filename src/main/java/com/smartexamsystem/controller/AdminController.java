package com.smartexamsystem.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smartexamsystem.entity.Choice;
import com.smartexamsystem.entity.Course;
import com.smartexamsystem.entity.ExaminationScore;
import com.smartexamsystem.entity.Level;
import com.smartexamsystem.entity.Question;
import com.smartexamsystem.entity.QuestionForm;
import com.smartexamsystem.entity.User;
import com.smartexamsystem.service.CourseService;
import com.smartexamsystem.service.ExaminationService;
import com.smartexamsystem.service.LevelService;
import com.smartexamsystem.service.QuestionService;
import com.smartexamsystem.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CourseService courseService;  // Inject CourseService bean
    
    @Autowired
    private UserService  userService;  // Inject CourseService bean
    
    
    @Autowired
	private QuestionService questionService;

	@Autowired
	private LevelService levelService;
	
	@Autowired
	private ExaminationService examinationService;

    

    @GetMapping("/")
    public String home() {
        return "admin/home";
    }

    @GetMapping("/courses")
    public String viewAllCourses(Model model) {
        List<Course> courses = courseService.getAllCourse();  // Fetch courses
        model.addAttribute("courses", courses);  // Add courses to model
        return "admin/courses";
    }

    @GetMapping("/courses/add")
    public String showAddCourseForm(Model model) {
        model.addAttribute("course", new Course());  // Add an empty Course object
        return "admin/add_course";
    }


    @PostMapping("/courses/add")
    public String addCourse(@ModelAttribute Course course, HttpServletRequest request) {
        courseService.saveCourse(course);

        HttpSession session = request.getSession(false);
        if (session != null) {
            if (course != null) {
                session.setAttribute("msg", "Course added successfully");
            } else {
                session.setAttribute("msg", "Something went wrong on the server");
            }
        }

        return "redirect:/admin/courses";
    }
    @GetMapping("/courses/edit/{id}")
    public String showEditCourseForm(@PathVariable long id, Model model) {
        // Retrieve the course by its ID
        com.smartexamsystem.entity.Course course = courseService.getCourseById(id);
       
        // Add the course to the model
        model.addAttribute("course", course);
        
        // Return the view for editing the course
        return "admin/edit_course";
    }

    @PostMapping("/courses/edit/{id}")
    public String editCourse(@PathVariable long id, @ModelAttribute com.smartexamsystem.entity.Course course) {
        // Set the ID of the course to match the ID in the URL path
        course.setId(id);
        
        // Save the edited course
        courseService.saveCourse(course);
        
     
        
        // Redirect the user back to the course list page
        return "redirect:/admin/courses";
    }

//
//@GetMapping("/courses/delete/{id}")
//public String deleteCourse(@PathVariable long id ) {
////    boolean f= courseService.deleteCourse(id);
////    	if(f) {
////    		session.setAttribute("msg", "Delete Succesfully");
////    	}else {
////    		session.setAttribute("msg", "something wrong on server");
////    	}
//    
//    return "redirect:/admin/courses";
//}

    @GetMapping("/courses/delete/{id}")
    public String deleteCourse(@PathVariable long id, HttpSession session) {
        boolean isDeleted = courseService.deleteCourse(id);
        
        if (isDeleted) {
            session.setAttribute("msg", "Course deleted successfully");
        } else {
            session.setAttribute("msg", "Something went wrong on the server");
        }
        
        return "redirect:/admin/courses";
    }



    
    @GetMapping("/manage-users")
    public String showManageUsers(Model model) {
        List<User> users = userService.getAllUser();
        model.addAttribute("userList", users);
        return "admin/manage-users";
    }
    
    @GetMapping("/manage-users/edit/{id}")
    public String showEditUserForm(@PathVariable int id, Model model) {
        // Retrieve the course by its ID
        com.smartexamsystem.entity.User user = userService.getUserById(id);
       
        // Add the course to the model
        model.addAttribute("user", user);
        
        // Return the view for editing the course
        return "admin/edit_user";
    }

    @PostMapping("/manage-users/edit/{id}")
    public String editUser(@PathVariable int id, @ModelAttribute com.smartexamsystem.entity.User user) {
       user.setId(id);
       userService.saveUser(user);
        
         return "redirect:/admin/manage-users";
    }

    
    @GetMapping("/manage-users/delete/{id}")
    public String deleteUser(@PathVariable int id, HttpSession session) {
        boolean isDeleted = userService.deleteUser(id);
        
        if (isDeleted) {
            session.setAttribute("msg", "User deleted successfully");
        } else {
            session.setAttribute("msg", "Something went wrong on the server");
        }
        
        return "redirect:/admin/manage-users";
    }

//    @GetMapping("/manage-users/add")
//    public String showAddUserForm() {
////        model.addAttribute("course", new Course());  // Add an empty Course object
//        return "admin/add_user";
//    }
//    
    
    
    

@GetMapping("/profile")
public String adminProfile() {

    return "admin/profile";
}


@GetMapping("/levels")
public String showLevelsPage(Model model) {
	List<Course> courses = courseService.getAllCourse(); // Fetch courses
	model.addAttribute("courses", courses); // Add courses to model
	return "admin/levels";
}

@GetMapping("/levels/course/{id}")
public String showLevelsPageByCourseId(@PathVariable long id, Model model) {
	List<Course> courses = courseService.getAllCourse(); // Fetch courses
	model.addAttribute("courses", courses); // Add courses to model

	List<Level> levels = levelService.getAllLevelByCourseId(id); // Fetch courses
	model.addAttribute("levels", levels); // Add courses to model

	model.addAttribute("selectedCourseId", id);

	return "admin/levels";
}

@GetMapping("/levels/course/{id}/addLevel")
public String addlevelPage(@PathVariable long id, Model model) {
	model.addAttribute("selectedCourseId", id);
	Course course = courseService.getCourseById(id);
	model.addAttribute("selectedCourseName", course.getName());
	return "admin/add_level";
}

@PostMapping("/levels/course/{id}/addLevel")
public String createLevel(@PathVariable long id, Level level) {
	Course course = courseService.getCourseById(id);
	Level newLevel = new Level();
	newLevel.setCourse(course);
	newLevel.setName(level.getName());
	levelService.createLevel(newLevel);

	return "redirect:/admin/levels/course/" + id;
}

@GetMapping("/questions")
public String showManageQuestions(Model model) {
	List<Course> courses = courseService.getAllCourse(); // Fetch courses
	model.addAttribute("courses", courses); // Add courses to model
	return "admin/questions";
}

@GetMapping("/questions/course/{id}")
public String showManageQuestions(@PathVariable long id, Model model) {
	List<Course> courses = courseService.getAllCourse(); // Fetch courses
	model.addAttribute("courses", courses); // Add courses to model

	List<Question> questions = questionService.getAllQuestionsByCourseId(id); // Fetch courses
	model.addAttribute("questions", questions); // Add courses to model

	model.addAttribute("selectedCourseId", id);
	return "admin/questions";
}

@GetMapping("/questions/course/{id}/addQuestion")
public String addQuestion(@PathVariable long id, Model model) {
	model.addAttribute("questionForm", new QuestionForm());
	model.addAttribute("selectedCourseId", id);
	Course course = courseService.getCourseById(id);
	model.addAttribute("selectedCourseName", course.getName());
	return "admin/add_question";
}

@PostMapping("/questions/course/{id}/addQuestion")
public String createQuestion(@PathVariable long id, @ModelAttribute("questionForm") QuestionForm questionForm) {
	// Create the question
	Question question = new Question();
	question.setCourseId(id);
	question.setQuestion(questionForm.getQuestion());
	questionService.createQuestion(question);

	// Create the choices
	for (int i = 1; i <= 4; i++) {
		Choice choice = new Choice();
		choice.setQuestion(question);
		choice.setChoiceText(questionForm.getOption(i));
		questionService.createChoice(question.getId(), choice,
				Integer.toString(i).equals(questionForm.getCorrectAnswer()));
	}

	return "redirect:/admin/questions/course/" + id;
}

@PostMapping("/levels")
public String handleCourseSelection(@RequestParam("course") String selectedCourse,
		RedirectAttributes redirectAttributes) {
	// Process the selected course (e.g., store it in the session or database)
	// Here, we're adding the selected course as a query parameter to the redirect
	// URL
	redirectAttributes.addAttribute("course", selectedCourse);
	// Redirect to the manage-levels page with the selected course
	return "redirect:/admin/manage-levels";
}

@GetMapping("/manage-levels")
public String showManageLevelsPage() {
//model.addAttribute("selectedCourse", selectedCourse);
	return "admin/manage-levels";
}

@GetMapping("/manage-levels/add")
public String showAddCourseForm() {
//model.addAttribute("course", new Course());  // Add an empty Course object
	return "admin/add_level";
}

@GetMapping("/course/{courseId}/level/{levelId}/assignQuestions")
public String addQuestionPage(@PathVariable long courseId,@PathVariable long levelId,  Model model) {
	
	model.addAttribute("selectedCourseId", courseId);
	model.addAttribute("selectedLevelId", levelId);
	Course course = courseService.getCourseById(courseId);
	Level level = levelService.getLevelById(levelId);
	
	List<Question> assignedQuestions =  questionService.findByCourseIdAndLevelId(courseId, levelId);
	List<Question> questions = questionService.getAllQuestionsByCourseId(courseId); // Fetch courses
	model.addAttribute("questions", questions); // Add courses to model
	
	
	model.addAttribute("selectedCourseName", course.getName());
	model.addAttribute("selectedLevelName", level.getName());
	model.addAttribute("assignedQuestions", assignedQuestions);
	model.addAttribute("questions", questions);
	
	return "admin/assign_question";
}

@PostMapping("/course/{courseId}/level/{levelId}/assignQuestion")
public String addQuestion(@PathVariable long courseId,@PathVariable long levelId, @RequestBody Map<String, Long> requestBody) {
	long questionId = requestBody.get("questionId");
	questionService.assignQuestion(courseId, levelId, questionId);
	
	return "admin/home";
}



@GetMapping("/view-results")
public String viewResults(Model model) {
    List<ExaminationScore> results = examinationService.getAllExaminationScores();
    model.addAttribute("results", results);
    return "admin/view_result";
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
