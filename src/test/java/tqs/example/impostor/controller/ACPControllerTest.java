package tqs.example.impostor.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tqs.example.impostor.models.ACP;
import tqs.example.impostor.service.ACPService;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ACPController.class)
class ACPControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ACPService acpService;

    private ACP acp;

   @BeforeEach
   void setUp(){
       acp = new ACP();
       acp.setId(1L);
       acp.setAddress("Address 1");
       acp.setCapacity(0.5f);

   }

    @Test
    void givenAllACPs_thenReturnAllACPs() throws Exception {
        List<ACP> acps = Collections.singletonList(acp);
        when(acpService.getAllACPs()).thenReturn(acps);

        mockMvc.perform(get("/acp/get/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].address", is("Address 1")))
                .andExpect(jsonPath("$[0].capacity", is(0.5)));
    }

    @Test
    void givenValidId_whenGetACPById_thenReturnACP() throws Exception {
        when(acpService.getACPById(1L)).thenReturn(Optional.of(acp));

        mockMvc.perform(get("/acp/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.address").value("Address 1"))
                .andExpect(jsonPath("$.capacity").value(0.5));
    }

    @Test
    void givenInvalidId_whenGetACPById_thenReturnEmptyList() throws Exception {
        when(acpService.getACPById(1L)).thenReturn(Optional.empty());

        MvcResult result = mockMvc.perform(get("/acp/{id}", 1L))
                .andExpect(status().isNotFound())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).isEmpty();
    }

    @Test
    void givenValidAddress_whenGetACPByAddress_thenReturnACP() throws Exception {
        String address = "Address 1";
        when(acpService.getACPByAddress(address)).thenReturn(Optional.of(acp));

        mockMvc.perform(get("/acp/address/{address}", address))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address", is("Address 1")))
                .andExpect(jsonPath("$.capacity", is(0.5)));

    }

    @Test
    void givenInValidAddress_whenGetACPByAddress_thenReturnEmptyList() throws Exception {
        String address = "Invalid Address";
        when(acpService.getACPByAddress(address)).thenReturn(Optional.empty());

        MvcResult result = mockMvc.perform(get("/acp/address/{address}", address))
                .andExpect(status().isNotFound())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).isEmpty();
    }

    @Test
    void givenValidACPData_whenCreateACP_thenReturnCreatedACP() throws Exception {
        when(acpService.createACP(Mockito.anyLong(), Mockito.anyString(), Mockito.anyFloat())).thenReturn(true);

        mockMvc.perform(post("/acp/create")
                        .param("id", String.valueOf(1L))
                        .param("address", "New Address")
                        .param("capacity", String.valueOf(0.8f)))
                .andExpect(status().isCreated());
    }


    @Test
    void givenInvalidACPData_whenCreateACP_thenReturnEmptyContent() throws Exception {
        when(acpService.createACP(Mockito.anyLong(), Mockito.anyString(), Mockito.anyFloat())).thenReturn(false);

        MvcResult result = mockMvc.perform(post("/acp/create")
                        .param("id", String.valueOf(1L))
                        .param("address", "Invalid Address")
                        .param("capacity", String.valueOf(0.8f)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).isEmpty();
    }
    
    @Test
    void givenExistingACPId_whenUpdateACP_thenReturnUpdatedACP() throws Exception {
        when(acpService.getACPById(any())).thenReturn(Optional.of(acp));
        when(acpService.updateACP(Mockito.anyLong(), Mockito.anyString(), Mockito.anyFloat())).thenReturn(true);

        MvcResult result = mockMvc.perform(put("/acp/update/{id}", String.valueOf(1L))
                        .param("address", "Updated Address")
                        .param("capacity", String.valueOf(0.9f)))
                .andExpect(status().isOk())
                .andReturn();

        boolean updatedACP = Boolean.parseBoolean(result.getResponse().getContentAsString());
        assertThat(updatedACP).isTrue();
    }


    @Test
    void givenNonExistingACPId_whenUpdateACP_tenReturnNotFound() throws Exception {
        when(acpService.updateACP(Mockito.anyLong(), Mockito.anyString(), Mockito.anyFloat())).thenReturn(false);

        MvcResult result = mockMvc.perform(put("/acp/update/{id}", String.valueOf(1L))
                        .param("address", "Invalid Address")
                        .param("capacity", String.valueOf(1.0f)))
                .andExpect(status().isNotFound())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).isEmpty();
    }

    @Test
    void givenExistingACP_whenDeleteACP_thenDeleteACP() throws Exception {
    when(acpService.deleteACP(Mockito.any(Long.class))).thenReturn(true);

        MvcResult result = mockMvc.perform(delete("/acp/delete/{id}", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).isEqualTo("true");
   }
}


