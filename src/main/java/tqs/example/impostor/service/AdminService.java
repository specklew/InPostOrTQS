package tqs.example.impostor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.example.impostor.repository.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AdminService implements AdminServiceInterface{

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ACPRepository acpRepository;

    public AdminService(AdminRepository adminRepository){
        this.adminRepository = adminRepository;
    }

    // Create
    public Admin saveAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    // Read
    public Admin getAdminById(Long id) {
        return adminRepository.findById(id).orElse(null);
    }

    // Update
    public Admin updateAdmin(Admin admin) {
        return adminRepository.save(admin);
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


    //Functions below are waiting for ACP Service
    @Override
    public List<Order> getPendingOrders() {
        //TODO
        return null;
    }

    @Override
    public ACP searchACPById(String acpId) {
        //TODO
        return null;
    }

    @Override
    public void addACP(ACP acp) {
        //TODO
    }
}
