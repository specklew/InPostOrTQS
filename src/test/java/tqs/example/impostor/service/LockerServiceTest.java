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
        expectedLockers.add(new Locker("Address 1", 10));
        expectedLockers.add(new Locker("Address 2", 20));
        when(lockerRepository.findAll()).thenReturn(expectedLockers);

        List<Locker> actualLockers = lockerService.getAllLockers();

        assertEquals(expectedLockers.size(), actualLockers.size());
        assertEquals(expectedLockers.get(0), actualLockers.get(0));
        assertEquals(expectedLockers.get(1), actualLockers.get(1));
    }

    @Test
    void givenValidId_whenGetLockerById_thenReturnLocker() {
        Long id = 1L;
        Locker expectedLocker = new Locker("Address 1", 10);
        when(lockerRepository.findById(id)).thenReturn(Optional.of(expectedLocker));

        Optional<Locker> actualLocker = lockerService.getLockerById(id);

        assertTrue(actualLocker.isPresent());
        assertEquals(expectedLocker, actualLocker.get());
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
    }

    @Test
    void givenInvalidAddress_whenGetLockerByAddress_thenReturnLocker() {
        String address = "Non-existent address";
        when(lockerRepository.findByAddress(address)).thenReturn(Optional.empty());

        Optional<Locker> actualLocker = lockerService.getLockerByAddress(address);

        assertFalse(actualLocker.isPresent());
    }

    @Test
    void givenValidData_whenCreateLocker_thenReturnSavedLocker() {
        Locker locker = new Locker("Address", 10);
        when(lockerRepository.save(locker)).thenReturn(locker);


        Locker savedLocker = lockerService.createLocker(locker);

        verify(lockerRepository, times(1)).save(locker);
        assertEquals(locker, savedLocker);
    }

    @Test
    void givenExistingLockerId_whenUpdateLocker_tenReturnUpdatedLocker() {
        Long id = 1L;
        String newAddress = "New Address";
        Integer newCapacity = 10;
        Locker existingLocker = new Locker();
        existingLocker.setId(id);

        when(lockerRepository.findById(id)).thenReturn(Optional.of(existingLocker));
        when(lockerRepository.save(any(Locker.class))).thenReturn(existingLocker);

        Locker updatedLocker = lockerService.updateLocker(id, newAddress, newCapacity);

        verify(lockerRepository, times(1)).save(existingLocker);
        assertEquals(newAddress, updatedLocker.getAddress());
        assertEquals(newCapacity, updatedLocker.getCapacity());
    }

    @Test
    void givenNonExistingLockerId_whenUpdateLocker_tenReturnNull() {
        Long id = 1L;
        String newAddress = "New Address";
        Integer newCapacity = 20;
        when(lockerRepository.findById(id)).thenReturn(Optional.empty());

        Locker result = lockerService.updateLocker(id, newAddress, newCapacity);

        verify(lockerRepository, times(1)).findById(id);
        verify(lockerRepository, never()).save(any());
        assertNull(result);
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