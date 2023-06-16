package tqs.example.impostor.service;

import org.springframework.stereotype.Service;
import tqs.example.impostor.models.ACP;
import tqs.example.impostor.repository.ACPRepository;

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

    public boolean createACP(Long id, String address, double capacity) {
        if (address == null || capacity == 0) {
            return false;
        }

        if (acpRepository.existsById(id)) {
            return false;
        }

        ACP acp = new ACP();
        acp.setId(id);
        acp.setAddress(address);
        acp.setCapacity(capacity);

        acpRepository.save(acp);

        return acpRepository.existsById(id);
    }

    public Optional<ACP> getACPById(Long id) {
        return acpRepository.findById(id);
    }


    public boolean updateACP(Long id, String address, double capacity) {
        Optional<ACP> acpOptional = acpRepository.findById(id);
        // poprawione according to maciek's remarks @malwina
        if (acpOptional.isPresent()) {
            ACP acp = acpOptional.get();
            if (address != null) {
                acp.setAddress(address);
            }
            if (capacity >= 0) {
                acp.setCapacity(capacity);
            }
            acpRepository.saveAndFlush(acp);
            return true;
        } else {
            return false; // when ACP with specified ID does not exist
        }
    }

    public boolean deleteACP(long id) {
        if (!acpRepository.existsById(id)) return false;
        acpRepository.deleteById(id);
        return true;
    }
}
