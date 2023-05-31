package tqs.example.impostor.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tqs.example.impostor.models.ACP;
import tqs.example.impostor.models.Locker;
import tqs.example.impostor.models.Order;
import tqs.example.impostor.service.ACPService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ACPControllerTest {

    @Mock
    private ACPService acpService;

    @InjectMocks
    private ACPController acpController;

    public ACPControllerTest() {
        MockitoAnnotations.openMocks(this);
        acpController = new ACPController(acpService);
    }

    @Test
    void givenAllACPs_thenReturnAllACPs() {
        ACP acp1 = new ACP("Address 1", 10);
        ACP acp2 = new ACP("Address 2", 20);
        List<ACP> acps = Arrays.asList(acp1, acp2);
        when(acpService.getAllACPs()).thenReturn(acps);

        ResponseEntity<List<ACP>> response = acpController.getAllACPs();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(acps, response.getBody());
        verify(acpService, times(1)).getAllACPs();
    }

    @Test
    void givenValidId_whenGetACPById_thenReturnACP() {
        Long id = 1L;
        ACP acp = new ACP("Address 1", 10.0f);
        when(acpService.getACPById(id)).thenReturn(Optional.of(acp));

        ResponseEntity<ACP> response = acpController.getACPById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(acp, response.getBody());
        verify(acpService, times(1)).getACPById(id);
    }

    @Test
    void givenInvalidId_whenGetACPById_thenReturnEmptyList() {
        Long id = 1L;
        when(acpService.getACPById(id)).thenReturn(Optional.empty());

        ResponseEntity<ACP> response = acpController.getACPById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(acpService, times(1)).getACPById(id);
    }

    @Test
    void givenValidAddress_whenGetACPByAddress_thenReturnACP() {
        String address = "Address 1";
        ACP acp = new ACP(address, 10.0f);

        when(acpService.getACPByAddress(address)).thenReturn(Optional.of(acp));

        ResponseEntity<ACP> response = acpController.getACPByAddress(address);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(acp, response.getBody());
        verify(acpService, times(1)).getACPByAddress(address);
    }

    @Test
    void givenInValidAddress_whenGetACPByAddress_thenReturnEmptyList() {
        String address = "Address 1";

        when(acpService.getACPByAddress(address)).thenReturn(Optional.empty());

        ResponseEntity<ACP> response = acpController.getACPByAddress(address);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(acpService, times(1)).getACPByAddress(address);
    }

    @Test
    void givenValidLockerData_whenCreateLocker_thenReturnCreatedLocker() {
        Long id = 1L;
        String address = "Address 1";
        float capacity = 10.0f;

        when(acpService.createACP(id, address, capacity)).thenReturn(true);

        ResponseEntity<Void> response = acpController.createACP(id, address, capacity);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(acpService, times(1)).createACP(id, address, capacity);
    }

    @Test
    void givenInValidLockerData_whenCreateLocker_thenReturn() {
        Long id = 1L;
        String address = "Address 1";
        float capacity = 10.0f;

        when(acpService.createACP(id, address, capacity)).thenReturn(false);

        ResponseEntity<Void> response = acpController.createACP(id, address, capacity);


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(acpService, times(1)).createACP(id, address, capacity);
    }

    @Test
    void givenExistingACPId_whenUpdateACP_tenReturnUpdatedACP() {
        Long id = 1L;
        String address = "New Address";
        Float capacity = 15.0f;
        Set<Order> orders = null;

        when(acpService.updateACP(id, address, capacity, orders)).thenReturn(true);

        ResponseEntity<Void> response = acpController.updateACP(id, address, capacity, orders);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(acpService, times(1)).updateACP(id, address, capacity, orders);
    }

    @Test
    void givenNonExistingACPId_whenUpdateACP_tenReturnNotFound() {
        Long id = 1L;
        String address = "New Address";
        Float capacity = 15.0f;
        Set<Order> orders = null;

        // Mock the service method
        when(acpService.updateACP(id, address, capacity, orders)).thenReturn(false);

        // Perform the PUT request
        ResponseEntity<Void> response = acpController.updateACP(id, address, capacity, orders);

        // Verify the response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(acpService, times(1)).updateACP(id, address, capacity, orders);
    }

    @Test
        void givenExistingACPId_whenDeleteACP_tenReturnNoContent() {
        Long id = 1L;

        when(acpService.deleteACP(id)).thenReturn(true);

        ResponseEntity<Void> response = acpController.deleteACP(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(acpService, times(1)).deleteACP(id);
    }

    @Test
    void givenNonExistingLockerId_whenDeleteLocker_thenReturnNotFound(){
    Long id = 1L;

    when(acpService.deleteACP(id)).thenReturn(false);

    ResponseEntity<Void> response = acpController.deleteACP(id);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    verify(acpService, times(1)).deleteACP(id);
    }
}


