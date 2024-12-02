package com.magadhUniversity.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import com.magadhUniversity.service.EmployeeService;
import com.magadhUniversity.service.StudentService;
import com.magadhUniversity.service.SubjectService;

@Controller
public class HomeController {

    private final EmployeeService employeeService;
    private final StudentService studentService;
    private final SubjectService subjectService;

    public HomeController(EmployeeService employeeService,
                          StudentService studentService,
                          SubjectService subjectService) {
        this.employeeService = employeeService;
        this.studentService = studentService;
        this.subjectService = subjectService;
    }

    @GetMapping("/home")
    public String home(Model model) {
        // Get the currently authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Determine the user role and set the appropriate view
        String viewName;
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            viewName = "adminHome";

            // Add counts for admin
            model.addAttribute("employeeCount", employeeService.getAllEmployees().size());
            model.addAttribute("studentCount", studentService.getAllStudents().size());
            model.addAttribute("subjectCount", subjectService.getAllSubjects().size());

        } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYEE"))) {
            viewName = "employeeHome";
        } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"))) {
            viewName = "studentHome";
        } else {
            viewName = "home";
        }

        return viewName;
    }
}
