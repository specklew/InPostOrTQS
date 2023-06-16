package tqs.example.impostor.proofofconcept.adminPanel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tqs.example.impostor.service.AdminService;

import static java.lang.Long.parseLong;

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

    @PostMapping("/logged")
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

    @GetMapping("/adminPanel/ACP")
    public String acpPage() {
        // Logic for handling ACP page
        return "adminPanel/ACP";
    }

    @GetMapping("/adminPanel/deliveries")
    public String deliveriesPage() {
        // Logic for handling deliveries page
        return "adminPanel/deliveries";
    }


    @PostMapping("/ACP/add")
    public void addACP(@RequestParam("id") String id, @RequestParam("address") String address,
                         @RequestParam("capacity") String capacity){
        adminService.addACP(parseLong(id), address, Float.parseFloat(capacity));
    }



}

