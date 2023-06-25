package tqs.example.impostor.proofofconcept.adminPanel;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tqs.example.impostor.models.ACP;
import tqs.example.impostor.models.Order;
import tqs.example.impostor.service.AdminService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ComponentScan("tqs.example.impostor.proofofconcept.adminPanel")
class AdminPanelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminPanelHttpRequester requester;

    @MockBean
    private AdminService adminService;

    @Test
    public void testIndex() throws Exception {
        mockMvc.perform(get("/adminPanel"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("adminPanel/admin_login"));
    }

    @Test
    void testLogin_WithValidCredentials() throws Exception {
        // Mock the behavior of the adminService.login() method
        when(adminService.login("admin", "password")).thenReturn("ACP");

        mockMvc.perform(post("/adminPanel/logged")
                        .param("username", "admin")
                        .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("adminPanel/ACP"));
    }

    @Test
    void testLogin_WithInvalidCredentials() throws Exception {
        // Mock the behavior of the adminService.login() method
        when(adminService.login("admin", "wrongpassword")).thenReturn("INVALID");

        mockMvc.perform(post("/adminPanel/logged")
                        .param("username", "admin")
                        .param("password", "wrongpassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/adminPanel"));
    }

    @Test
    void testACPPage() throws Exception {
        mockMvc.perform(get("/adminPanel/adminPanel/ACP"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminPanel/ACP"));
    }

    @Test
    void testDeliveriesPage() throws Exception {
        mockMvc.perform(get("/adminPanel/adminPanel/deliveries"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminPanel/deliveries"));
    }

    @Test
    void testSearchACP_WithExistingACP() throws Exception {
        ACP acp = new ACP();
        acp.setId(1L);
        acp.setAddress("R. Dos Marnotos 58, Aveiro");
        acp.setCapacity(1.2f);
        when(requester.getAcpFromRemoteServer(anyString())).thenReturn(Optional.of(acp));

        mockMvc.perform(post("/adminPanel/ACP/search")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminPanel/ACP"))
                .andExpect(model().attribute("acp", acp));
    }

    @Test
    void testSearchACP_WithNonExistingACP() throws Exception {
        when(requester.getAcpFromRemoteServer(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(post("/adminPanel/ACP/search")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminPanel/ACP"))
                .andExpect(model().attributeDoesNotExist("acp"));
    }

    @Test
    void testAddACP_Successful() throws Exception {
        mockMvc.perform(post("/adminPanel/ACP/add")
                        .param("id", "1")
                        .param("address", "R. Dos Marnotos 58, Aveiro")
                        .param("capacity", "1.2"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminPanel/ACP"));
    }

    @Test
    void testAddACP_Failure() throws Exception {
        mockMvc.perform(post("/adminPanel/ACP/add")
                        .param("id", "1")
                        .param("address", "R. Dos Marnotos 58, Aveiro")
                        .param("capacity", "1.2"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminPanel/ACP"));
    }

    @Test
    void testGetOrders() throws Exception {
        List<Order> orders = Arrays.asList(new Order(), new Order());
        when(requester.getOrdersFromRemoteServer()).thenReturn(orders);

        mockMvc.perform(get("/adminPanel/deliveries"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminPanel/deliveries"))
                .andExpect(model().attribute("orders", orders));
    }

}