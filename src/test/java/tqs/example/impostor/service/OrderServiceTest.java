package tqs.example.impostor.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs.example.impostor.repository.ACP;
import tqs.example.impostor.repository.ACPRepository;
import tqs.example.impostor.repository.Order;
import tqs.example.impostor.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    // No documentation on why lenient is deprecated was found. Don't know what else to use, so I'm sticking with it.
    // Possibly will change it later, if there is time for it.
    @Mock(lenient = true)
    private OrderRepository orderRepository;
    @Mock(lenient = true)
    private ACPRepository acpRepository;

    @InjectMocks
    private OrderService service;

    private ACP acp;
    private List<Order> orders = new ArrayList<>();

    @BeforeEach
    void setUp() {
        acp = new ACP();
        acp.setAddress("addr");
        acp.setCapacity(0.5f);

        Order order1 = new Order();
        order1.setAcp(acp);
        order1.setOwner("owner1");
        order1.setShopName("shopName1");
        order1.setDeliverer("deliverer1");
        orders.add(order1);

        Order order2 = new Order();
        order2.setAcp(acp);
        order2.setOwner("owner2");
        order2.setShopName("shopName2");
        order2.setDeliverer("deliverer2");
        orders.add(order2);

        Order order3 = new Order();
        order3.setAcp(acp);
        order3.setOwner("owner1");
        order3.setShopName("shopName1");
        order3.setDeliverer("deliverer2");
        orders.add(order3);

        when(acpRepository.findByAddress(any(String.class))).thenReturn(Optional.of(acp));

        for(Order order : orders){
            when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        }
    }

    @Test
    void createOrder() {
    }

    @Test
    void readOrder() {
    }

    @Test
    void readOrdersByACPAddress() {
    }

    @Test
    void readOrdersByOwner() {
    }

    @Test
    void readOrdersByShopName() {
    }

    @Test
    void readOrdersByDeliverer() {
    }

    @Test
    void updateOrder() {
    }

    @Test
    void deleteOrder() {
    }
}