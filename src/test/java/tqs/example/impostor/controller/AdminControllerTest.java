package tqs.example.impostor.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tqs.example.impostor.models.ACP;
import tqs.example.impostor.models.Admin;
import tqs.example.impostor.service.AdminService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    public AdminControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenValidUsernameAndPassword_whenCreateAdmin_thenReturnAdminCreatedSuccessfully() {
        String username = "admin";
        String password = "password";

        when(adminService.createAdmin(username, password)).thenReturn(true);

        ResponseEntity<String> response = adminController.createAdmin(username, password);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Admin created successfully", response.getBody());
    }

    @Test
    public void givenNullUsernameAndPassword_whenCreateAdmin_thenReturnInvalidUsernameOrPassword() {
        String username = null;
        String password = null;

        ResponseEntity<String> response = adminController.createAdmin(username, password);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid username or password", response.getBody());
    }

    @Test
    public void givenFailedAdminCreation_whenCreateAdmin_thenReturnFailedToCreateAdmin() {
        String username = "admin";
        String password = "password";

        when(adminService.createAdmin(username, password)).thenReturn(false);

        ResponseEntity<String> response = adminController.createAdmin(username, password);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to create admin", response.getBody());
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
    public void givenValidIdAndUsername_whenUpdateAdmin_thenReturnAdminUpdatedSuccessfully() {
        Long id = 1L;
        String username = "newAdmin";

        Admin admin = new Admin("admin", "password");
        admin.setId(id);

        when(adminService.getAdminById(id)).thenReturn(admin);
        when(adminService.updateAdmin(id, username, null)).thenReturn(true);

        ResponseEntity<String> response = adminController.updateAdmin(id, username, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Admin updated successfully", response.getBody());
        verify(adminService, times(1)).updateAdmin(id, username, null);
    }

    @Test
    public void givenInvalidId_whenUpdateAdmin_thenReturnNotFound() {
        Long id = 1L;

        when(adminService.getAdminById(id)).thenReturn(null);

        ResponseEntity<String> response = adminController.updateAdmin(id, null, null);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(adminService, never()).updateAdmin(anyLong(), anyString(), anyString());
    }

    @Test
    public void givenNoUpdateParametersProvided_whenUpdateAdmin_thenReturnBadRequest() {
        Long id = 1L;

        Admin admin = new Admin("admin", "password");
        admin.setId(id);

        when(adminService.getAdminById(id)).thenReturn(admin);

        ResponseEntity<String> response = adminController.updateAdmin(id, null, null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("No update parameters provided", response.getBody());
        verify(adminService, never()).updateAdmin(anyLong(), anyString(), anyString());
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

    @Test
    public void givenExistingACPId_whenSearchACPById_thenReturnACP() {
        Long acpId = 1L;
        ACP acp = new ACP("Address", 100.0f);
        acp.setId(acpId);
        Optional<ACP> optionalACP = Optional.of(acp);

        when(adminService.searchACPById(acpId)).thenReturn(optionalACP);

        ResponseEntity<ACP> response = adminController.searchACPById(acpId);

        verify(adminService, times(1)).searchACPById(acpId);
        assertSame(acp, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void givenNonExistingACPId_whenSearchACPById_thenReturnNotFound() {
        Long acpId = 1L;
        Optional<ACP> optionalACP = Optional.empty();

        when(adminService.searchACPById(acpId)).thenReturn(optionalACP);

        ResponseEntity<ACP> response = adminController.searchACPById(acpId);

        verify(adminService, times(1)).searchACPById(acpId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void givenValidACPParameters_whenAddACP_thenReturnSuccess() {
        Long acpId = 1L;
        String address = "Address";
        float capacity = 100.0f;

        ResponseEntity<String> response = adminController.addACP(acpId, address, capacity);

        verify(adminService, times(1)).addACP(acpId, address, capacity);
        assertEquals("ACP added successfully", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
