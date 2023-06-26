package tqs.example.impostor.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tqs.example.impostor.models.Locker;
import tqs.example.impostor.service.LockerService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LockerController.class)
class LockerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LockerService lockerService;

    private Locker locker;

    @BeforeEach
    void setup() {
        locker = new Locker();
        locker.setId(1L);
        locker.setAddress("Address 1");
        locker.setCapacity(0.5f);
    }

    @Test
    void givenAllLockers_thenReturnAllLockers() throws Exception {
        List<Locker> lockers = Arrays.asList(locker);

        when(lockerService.getAllLockers()).thenReturn(lockers);

        mockMvc.perform(get("/locker"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].address").value("Address 1"))
                .andExpect(jsonPath("$[0].capacity").value(0.5f));
    }

    @Test
    void givenValidId_whenGetLockerById_thenReturnLocker() throws Exception {
        when(lockerService.getLockerById(1L)).thenReturn(Optional.of(locker));

        mockMvc.perform(get("/locker/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.address").value("Address 1"))
                .andExpect(jsonPath("$.capacity").value(0.5f));
    }

    @Test
    void givenInvalidId_whenGetLockerById_thenReturnEmptyList() throws Exception {
        when(lockerService.getLockerById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/locker/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenValidLockerData_whenCreateLocker_thenReturnCreatedLocker() throws Exception {
        when(lockerService.createLocker(Mockito.anyLong(), Mockito.anyString(), Mockito.anyFloat())).thenReturn(true);

        mockMvc.perform(post("/locker/create")
                        .param("id", String.valueOf(1L))
                        .param("address", "New Address")
                        .param("capacity", String.valueOf(0.5f)))
                .andExpect(status().isCreated());
    }

    @Test
    void givenInvalidLockerData_whenCreateLocker_thenReturnEmptyContent() throws Exception {
        when(lockerService.createLocker(Mockito.anyLong(), Mockito.anyString(), Mockito.anyFloat())).thenReturn(false);

        MvcResult result = mockMvc.perform(post("/locker/create")
                        .param("id", String.valueOf(1L))
                        .param("address", "Invalid Address")
                        .param("capacity", String.valueOf(0.8f)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).isEmpty();
    }

    @Test
    void givenExistingLockerId_whenUpdateLocker_tenReturnUpdatedLocker() throws Exception {
        when(lockerService.getLockerById(any())).thenReturn(Optional.of(locker));
        when(lockerService.updateLocker(Mockito.anyLong(), Mockito.anyString(), Mockito.anyFloat())).thenReturn(true);

        MvcResult result = mockMvc.perform(put("/locker/update/{id}", String.valueOf(1L))
                        .param("address", "Updated Address")
                        .param("capacity", String.valueOf(0.9f)))
                .andExpect(status().isOk())
                .andReturn();

        boolean updatedACP = Boolean.parseBoolean(result.getResponse().getContentAsString());
        assertThat(updatedACP).isTrue();
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