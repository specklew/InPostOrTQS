package tqs.example.impostor.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs.example.impostor.models.Locker;
import tqs.example.impostor.repository.LockerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LockerServiceTest {

    @Mock
    private LockerRepository lockerRepository;

    private LockerService lockerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        lockerService = new LockerService(lockerRepository);
    }

    @Test
    void givenAllLockers_thenReturnAllLockers() {
        List<Locker> expectedLockers = new ArrayList<>();
        expectedLockers.add(new Locker("Address 1", 0.5f));
        expectedLockers.add(new Locker("Address 2", 0.8f));
        when(lockerRepository.findAll()).thenReturn(expectedLockers);

        List<Locker> actualLockers = lockerService.getAllLockers();

        assertEquals(expectedLockers, actualLockers);
        verify(lockerRepository, times(1)).findAll();
    }

    @Test
    void givenValidId_whenGetLockerById_thenReturnLocker() {
        Long id = 1L;
        Locker expectedLocker = new Locker("Address 1", 0.5f);
        when(lockerRepository.findById(id)).thenReturn(Optional.of(expectedLocker));

        Optional<Locker> actualLocker = lockerService.getLockerById(id);

        assertTrue(actualLocker.isPresent());
        assertEquals(expectedLocker, actualLocker.get());
        verify(lockerRepository, times(1)).findById(1L);
    }

    @Test
    void givenInvalidId_whenGetLockerById_thenReturnEmptyList() {
        Long id = 1L;
        when(lockerRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Locker> actualLocker = lockerService.getLockerById(id);

        assertFalse(actualLocker.isPresent());
    }

    @Test
    void givenValidAddress_whenGetLockerByAddress_thenReturnLocker() {
        String address = "Valid Address";
        Locker expectedLocker = new Locker();
        when(lockerRepository.findByAddress(address)).thenReturn(Optional.of(expectedLocker));

        Optional<Locker> actualLocker = lockerService.getLockerByAddress(address);

        assertTrue(actualLocker.isPresent());
        assertEquals(expectedLocker, actualLocker.get());
        verify(lockerRepository, times(1)).findByAddress("Valid Address");

    }

    @Test
    void givenInvalidAddress_whenGetLockerByAddress_thenReturnLocker() {
        String invalidAddress = "Invalid Address";

        when(lockerRepository.findByAddress(invalidAddress)).thenReturn(Optional.empty());

        Optional<Locker> result = lockerService.getLockerByAddress(invalidAddress);

        assertTrue(result.isEmpty());
        verify(lockerRepository, times(1)).findByAddress(invalidAddress);

    }


    @Test
    void givenValidData_whenCreateLocker_thenReturnSavedLocker() {
        Long id = 1L;
        String address = "Address 1";
        float capacity = 0.5f;

        when(lockerRepository.existsById(id)).thenReturn(false);

        boolean result = lockerService.createLocker(id, address, capacity);

        assertTrue(result);
    }

    @Test
    void givenExistingLockerId_whenUpdateLocker_thenReturnUpdatedLocker() {
        Long id = 1L;
        String newAddress = "New Address";
        float newCapacity = 10;
        Locker existingLocker = new Locker();
        existingLocker.setId(id);

        when(lockerRepository.findById(id)).thenReturn(Optional.of(existingLocker));
        when(lockerRepository.saveAndFlush(any(Locker.class))).thenReturn(existingLocker);

        boolean updateResult = lockerService.updateLocker(id, newAddress, newCapacity);

        verify(lockerRepository, times(1)).saveAndFlush(any(Locker.class));
        assertTrue(updateResult);
    }

    @Test
    void givenNonExistingLockerId_whenUpdateLocker_thenReturnFalse() {
        // Arrange
        Long id = 1L;
        String newAddress = "New Address";
        float newCapacity = 20;

        when(lockerRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        boolean updateResult = lockerService.updateLocker(id, newAddress, newCapacity);

        // Assert
        verify(lockerRepository, times(1)).findById(id);
        verify(lockerRepository, never()).save(any(Locker.class));
        assertFalse(updateResult);
    }


    @Test
    void givenExistingLockerId_whenDeleteLocker_thenDeleteLocker() {
        Long id = 1L;
        when(lockerRepository.existsById(id)).thenReturn(true);

        lockerService.deleteLocker(id);

        verify(lockerRepository, times(1)).deleteById(id);
    }

    @Test
    void givenNonExistingLockerId_whenDeleteLocker_thenThrowException() {
        Long id = 1L;
        when(lockerRepository.existsById(id)).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> lockerService.deleteLocker(id));
    }

}