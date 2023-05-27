package tqs.example.impostor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LockerRepository extends JpaRepository<Locker, Long> {
    Optional<Locker> findById(Long id);
    Optional<Locker> findByAddress(String address);

}
