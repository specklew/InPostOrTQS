package tqs.example.impostor.proofofconcept.adminPanel;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tqs.example.impostor.models.ACP;
import tqs.example.impostor.models.Order;
import tqs.example.impostor.service.AdminService;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
        return "adminPanel/admin_login";
    }

    @PostMapping("/logged")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, Model model) throws IOException, ParseException {
        String loginResult = adminService.login(username, password);
        if (loginResult.equals("ACP")) {
            // Login successful, redirect to the next page
            getOrders(model);
            return "adminPanel/deliveries";
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

    @PostMapping("/ACP/search")
    public String searchACP(@RequestParam("id") String id, Model model) throws IOException, ParseException {
        Optional<ACP> acpOptional = httpRequester.getAcpFromRemoteServer(id);
        ACP acp = acpOptional.orElse(null); // Get the ACP object if present, or null if not

        if (acp != null) {
            System.out.println("First ACP: " + acp.getId() + acp.getAddress() + acp.getCapacity());
            model.addAttribute("acp", acp);
        }

        return "adminPanel/ACP";
    }

    @PostMapping("/ACP/add")
    public String addACP(@RequestParam("id") String id, @RequestParam("address") String address,
                         @RequestParam("capacity") String capacity){
        //Possibility to add error page/popup
        if(adminService.addACP(parseLong(id), address, Double.parseDouble(capacity))) return "adminPanel/ACP";
        return "adminPanel/ACP";
    }

    @PostMapping("/deliveries")
    public String getOrders(Model model) throws IOException, ParseException {
        List<Order> orders = httpRequester.getOrdersFromRemoteServer();
        System.out.println("Siema");
        System.out.println(orders.get(0));
        model.addAttribute("orders", orders);

       return "adminPanel/deliveries";
    }

    @GetMapping("/deliveries")
    public String getOrders2(Model model) throws IOException, ParseException {
        List<Order> orders = httpRequester.getOrdersFromRemoteServer();
        System.out.println("Siema");
        System.out.println(orders.get(0));
        model.addAttribute("orders", orders);

        return "adminPanel/deliveries";
    }


}

