package tqs.example.impostor.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tqs.example.impostor.models.Locker;
import tqs.example.impostor.service.LockerService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LockerController.class)
class LockerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LockerService lockerService;

    private Locker locker;

    @BeforeEach
    public void setup() {
        locker = new Locker();
        locker.setId(1L);
        locker.setAddress("Address 1");
        locker.setCapacity(10);
    }

    @Test
    void givenAllLockers_thenReturnAllLockers() throws Exception {
        List<Locker> lockers = Arrays.asList(locker);

        when(lockerService.getAllLockers()).thenReturn(lockers);

        mockMvc.perform(get("/locker"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].address", is("Address 1")))
                .andExpect(jsonPath("$[0].capacity", is(10)));
    }

    @Test
    void givenValidId_whenGetLockerById_thenReturnLocker() throws Exception {
        when(lockerService.getLockerById(1L)).thenReturn(Optional.of(locker));

        mockMvc.perform(get("/locker/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.address", is("Address 1")))
                .andExpect(jsonPath("$.capacity", is(10)));
    }

    @Test
    void givenInvalidId_whenGetLockerById_thenReturnEmptyList() throws Exception {
        when(lockerService.getLockerById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/locker/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenValidLockerData_whenCreateLocker_thenReturnCreatedLocker() throws Exception {
        when(lockerService.createLocker(any(Locker.class))).thenReturn(locker);

        mockMvc.perform(post("/locker/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"address\":\"Address 1\",\"capacity\":10}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.address", is("Address 1")))
                .andExpect(jsonPath("$.capacity", is(10)));
    }

    @Test
    void givenExistingLockerId_whenUpdateLocker_tenReturnUpdatedLocker() throws Exception {
        when(lockerService.getLockerById(1L)).thenReturn(Optional.of(locker));
        when(lockerService.updateLocker(1L, "New Address", 20)).thenReturn(locker);

        mockMvc.perform(put("/locker/update/1")
                        .param("newAddress", "New Address")
                        .param("newCapacity", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.address", is("Address 1")))
                .andExpect(jsonPath("$.capacity", is(10)));
    }


    @Test
    void givenNonExistingLockerId_whenUpdateLocker_thenReturnNotFound() throws Exception {
        when(lockerService.getLockerById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/locker/update/2")
                        .param("newAddress", "New Address")
                        .param("newCapacity", "20"))
                .andExpect(status().isNotFound());

    }

    @Test
    void givenExistingLockerId_whenDeleteLocker_thenReturnNoContent() throws Exception {
        when(lockerService.getLockerById(1L)).thenReturn(Optional.of(locker));

        mockMvc.perform(delete("/locker/delete/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void givenNonExistingLockerId_whenDeleteLocker_thenReturnNotFound() throws Exception {
        when(lockerService.getLockerById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/locker/delete/2"))
                .andExpect(status().isNotFound());
    }
}