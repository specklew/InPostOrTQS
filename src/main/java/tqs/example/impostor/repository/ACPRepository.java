package tqs.example.impostor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ACPRepository extends JpaRepository<ACP, Long> {

    boolean existsByAddress(String address);

    ACP findByAddress(String address);
}
