package tqs.example.impostor.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs.example.impostor.models.ACP;
import tqs.example.impostor.models.Locker;
import tqs.example.impostor.models.Order;
import tqs.example.impostor.repository.ACPRepository;
import tqs.example.impostor.repository.LockerRepository;
import tqs.example.impostor.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    // No documentation on why lenient is deprecated was found. Don't know what else to use, so I'm sticking with it.
    // Possibly will change it later, if there is time for it.
    @Mock(lenient = true)
    private OrderRepository orderRepository;
    @Mock(lenient = true)
    private ACPRepository acpRepository;
    @Mock(lenient = true)
    private LockerRepository lockerRepository;

    @InjectMocks
    private OrderService service;

    private ACP acp;
    private Locker locker;
    private final List<Order> orders = new ArrayList<>();

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

        when(acpRepository.findByAddress(acp.getAddress())).thenReturn(Optional.of(acp));
        when(acpRepository.findByAddress("wrong")).thenReturn(Optional.empty());
        when(lockerRepository.findByAddress(locker.getAddress())).thenReturn(Optional.of(locker));
        when(lockerRepository.findByAddress("wrong")).thenReturn(Optional.empty());

        for(Order order : orders){
            when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
            when(orderRepository.existsById(order.getId())).thenReturn(true);
        }

        when(orderRepository.findAll()).thenReturn(orders);
        when(orderRepository.findAllByAcp(acp)).thenReturn(orders);
        when(orderRepository.findAllByLocker(locker)).thenReturn(orders);
        when(orderRepository.findAllByOwner("owner1")).thenReturn(List.of(order1, order3));
        when(orderRepository.findAllByOwner("owner2")).thenReturn(List.of(order2));
        when(orderRepository.findAllByShopName("shopName1")).thenReturn(List.of(order1, order3));
        when(orderRepository.findAllByShopName("shopName2")).thenReturn(List.of(order2));
        when(orderRepository.findAllByDeliverer("deliverer1")).thenReturn(List.of(order1));
        when(orderRepository.findAllByDeliverer("deliverer2")).thenReturn(List.of(order2, order3));
    }

    @Test
    void givenCorrectData_whenCreateOrder_thenReturnTrue() {
        assertThat(service.createOrder(acp.getAddress(), "correct", "correct", null)).isTrue();
    }

    @Test
    void givenWrongData_whenCreateOrder_thenReturnFalse() {
        assertThat(service.createOrder(null, null, null, null)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,2})
    void givenOrderId_whenReadOrder_thenReturnOrder(int id) {
        assertThat(service.readOrder(orders.get(id).getId())).isEqualTo(orders.get(id));
    }

    @Test
    void givenWrongOrderId_whenReadOrder_thenReturnNull() {
        assertThat(service.readOrder(-1L)).isNull();
    }

    @Test
    void whenReadAllOrders_thenReturnListOfOrders() {
        assertThat(service.readAllOrders()).isEqualTo(orders);
    }

    @Test
    void givenACPAddress_whenReadOrdersByACPAddress_thenReturnListOfOrders() {
        assertThat(service.readOrdersByACPAddress(acp.getAddress())).isEqualTo(orders);
    }

    @Test
    void givenWrongACPAddress_whenReadOrdersByACPAddress_thenReturnEmptyList() {
        assertThat(service.readOrdersByACPAddress("wrong")).isNull();
    }

    @Test
    void givenLockerAddress_whenReadOrdersByLockerAddress_thenReturnListOfOrders() {
        assertThat(service.readOrdersByLockerAddress(locker.getAddress())).isEqualTo(orders);
    }

    @Test
    void givenWrongLockerAddress_whenReadOrdersByLockerAddress_thenReturnEmptyList() {
        assertThat(service.readOrdersByLockerAddress("wrong")).isNull();
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,2})
    void givenOwner_whenReadOrdersByOwner_thenReturnListOfOrders(int id) {

        List<Order> orderList = new ArrayList<>();

        for(Order order : orders){
            if(Objects.equals(order.getOwner(), orders.get(id).getOwner())){
                orderList.add(order);
            }
        }

        assertThat(service.readOrdersByOwner(orders.get(id).getOwner()))
                .asList().containsExactlyInAnyOrderElementsOf(orderList);
    }

    @Test
    void givenWrongOwner_whenReadOrdersByOwner_thenReturnEmptyList() {
        assertThat(service.readOrdersByOwner("wrong")).asList().isEmpty();
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,2})
    void givenShopName_whenReadOrdersByShopName_thenReturnListOfOrders(int id) {

        List<Order> orderList = new ArrayList<>();

        for(Order order : orders){
            if(Objects.equals(order.getShopName(), orders.get(id).getShopName())){
                orderList.add(order);
            }
        }

        assertThat(service.readOrdersByShopName(orders.get(id).getShopName()))
                .asList().containsExactlyInAnyOrderElementsOf(orderList);
    }

    @Test
    void givenWrongShopName_whenReadOrdersByShopName_thenReturnEmptyList() {
        assertThat(service.readOrdersByShopName("wrong")).asList().isEmpty();
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,2})
    void givenDeliverer_whenReadOrdersByDeliverer_thenReturnListOfOrders(int id) {

        List<Order> orderList = new ArrayList<>();

        for(Order order : orders){
            if(Objects.equals(order.getDeliverer(), orders.get(id).getDeliverer())){
                orderList.add(order);
            }
        }

        assertThat(service.readOrdersByDeliverer(orders.get(id).getDeliverer()))
                .asList().containsExactlyInAnyOrderElementsOf(orderList);
    }

    @Test
    void givenWrongDeliverer_whenReadOrdersByDeliverer_thenReturnEmptyList() {
        assertThat(service.readOrdersByDeliverer("wrong")).asList().isEmpty();
    }

    @Test
    void givenCorrectData_whenUpdateOrder_thenReturnTrue() {
        assertThat(service.updateOrder(
                orders.get(0).getId(),
                acp.getAddress(),
                "name",
                "own",
                "deli")).isTrue();
    }

    @Test
    void givenWrongACP_whenUpdateOrder_thenReturnFalse() {
        assertThat(service.updateOrder(
                orders.get(0).getId(),
                "wrong",
                null,
                null,
                null)).isFalse();
    }

    @Test
    void givenWrongId_whenUpdateOrder_thenReturnFalse() {
        assertThat(service.updateOrder(
                -1L,
                acp.getAddress(),
                null,
                null,
                null)).isFalse();
    }

    @Test
    void givenAcceptableNullData_whenUpdateOrder_thenReturnTrue() {
        assertThat(service.updateOrder(
                orders.get(0).getId(),
                null,
                null,
                null,
                null)).isTrue();
    }

    @Test
    void givenId_whenDeleteOrder_thenReturnTrue() {
        assertThat(service.deleteOrder(orders.get(0).getId())).isTrue();
    }

    @Test
    void givenWrongId_whenDeleteOrder_thenReturnFalse() {
        assertThat(service.deleteOrder(-1L)).isFalse();
    }
}