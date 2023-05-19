package tqs.example.impostor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ACPRepository extends JpaRepository<ACP, Long> {

    boolean existsByAddress(String address);
    ACP findByAddress(String address);
}
