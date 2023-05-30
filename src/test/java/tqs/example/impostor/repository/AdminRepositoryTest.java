package tqs.example.impostor.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AdminRepositoryTest {

    @Autowired

    private AdminRepository adminRepository;
    private Admin testAdmin1;

    @BeforeEach
    void setUp(){
        testAdmin1 = new Admin("Stonoga", "PanJestZerem");
        adminRepository.saveAndFlush(testAdmin1);
    }

    @Test
    void givenGoodUserName_whenFindByUserName_ThenReturnTrue(){
        Optional<Admin> found = adminRepository.findByUserName(testAdmin1.getUserName());
        if(found.isEmpty()) Assertions.fail();
        assertThat(found.get().getUserName()).isEqualTo(testAdmin1.getUserName());
    }

    @Test
    void givenBadUserName_whenFindByUserName_ThenReturnEmpty(){
        Optional<Admin> found = adminRepository.findByUserName("Zero");
        assertThat(found).isEmpty();
    }
}
