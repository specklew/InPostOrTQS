package tqs.example.impostor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.example.impostor.models.ACP;
import tqs.example.impostor.models.Admin;
import tqs.example.impostor.models.Order;
import tqs.example.impostor.repository.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AdminService implements AdminServiceInterface{

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ACPService acpService;

    public AdminService(AdminRepository adminRepository){
        this.adminRepository = adminRepository;
    }

    //Create
    public boolean createAdmin(String userName, String password){
        if(userName == null || password == null) return false;
        if(adminRepository.findByUserName(userName).isPresent()) return false;
        Admin admin = new Admin();
        admin.setUserName(userName);
        admin.setPassword(password);

        adminRepository.saveAndFlush(admin);
        return true;
    }

    public void createAdminProgrammatically() {
        String username = "dummy";
        String password = "password";

        createAdmin(username, password);
    }

    // Read
    public Admin getAdminById(Long id) {
        return adminRepository.findById(id).orElse(null);
    }

    // Update
    public boolean updateAdmin(long id, String userName, String password) {
        Optional<Admin> optionalAdmin = adminRepository.findById(id);
        if (optionalAdmin.isEmpty()) return false;

        Admin admin = optionalAdmin.get();

        if(userName == null && password == null) return false;
        if (userName != null) admin.setUserName(userName);
        if (password != null) admin.setPassword(password);

        adminRepository.saveAndFlush(admin);
        return true;
    }
    // Delete
    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }

    @Override
    public boolean passwordVerification(String userName, String password) {
        Optional<Admin> desiredAdminOptional = adminRepository.findByUserName(userName);
        if (desiredAdminOptional.isPresent()) {
            Admin desiredAdmin = desiredAdminOptional.get();
            return Objects.equals(desiredAdmin.getPassword(), password);
        }
        return false;
    }

    @Override
    public String login(String userName, String password){
        if(passwordVerification(userName, password)) {
            return "ACP";
        }
        return "Login failed";
    }


    //Function below is waiting for Order Service
    @Override
    public List<Order> getPendingOrders() {
        //TODO
        return null;
    }

    public Optional<ACP> searchACPById(Long acpId) {
        return acpService.getACPById(acpId);
    }

    @Override
    public boolean addACP(Long id,String address, double capacity) {

        return acpService.createACP(id, address, capacity);
    }
}
