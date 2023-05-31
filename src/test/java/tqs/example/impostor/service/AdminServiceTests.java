package tqs.example.impostor.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs.example.impostor.models.Admin;
import tqs.example.impostor.repository.AdminRepository;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTests {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenAdmin_whenSaveAdmin_thenReturnSavedAdmin() {
        Admin admin = new Admin("username", "password");

        when(adminRepository.save(any(Admin.class))).thenReturn(admin);

        Admin savedAdmin = adminService.saveAdmin(admin);

        Assertions.assertEquals(admin, savedAdmin);
        verify(adminRepository, times(1)).save(admin);
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
    public void givenAdmin_whenUpdateAdmin_thenReturnUpdatedAdmin() {
        Admin admin = new Admin("username", "password");
        admin.setId(1L);

        when(adminRepository.save(any(Admin.class))).thenReturn(admin);

        Admin updatedAdmin = adminService.updateAdmin(admin);

        Assertions.assertEquals(admin, updatedAdmin);
        verify(adminRepository, times(1)).save(admin);
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
