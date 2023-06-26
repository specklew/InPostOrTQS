package tqs.example.impostor.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tqs.example.impostor.models.ACP;
import tqs.example.impostor.models.Admin;
import tqs.example.impostor.models.Order;
import tqs.example.impostor.service.AdminService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Operation(description = "Create an admin")
    @PostMapping("/create")
    public ResponseEntity<String> createAdmin(@RequestParam("username") String username,
                                              @RequestParam("password") String password) {
        if (username == null || password == null) {
            return ResponseEntity.badRequest().body("Invalid username or password");
        }

        boolean created = adminService.createAdmin(username, password);
        if (created) {
            return ResponseEntity.ok("Admin created successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create admin");
        }
    }

    @Operation(description = "Get Admin by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable Long id) {
        Admin admin = adminService.getAdminById(id);
        if (admin != null) {
            return new ResponseEntity<>(admin, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(description = "Update Admin")
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateAdmin(@PathVariable Long id,
                                              @RequestParam(required = false) String username,
                                              @RequestParam(required = false) String password) {
        Optional<Admin> optionalAdmin = Optional.ofNullable(adminService.getAdminById(id));
        if (optionalAdmin.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (username == null && password == null) {
            return ResponseEntity.badRequest().body("No update parameters provided");
        }

        adminService.updateAdmin(id, username, password);
        return ResponseEntity.ok("Admin updated successfully");
    }

    @Operation(description = "Delete Admin")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(description = "Login to account")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam("username") String username, @RequestParam("password") String password) {
        // Pass the username and password to the AdminService for authentication
        String result = adminService.login(username, password);

        // If login is successful, return a successful response
        if (result.equals("ACP")) {
            return ResponseEntity.ok("Login successful");
        } else {
            // Return an error response or handle failed login
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @Operation(description = "Search ACP by ID")
    @GetMapping("/acp/{id}")
    public ResponseEntity<ACP> searchACPById(@PathVariable Long id) {
        Optional<ACP> acp = adminService.searchACPById(id);
        return acp.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(description = "Add ACP")
    @PostMapping("/acp")
    public ResponseEntity<String> addACP(@RequestParam Long id,
                                         @RequestParam String address,
                                         @RequestParam float capacity) {
        adminService.addACP(id, address, capacity);
        return ResponseEntity.ok("ACP added successfully");
    }

    @Operation(description = "Get all orders")
    @GetMapping("/deliveries/all")
    public ResponseEntity<List<Order>> getAllOrders(){
        List<Order> orders = adminService.getPendingOrders();
//        System.out.println("TEST 3");
//        for (Order order : orders) {
//            System.out.println(order);
//        }
        return ResponseEntity.ok(orders);
    }


}
