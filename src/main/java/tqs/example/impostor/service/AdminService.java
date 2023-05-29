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
