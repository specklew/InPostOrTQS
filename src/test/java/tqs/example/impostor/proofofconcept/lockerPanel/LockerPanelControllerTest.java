package tqs.example.impostor.proofofconcept.lockerPanel;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import tqs.example.impostor.models.Order;
import tqs.example.impostor.proofofconcept.lockerPanel.LockerPanelController;
import tqs.example.impostor.proofofconcept.lockerPanel.LockerPanelHttpRequester;
import tqs.example.impostor.service.LockerService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LockerPanelController.class)
class LockerPanelControllerTest {

    @MockBean
    private LockerPanelHttpRequester httpRequester;

    @MockBean
    private LockerService lockerService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testIndex() throws Exception {
        mockMvc.perform(get("/locker/"))
                .andExpect(status().isOk())
                .andExpect(view().name("locker/lockerMainPage"));
    }

    @Test
    void testGetOrder_WithExistingOrder() throws Exception {
        Order order = new Order();
        order.setId(1L);

        when(httpRequester.getOrderFromRemoteServer(anyString())).thenReturn(Optional.of(order));

        mockMvc.perform(post("/locker/order/get")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("locker/lockerRetrievePackage"))
                .andExpect(model().attribute("order", order));
    }

    @Test
    void testGetOrder_WithNonExistingOrder() throws Exception {
        when(httpRequester.getOrderFromRemoteServer(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(post("/locker/order/get")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("locker/lockerRetrievePackage"))
                .andExpect(model().attributeDoesNotExist("order"));
    }
}
