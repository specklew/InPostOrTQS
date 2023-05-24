package tqs.example.impostor.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ACPRepositoryTest {

    @Autowired
    private ACPRepository repository;

    private ACP testAcp1;

    @BeforeEach
    void setUp() {
        testAcp1 = new ACP("Portugal, Aveiro, 42 Rua dos Marnotos, 95-100", 10.4f);
        repository.saveAndFlush(testAcp1);
    }

    @Test
    void givenCorrectAddress_whenExistsByAddress_thenReturnTrue() {
        assertThat(repository.existsByAddress(testAcp1.getAddress())).isTrue();
    }

    @Test
    void givenWrongAddress_whenExistsByAddress_thenReturnFalse() {
        assertThat(repository.existsByAddress("Wrong Address")).isFalse();
    }

    @Test
    void givenCorrectAddress_whenFindByAddress_thenReturnACP() {
        ACP found = repository.findByAddress(testAcp1.getAddress());
        assertThat(found.getCapacity()).isEqualTo(testAcp1.getCapacity());
    }

    @Test
    void givenWrongAddress_whenFindByAddress_thenReturnNull() {
        ACP found = repository.findByAddress("Wrong Address");
        assertThat(found).isNull();
    }
}