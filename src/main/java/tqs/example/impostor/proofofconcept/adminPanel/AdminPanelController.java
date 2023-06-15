package tqs.example.impostor.proofofconcept.adminPanel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tqs.example.impostor.service.AdminService;

@Controller
@RequestMapping("/adminPanel")
public class AdminPanelController {

    AdminPanelHttpRequester httpRequester;

    private final AdminService adminService;


    @Autowired
    AdminPanelController(AdminPanelHttpRequester requester, AdminService adminService) {
        this.httpRequester = requester;
        this.adminService = adminService;
    }
    @GetMapping()
    public String index() {
        adminService.createAdminProgrammatically();
        return "adminPanel/admin_login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password) {
        String loginResult = adminService.login(username, password);
        if (loginResult.equals("ACP")) {
            // Login successful, redirect to the next page
            return "adminPanel/ACP";
        } else {
            // Login failed, return to the login page with an error message
            return "adminPanel/admin_login";
        }
    }

}

