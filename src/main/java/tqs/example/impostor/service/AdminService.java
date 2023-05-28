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

    @Override
    public String loginPage() {
        return "admin_login";
    }

    @Override
    public boolean passwordVerification(String userName, String password) {
        Admin desiredAdmin = adminRepository.findByUserName(userName).orElse(null);
        assert desiredAdmin != null;
        return Objects.equals(desiredAdmin.getPassword(), password);
    }

    @Override
    public String login(String userName, String password){
        if(passwordVerification(userName, password)) return "ACP";
        return "Login failed";
    }

    @Override
    public List<Order> getPendingOrders() {
        return orderRepository.findAll();
    }

    @Override
    public ACP searchACPById(String acpId) {
        return acpRepository.findById(Long.valueOf(acpId)).orElse(null);
    }

    @Override
    public ACP searchACPByOwnerSurname(String ownerSurname) {
        return acpRepository.findByOwnerSurname(ownerSurname).orElse(null);
    }

    @Override
    public void addACP(ACP acp) {
        acpRepository.save(acp);
    }
}
