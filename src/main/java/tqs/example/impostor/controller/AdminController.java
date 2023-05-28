package tqs.example.impostor.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import tqs.example.impostor.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/")
    public String index(){return adminService.loginPage();}

    @PostMapping("/login")
    public ModelAndView login(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Pass the username and password to the AdminService for authentication
        String result = adminService.login(username, password);

        // If login is successful, redirect to the ACP.html page
        if (result.equals("ACP")) {
            return new ModelAndView(new RedirectView("ACP.html"));
        } else {
            // Return error message or handle failed login
            return new ModelAndView("login-failed");
        }
    }
}
