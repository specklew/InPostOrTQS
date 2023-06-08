package tqs.example.impostor.service;

import org.springframework.stereotype.Service;
import tqs.example.impostor.models.ACP;
import tqs.example.impostor.repository.ACPRepository;
import tqs.example.impostor.models.Order;

import java.util.*;

@Service
public class ACPService {

    private final ACPRepository acpRepository;

    public ACPService(ACPRepository acpRepository) {
        this.acpRepository = acpRepository;
    }

    public List<ACP> getAllACPs() {
        return acpRepository.findAll();
    }

    public Optional<ACP> getACPByAddress(String address) {
        return acpRepository.findByAddress(address);
    }

    public boolean createACP(Long id, String address, float capacity) {
        if (address == null || capacity == 0) {
            return false;
        }
        ACP acp = new ACP();
        //Set<Order> orders;
        if (!acpRepository.existsById(id)) {
            acp.setOrders(null);
            acp.setId(id);
            acp.setAddress(address);
            acp.setCapacity(capacity);
        } else {
            return false;
        }

        return true;

    }

    public Optional<ACP> getACPById(Long id) {
        Optional<ACP> acp = acpRepository.findById(id);
        return Optional.ofNullable(acp.orElse(null));
    }


    public boolean updateACP(Long id, String address, float capacity, Set<Order> order) {
        Optional<ACP> acp = acpRepository.findById(id);
        // What does s stand for my dude? I get that it's a temp variable but come on.
        // Also getting an optional without checking isEmpty is just bad. You should probably return false is optional
        // is empty. @maciek
        ACP s = acp.get();
        if (address != null) {
            s.setAddress(address);
        }
        if (capacity >= 0) {
            s.setCapacity(capacity);
        }
        if (order != null) {
            s.setOrders(order); // The naming here is confusing since you're not updating order but orders
        }
        acpRepository.saveAndFlush(s);
        return true;
    }

    public boolean deleteACP(long id) {
        if (!acpRepository.existsById(id)) return false;
        acpRepository.deleteById(id);
        return true;
    }


}
