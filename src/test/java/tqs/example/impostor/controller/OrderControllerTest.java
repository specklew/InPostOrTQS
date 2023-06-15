package tqs.example.impostor.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tqs.example.impostor.models.ACP;
import tqs.example.impostor.models.Locker;
import tqs.example.impostor.models.Order;
import tqs.example.impostor.service.OrderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mvc;    //entry point to the web framework

    // inject required beans as "mockeable" objects
    // note that @AutoWire would result in NoSuchBeanDefinitionException
    @MockBean
    private OrderService service;

    private ACP acp;
    private Locker locker;
    private List<Order> orders = new ArrayList<>();

    @BeforeEach
    void setUp() {
        acp = new ACP();
        acp.setAddress("addr");
        acp.setCapacity(0.5f);

        locker = new Locker();
        locker.setAddress("lock");
        locker.setCapacity(10);

        Order order1 = new Order();
        order1.setId(1L);
        order1.setAcp(acp);
        order1.setOwner("owner1");
        order1.setShopName("shopName1");
        order1.setDeliverer("deliverer1");
        order1.setLocker(locker);
        orders.add(order1);

        Order order2 = new Order();
        order2.setId(2L);
        order2.setAcp(acp);
        order2.setOwner("owner2");
        order2.setShopName("shopName2");
        order2.setDeliverer("deliverer2");
        order2.setLocker(locker);
        orders.add(order2);

        Order order3 = new Order();
        order3.setId(3L);
        order3.setAcp(acp);
        order3.setOwner("owner1");
        order3.setShopName("shopName1");
        order3.setDeliverer("deliverer2");
        order3.setLocker(locker);
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
                .andExpect(jsonPath("$.deliverer", is(orders.get(id).getDeliverer())));
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,2})
    void givenAcpAddress_whenGetOrdersByAcpAddress_thenReturnListOfOrders(int id) throws Exception {

        List<Order> orderList = new ArrayList<>();

        for(Order order : orders){
            if(Objects.equals(order.getAcp(), orders.get(id).getAcp())){
                orderList.add(order);
            }
        }

        when(service.readOrdersByACPAddress(orders.get(id).getAcp().getAddress())).thenReturn(orderList);

        mvc.perform(get("/order/getByACPAddress/" + orders.get(id).getAcp().getAddress()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id",
                        containsInAnyOrder(orderList.stream().map(order -> order.getId().intValue()).toArray())))
                .andExpect(jsonPath("$[*].shopName",
                        containsInAnyOrder(orderList.stream().map(Order::getShopName).toArray())))
                .andExpect(jsonPath("$[*].acp.address",
                        containsInAnyOrder(orderList.stream().map(order -> order.getAcp().getAddress()).toArray())))
                .andExpect(jsonPath("$[*].owner",
                        containsInAnyOrder(orderList.stream().map(Order::getOwner).toArray())))
                .andExpect(jsonPath("$[*].deliverer",
                        containsInAnyOrder(orderList.stream().map(Order::getDeliverer).toArray())));
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,2})
    void givenLockerAddress_whenGetOrdersByLockerAddress_thenReturnListOfOrders(int id) throws Exception {

        List<Order> orderList = new ArrayList<>();

        for(Order order : orders){
            if(Objects.equals(order.getLocker(), orders.get(id).getLocker())){
                orderList.add(order);
            }
        }

        when(service.readOrdersByLockerAddress(orders.get(id).getLocker().getAddress())).thenReturn(orderList);

        mvc.perform(get("/order/getByLockerAddress/" + orders.get(id).getLocker().getAddress()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id",
                        containsInAnyOrder(orderList.stream().map(order -> order.getId().intValue()).toArray())))
                .andExpect(jsonPath("$[*].shopName",
                        containsInAnyOrder(orderList.stream().map(Order::getShopName).toArray())))
                .andExpect(jsonPath("$[*].acp.address",
                        containsInAnyOrder(orderList.stream().map(order -> order.getAcp().getAddress()).toArray())))
                .andExpect(jsonPath("$[*].owner",
                        containsInAnyOrder(orderList.stream().map(Order::getOwner).toArray())))
                .andExpect(jsonPath("$[*].deliverer",
                        containsInAnyOrder(orderList.stream().map(Order::getDeliverer).toArray())));
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,2})
    void givenOwner_whenGetOrdersByOwner_thenReturnListOfOrders(int id) throws Exception {

        List<Order> orderList = new ArrayList<>();

        for(Order order : orders){
            if(Objects.equals(order.getOwner(), orders.get(id).getOwner())){
                orderList.add(order);
            }
        }

        when(service.readOrdersByOwner(orders.get(id).getOwner())).thenReturn(orderList);

        mvc.perform(get("/order/getByOwner/" + orders.get(id).getOwner()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id",
                        containsInAnyOrder(orderList.stream().map(order -> order.getId().intValue()).toArray())))
                .andExpect(jsonPath("$[*].shopName",
                        containsInAnyOrder(orderList.stream().map(Order::getShopName).toArray())))
                .andExpect(jsonPath("$[*].acp.address",
                        containsInAnyOrder(orderList.stream().map(order -> order.getAcp().getAddress()).toArray())))
                .andExpect(jsonPath("$[*].owner",
                        containsInAnyOrder(orderList.stream().map(Order::getOwner).toArray())))
                .andExpect(jsonPath("$[*].deliverer",
                        containsInAnyOrder(orderList.stream().map(Order::getDeliverer).toArray())));
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,2})
    void givenShopName_whenGetOrdersByShopName_thenReturnListOfOrders(int id) throws Exception {

        List<Order> orderList = new ArrayList<>();

        for(Order order : orders){
            if(Objects.equals(order.getShopName(), orders.get(id).getShopName())){
                orderList.add(order);
            }
        }

        when(service.readOrdersByShopName(orders.get(id).getShopName())).thenReturn(orderList);

        mvc.perform(get("/order/getByShopName/" + orders.get(id).getShopName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id",
                        containsInAnyOrder(orderList.stream().map(order -> order.getId().intValue()).toArray())))
                .andExpect(jsonPath("$[*].shopName",
                        containsInAnyOrder(orderList.stream().map(Order::getShopName).toArray())))
                .andExpect(jsonPath("$[*].acp.address",
                        containsInAnyOrder(orderList.stream().map(order -> order.getAcp().getAddress()).toArray())))
                .andExpect(jsonPath("$[*].owner",
                        containsInAnyOrder(orderList.stream().map(Order::getOwner).toArray())))
                .andExpect(jsonPath("$[*].deliverer",
                        containsInAnyOrder(orderList.stream().map(Order::getDeliverer).toArray())));
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,2})
    void givenDeliverer_whenGetOrdersByDeliverer_thenReturnListOfOrders(int id) throws Exception {

        List<Order> orderList = new ArrayList<>();

        for(Order order : orders){
            if(Objects.equals(order.getDeliverer(), orders.get(id).getDeliverer())){
                orderList.add(order);
            }
        }

        when(service.readOrdersByDeliverer(orders.get(id).getDeliverer())).thenReturn(orderList);

        mvc.perform(get("/order/getByDeliverer/" + orders.get(id).getDeliverer()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id",
                        containsInAnyOrder(orderList.stream().map(order -> order.getId().intValue()).toArray())))
                .andExpect(jsonPath("$[*].shopName",
                        containsInAnyOrder(orderList.stream().map(Order::getShopName).toArray())))
                .andExpect(jsonPath("$[*].acp.address",
                        containsInAnyOrder(orderList.stream().map(order -> order.getAcp().getAddress()).toArray())))
                .andExpect(jsonPath("$[*].owner",
                        containsInAnyOrder(orderList.stream().map(Order::getOwner).toArray())))
                .andExpect(jsonPath("$[*].deliverer",
                        containsInAnyOrder(orderList.stream().map(Order::getDeliverer).toArray())));
    }

    @Test
    void givenParameters_whenPostUpdateOrder_thenUpdateOrder() throws Exception {
        when(service.updateOrder(Mockito.any(Long.class), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);

        MvcResult result = mvc.perform(
                        post("/order/update")
                                .param("id", "1")
                                .param("acpAddress", "test")
                                .param("shopName", "name")
                                .param("owner", "own")
                                .param("deliverer", "deli"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).isEqualTo("true");
    }

    @Test
    void givenPathVar_whenPostDeleteOrder_thenDeleteOrder() throws Exception {
        when(service.deleteOrder(Mockito.any(Long.class))).thenReturn(true);

        MvcResult result = mvc.perform(
                        post("/order/delete/1"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).isEqualTo("true");
    }
}