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
    public boolean createACP(Long id,String address, float capacity){
            if(address==null || capacity==0){
                return false;
            }
            ACP acp = new ACP();
            //Set<Order> orders;
            if(!acpRepository.existsById(id)){
                acp.setOrders(null);
                acp.setId(id);
                acp.setAddress(address);
                acp.setCapacity(capacity);
            }else{
                return false;
            }

            return true;

    }
    public Optional<ACP> searchACPbyID(Long id){
        Optional<ACP> acp = acpRepository.findByID(id);
        return Optional.ofNullable(acp.orElse(null));
    }


    public boolean updateACP(Long id,String address, float capacity,Set<Order> order){
        Optional<ACP> acp = acpRepository.findById(id);
        ACP s = acp.get();
        if(acp.isEmpty()){
            return false;
        }
        if(address != null){
            s.setAddress(address);
        }
        if(capacity>=0){
            s.setCapacity(capacity);
        }
        if(order!=null){
            s.setOrders(order);
        }
        acpRepository.saveAndFlush(s);
        return true;
    }

    public boolean deleteACP(long id){
        if(!acpRepository.existsById(id)) return false;
        acpRepository.deleteById(id);
        return true;
    }


}
