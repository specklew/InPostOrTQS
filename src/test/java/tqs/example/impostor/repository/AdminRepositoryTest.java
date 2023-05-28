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
    void findById_WhenGoodId(){
        Optional<Admin> found = adminRepository.findById(testAdmin1.getId());
        if(found.isEmpty()) Assertions.fail();
        assertThat(found.get().getId()).isEqualTo(testAdmin1.getId());
    }

    @Test
    void findById_WhenBadId(){
        Optional<Admin> found = adminRepository.findById(2137420L);
        assertThat(found).isEmpty();
    }

    @Test
    void findByUserName_WhenGoodUserName(){
        Optional<Admin> found = adminRepository.findByUserName(testAdmin1.getUserName());
        if(found.isEmpty()) Assertions.fail();
        assertThat(found.get().getUserName()).isEqualTo(testAdmin1.getUserName());
    }

    @Test
    void findByUserName_WhenBadUserName(){
        Optional<Admin> found = adminRepository.findByUserName("Zero");
        assertThat(found).isEmpty();
    }
}
