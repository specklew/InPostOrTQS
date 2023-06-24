package tqs.example.impostor.service;

import org.springframework.stereotype.Service;
import tqs.example.impostor.models.ACP;
import tqs.example.impostor.models.Locker;
import tqs.example.impostor.repository.LockerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LockerService {

    private final LockerRepository lockerRepository;

    public LockerService(LockerRepository lockerRepository) {
        this.lockerRepository = lockerRepository;
    }

    public List<Locker> getAllLockers() {
        return lockerRepository.findAll();
    }

    public Optional<Locker> getLockerById(Long id) {
        return lockerRepository.findById(id);
    }

    public Optional<Locker> getLockerByAddress(String address) {
        return lockerRepository.findByAddress(address);
    }

    // CRUD
    public boolean createLocker(Long id, String address, float capacity) {
        if (address == null || capacity == 0) {
            return false;
        }

        if (lockerRepository.existsById(id)) {
            return false;
        }

        Locker locker = new Locker();
        locker.setId(id);
        locker.setAddress(address);
        locker.setCapacity(capacity);

        lockerRepository.saveAndFlush(locker);

        return true;
    }


    public boolean updateLocker(Long id, String address, Float capacity) {
        Optional<Locker> lockerOptional = lockerRepository.findById(id);
        if (lockerOptional.isPresent()) {
            Locker locker = lockerOptional.get();
            if (address != null) {
                locker.setAddress(address);
            }
            if (capacity != null) {
                locker.setCapacity(capacity);
            }
            lockerRepository.saveAndFlush(locker);
            return true;
        } else {
            return false; // when Locker with specified ID does not exist
        }
    }


    public void deleteLocker(Long id) {
        if (!lockerRepository.existsById(id)) {
            throw new IllegalStateException("Specified entry does not exist. ");
        }
        lockerRepository.deleteById(id);
    }
}
