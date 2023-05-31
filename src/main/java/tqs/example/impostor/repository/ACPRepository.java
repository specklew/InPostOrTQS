package tqs.example.impostor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tqs.example.impostor.models.ACP;

import java.util.Optional;

@Repository
public interface ACPRepository extends JpaRepository<ACP, Long> {

    boolean existsByAddress(String address);

    Optional<ACP> findByAddress(String address);

    Optional<ACP> findByID(Long id);
}
