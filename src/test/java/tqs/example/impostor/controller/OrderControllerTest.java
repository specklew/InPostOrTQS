package tqs.example.impostor.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tqs.example.impostor.models.ACP;
import tqs.example.impostor.models.Order;
import tqs.example.impostor.service.OrderService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mvc;    //entry point to the web framework

    // inject required beans as "mockeable" objects
    // note that @AutoWire would result in NoSuchBeanDefinitionException
    @MockBean
    private OrderService service;

    private ACP acp;
    private List<Order> orders = new ArrayList<>();

    @BeforeEach
    void setUp() {
        acp = new ACP();
        acp.setAddress("addr");
        acp.setCapacity(0.5f);

        Order order1 = new Order();
        order1.setId(1L);
        order1.setAcp(acp);
        order1.setOwner("owner1");
        order1.setShopName("shopName1");
        order1.setDeliverer("deliverer1");
        orders.add(order1);

        Order order2 = new Order();
        order2.setId(2L);
        order2.setAcp(acp);
        order2.setOwner("owner2");
        order2.setShopName("shopName2");
        order2.setDeliverer("deliverer2");
        orders.add(order2);

        Order order3 = new Order();
        order3.setId(3L);
        order3.setAcp(acp);
        order3.setOwner("owner1");
        order3.setShopName("shopName1");
        order3.setDeliverer("deliverer2");
        orders.add(order3);
    }

    @Test
    void givenParameters_whenPostCreateOrder_thenCreateOrder() throws Exception {
        when(service.createOrder(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);

        MvcResult result = mvc.perform(
                post("/order/create")
                        .param("acpAddress", "test")
                        .param("shopName", "name")
                        .param("owner", "own")
                        .param("deliverer", "deli"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).isEqualTo("true");
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,2})
    void givenPathVarId_whenGetOrderById_thenReturnOrder(int id) throws Exception {
        when(service.readOrder(id)).thenReturn(orders.get(id));

        mvc.perform(get("/order/get/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(orders.get(id).getId().intValue())))
                .andExpect(jsonPath("$.shopName", is(orders.get(id).getShopName())))
                .andExpect(jsonPath("$.acp.address", is(orders.get(id).getAcp().getAddress())))
                .andExpect(jsonPath("$.owner", is(orders.get(id).getOwner())))
                .andExpect(jsonPath("$.deliverer", is(orders.get(id).getDeliverer())))
                .andDo(print());
    }
}