package tqs.example.impostor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Admin_loginController {

    @GetMapping("/admin")
    public String adminLogin() {
        return "admin_login";
    }

}

