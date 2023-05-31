package tqs.example.impostor.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tqs.example.impostor.models.Admin;
import tqs.example.impostor.service.AdminService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AdminControllerTests {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    public AdminControllerTests() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenValidAdmin_whenCreateAdmin_thenReturnsCreatedStatus() {
        Admin admin = new Admin("username", "password");
        when(adminService.saveAdmin(any(Admin.class))).thenReturn(admin);

        ResponseEntity<Admin> response = adminController.createAdmin(admin);

        verify(adminService, times(1)).saveAdmin(admin);
        assert response.getStatusCode() == HttpStatus.CREATED;
    }

    @Test
    public void givenExistingAdmin_whenGetAdminById_theReturnsAdminWithOkStatus() {
        Long adminId = 1L;
        Admin admin = new Admin("username", "password");
        admin.setId(adminId);
        when(adminService.getAdminById(adminId)).thenReturn(admin);

        ResponseEntity<Admin> response = adminController.getAdminById(adminId);

        verify(adminService, times(1)).getAdminById(adminId);
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() != null;
        assert response.getBody().getId() == adminId;
    }

    @Test
    public void givenNonexistentAdmin_whenGetAdminById_thenReturnsNotFoundStatus() {
        Long adminId = 1L;
        when(adminService.getAdminById(adminId)).thenReturn(null);

        ResponseEntity<Admin> response = adminController.getAdminById(adminId);

        verify(adminService, times(1)).getAdminById(adminId);
        assert response.getStatusCode() == HttpStatus.NOT_FOUND;
    }

    @Test
    public void givenValidAdmin_whenUpdateAdmin_thenReturnsUpdatedAdminWithOkStatus() {
        Admin admin = new Admin("username", "password");
        admin.setId(1L);
        when(adminService.updateAdmin(any(Admin.class))).thenReturn(admin);

        ResponseEntity<Admin> response = adminController.updateAdmin(admin);

        verify(adminService, times(1)).updateAdmin(admin);
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() != null;
        assert response.getBody().getId() == admin.getId();
    }

    @Test
    public void givenNonexistentAdmin_whenUpdateAdmin_thenReturnsNotFoundStatus() {
        Admin admin = new Admin("username", "password");
        admin.setId(1L);
        when(adminService.updateAdmin(any(Admin.class))).thenReturn(null);

        ResponseEntity<Admin> response = adminController.updateAdmin(admin);

        verify(adminService, times(1)).updateAdmin(admin);
        assert response.getStatusCode() == HttpStatus.NOT_FOUND;
    }

    @Test
    public void givenExistingAdmin_whenDeleteAdmin_thenReturnsNoContentStatus() {
        Long adminId = 1L;

        ResponseEntity<Void> response = adminController.deleteAdmin(adminId);

        verify(adminService, times(1)).deleteAdmin(adminId);
        assert response.getStatusCode() == HttpStatus.NO_CONTENT;
    }

    @Test
    public void givenValidCredentials_whenLogin_thenReturnsOkStatus() {
        String username = "admin";
        String password = "password";
        when(adminService.login(username, password)).thenReturn("ACP");

        ResponseEntity<String> response = adminController.login(username, password);

        verify(adminService, times(1)).login(username, password);
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() != null;
        assert response.getBody().equals("Login successful");
    }

    @Test
    public void givenInvalidCredentials_whenLogin_thenReturnsUnauthorizedStatus() {
        String username = "admin";
        String password = "password";
        when(adminService.login(username, password)).thenReturn("Login failed");

        ResponseEntity<String> response = adminController.login(username, password);

        verify(adminService, times(1)).login(username, password);
        assert response.getStatusCode() == HttpStatus.UNAUTHORIZED;
        assert response.getBody() != null;
        assert response.getBody().equals("Invalid credentials");
    }
}
