package tqs.example.impostor.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;

@DataJpaTest
public class LockerRepositoryTest {

    @Autowired
    private LockerRepository lockerRepository;

    @Test
    public void whenFindLockerByExistingId_thenReturnLocker() {
        Locker locker = new Locker("Locker 1", 10);
        lockerRepository.save(locker);

        Optional<Locker> foundLocker = lockerRepository.findById(locker.getId());

        Assertions.assertThat(foundLocker).isPresent();
        Assertions.assertThat(foundLocker.get().getId()).isEqualTo(locker.getId());
        Assertions.assertThat(foundLocker.get().getAddress()).isEqualTo(locker.getAddress());
        Assertions.assertThat(foundLocker.get().getCapacity()).isEqualTo(locker.getCapacity());
    }

    @Test
    public void whenFindInvalidLockerId_thenReturnNull() {
        Optional<Locker> foundLocker = lockerRepository.findById(9999L);
        Assertions.assertThat(foundLocker).isEmpty();
    }

    @Test
    public void givenCorrectAddress_whenFindByAddress_thenReturnLocker() {
        Locker locker = new Locker("Locker 1", 10);
      
        lockerRepository.save(locker);

        Optional<Locker> foundLocker = lockerRepository.findByAddress("Locker 1");

        Assertions.assertThat(foundLocker).isPresent();
        Assertions.assertThat(foundLocker.get().getId()).isEqualTo(locker.getId());
        Assertions.assertThat(foundLocker.get().getAddress()).isEqualTo(locker.getAddress());
        Assertions.assertThat(foundLocker.get().getCapacity()).isEqualTo(locker.getCapacity());
    }

    @Test
    public void givenWrongAddress_whenFindByAddress_thenReturnEmpty() {
        Optional<Locker> lockerFound = lockerRepository.findByAddress("Non-existent locker");
        Assertions.assertThat(lockerFound).isEmpty();
    }
}