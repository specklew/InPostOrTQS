package tqs.example.impostor.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tqs.example.impostor.repository.Locker;
import tqs.example.impostor.service.LockerService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LockerControllerTest {

    @Mock
    private LockerService lockerService;

    @InjectMocks
    private LockerController lockerController;

    public LockerControllerTest() {
        MockitoAnnotations.openMocks(this);
        lockerController = new LockerController(lockerService);
    }

    @Test
    void givenAllLockers_thenReturnAllLockers() {
        Locker locker1 = new Locker("Address 1", 10);
        Locker locker2 = new Locker("Address 2", 20);
        List<Locker> lockers = Arrays.asList(locker1, locker2);
        when(lockerService.getAllLockers()).thenReturn(lockers);

        ResponseEntity<List<Locker>> response = lockerController.getAllLockers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(lockers, response.getBody());
        verify(lockerService, times(1)).getAllLockers();
    }

    @Test
    void givenValidId_whenGetLockerById_thenReturnLocker() {
        Long id = 1L;
        Locker locker = new Locker("Address 1", 10);
        when(lockerService.getLockerById(id)).thenReturn(Optional.of(locker));

        ResponseEntity<Locker> response = lockerController.getLockerById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(locker, response.getBody());
        verify(lockerService, times(1)).getLockerById(id);
    }

    @Test
    void givenInvalidId_whenGetLockerById_thenReturnEmptyList() {
        Long id = 1L;
        when(lockerService.getLockerById(id)).thenReturn(Optional.empty());

        ResponseEntity<Locker> response = lockerController.getLockerById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(lockerService, times(1)).getLockerById(id);
    }

    @Test
    void givenValidLockerData_whenCreateLocker_thenReturnCreatedLocker() {
        Locker locker = new Locker("Address 1", 10);
        Locker savedLocker = new Locker("Address 1", 10);
        when(lockerService.createLocker(locker)).thenReturn(savedLocker);

        ResponseEntity<Locker> response = lockerController.createLocker(locker);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedLocker, response.getBody());
        verify(lockerService, times(1)).createLocker(locker);
    }

    @Test
    void givenExistingLockerId_whenUpdateLocker_tenReturnUpdatedLocker() {
            Long id = 1L;
            String newAddress = "New Address";
            Integer newCapacity = 15;
            Locker existingLocker = new Locker("Old Address", 10);
            Locker updatedLocker = new Locker(newAddress, newCapacity);
            when(lockerService.getLockerById(id)).thenReturn(Optional.of(existingLocker));
            when(lockerService.updateLocker(id, newAddress, newCapacity)).thenReturn(updatedLocker);

            ResponseEntity<Locker> response = lockerController.updateLocker(id, newAddress, newCapacity);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(updatedLocker, response.getBody());
            verify(lockerService, times(1)).getLockerById(id);
            verify(lockerService, times(1)).updateLocker(id, newAddress, newCapacity);
    }


    @Test
    void givenNonExistingLockerId_whenUpdateLocker_thenReturnNotFound() {
        Long id = 1L;
        String newAddress = "New Address";
        Integer newCapacity = 15;
        when(lockerService.getLockerById(id)).thenReturn(Optional.empty());

        ResponseEntity<Locker> response = lockerController.updateLocker(id, newAddress, newCapacity);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(lockerService, times(1)).getLockerById(id);
        verifyNoMoreInteractions(lockerService);
    }

    @Test
    void givenExistingLockerId_whenDeleteLocker_thenReturnNoContent(){
        Long id = 1L;
        when(lockerService.getLockerById(id)).thenReturn(Optional.of(new Locker()));

        ResponseEntity<Void> response = lockerController.deleteLocker(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(lockerService, times(1)).deleteLocker(id);
    }

    @Test
    void givenNonExistingLockerId_whenDeleteLocker_thenReturnNotFound() {
        Long id = 1L;
        when(lockerService.getLockerById(id)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = lockerController.deleteLocker(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(lockerService, times(1)).getLockerById(id);
        verifyNoMoreInteractions(lockerService);
    }
}