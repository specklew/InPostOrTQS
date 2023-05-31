package tqs.example.impostor.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import tqs.example.impostor.models.Admin;
import tqs.example.impostor.repository.AdminRepository;
import tqs.example.impostor.models.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTests {

    @Mock(lenient = true)
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminService adminService;

    private List<Admin> admins = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Admin admin1 = new Admin();
        admin1.setId(2137L);
        admin1.setUserName("UserName");
        admin1.setPassword("Password");
        admins.add(admin1);

        for (Admin admin : admins) {
            when(adminRepository.findById(admin.getId())).thenReturn(Optional.of(admin));
        }
    }

    @Test
    public void givenCorrectData_whenSaveAdmin_thenReturnTrue() {
        assertThat(adminService.createAdmin("userName", "password")).isTrue();
    }
    @Test
    public void givenWrongData_whenSaveAdmin_thenReturnFalse() {
        assertThat(adminService.createAdmin(null, null)).isFalse();
    }

    @Test
    public void givenAdminId_whenGetAdminById_thenReturnAdmin() {
        Long adminId = 1L;
        Admin admin = new Admin("username", "password");
        admin.setId(adminId);

        when(adminRepository.findById(adminId)).thenReturn(Optional.of(admin));

        Admin retrievedAdmin = adminService.getAdminById(adminId);

        Assertions.assertEquals(admin, retrievedAdmin);
        verify(adminRepository, times(1)).findById(adminId);
    }

    @Test
    public void givenCorrectData_whenUpdateAdmin_thenReturnTrue() {
        assertThat(adminService.updateAdmin(admins.get(0).getId(), "newUserName", "newPassword")).isTrue();
    }

    @Test
    public void givenWrongData_whenUpdateAdmin_thenReturnFalse() {
        assertThat(adminService.updateAdmin(admins.get(0).getId(), null, null)).isFalse();
    }

    @Test
    public void givenAdminId_whenDeleteAdmin_thenVerifyDeleteOperation() {
        Long adminId = 1L;

        adminService.deleteAdmin(adminId);

        verify(adminRepository, times(1)).deleteById(adminId);
    }

    @Test
    public void givenValidUserNameAndPassword_whenPasswordVerification_thenReturnsTrue() {
        String userName = "admin";
        String password = "password";
        Admin admin = new Admin(userName, password);

        when(adminRepository.findByUserName(userName)).thenReturn(Optional.of(admin));

        boolean result = adminService.passwordVerification(userName, password);

        Assertions.assertTrue(result);
    }

    @Test
    public void givenInvalidUserNameAndPassword_whenPasswordVerification_thenReturnsFalse() {
        String userName = "admin";
        String password = "password";

        when(adminRepository.findByUserName(userName)).thenReturn(Optional.empty());

        boolean result = adminService.passwordVerification(userName, password);

        Assertions.assertFalse(result);
    }

    @Test
    public void givenValidUserNameAndPassword_whenLogin_thenReturnsACP() {
        String userName = "admin";
        String password = "password";
        Admin admin = new Admin(userName, password);

        when(adminRepository.findByUserName(userName)).thenReturn(Optional.of(admin));

        String result = adminService.login(userName, password);

        Assertions.assertEquals("ACP", result);
    }

    @Test
    public void givenInvalidUserNameAndPassword_whenLogin_thenReturnsLoginFailed() {
        String userName = "admin";
        String password = "password";

        when(adminRepository.findByUserName(anyString())).thenReturn(Optional.empty());

        String result = adminService.login(userName, password);

        Assertions.assertEquals("Login failed", result);
    }
}
