package tqs.example.impostor.service;

import org.springframework.stereotype.Service;
import tqs.example.impostor.repository.Locker;
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
    public Locker createLocker(Locker locker) {
        return lockerRepository.save(locker);
    }

    public Locker updateLocker(Long id, String newAddress) {
        Optional<Locker> optionalLocker = lockerRepository.findById(id);
        if (optionalLocker.isPresent()) {
            Locker locker = optionalLocker.get();
            locker.setAddress(newAddress);
            return lockerRepository.save(locker);
        }
        return null;
    }

    public void deleteLocker(Long id) {
        if (!lockerRepository.existsById(id)) {
            throw new IllegalStateException("Specified entry does not exist. ");
        }
        lockerRepository.deleteById(id);
    }
}
