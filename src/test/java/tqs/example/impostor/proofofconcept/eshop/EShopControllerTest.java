package tqs.example.impostor.proofofconcept.eshop;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EShopController.class)
class EShopControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EShopHttpRequester requester;

    List<String> addresses = new ArrayList<>();

    @BeforeEach
    void setUp() throws IOException, ParseException, URISyntaxException {
        addresses.add("R. Dos Marnotos");

        when(requester.getAcpAddressesFromRemoteServer()).thenReturn(addresses);
        when(requester.postNewOrder(any(), any(), any(), any())).thenReturn(true);
    }

    @Test
    void testEmptyConstructor() {
        EShopHttpRequester requester1 = new EShopHttpRequester();
    }

    @Test
    public void whenRequestIndex_thenReturnStatusOk() throws Exception {

        mockMvc.perform(get("/poc/eshop").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenRequestCart_thenReturnAcpInDropdown() throws Exception {
        mockMvc.perform(get("/poc/eshop/cart").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(model().attribute("acpList", addresses));
    }

    @Test
    void whenPostOrder_thenReturnTrue() throws Exception {
        mockMvc.perform(post("/poc/eshop/order")
                        .param("address", "add")
                        .param("owner", "own")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(model().attribute("postResult", true));
    }
}