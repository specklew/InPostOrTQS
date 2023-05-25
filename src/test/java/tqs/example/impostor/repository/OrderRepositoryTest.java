package tqs.example.impostor.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ACPRepository acpRepository;

    private final List<Order> orderList = new ArrayList<>();
    private final List<ACP> acpList = new ArrayList<>();

    @BeforeEach
    void setUp() {

        ACP acp0 = new ACP("Example Address 404", 10f);
        ACP acp1 = new ACP("Second One 202", 5f);

        acpList.add(acp0);
        acpList.add(acp1);

        for (ACP acp : acpList) {
            acpRepository.saveAndFlush(acp);
        }

        Order order0 = new Order();
        order0.setOwner("Mark Watney");
        order0.setShopName("PokeShop");
        order0.setDeliverer("DeliverPolEx");
        order0.setAcp(acp0);

        Order order1 = new Order();
        order1.setOwner("Ben Dover");
        order1.setShopName("PokeShop");
        order1.setDeliverer("FedPolEx");
        order1.setAcp(acp1);

        Order order2 = new Order();
        order2.setOwner("Ben Dover");
        order2.setShopName("Shady");
        order2.setDeliverer("DeliverPolEx");
        order2.setAcp(acp0);

        orderList.add(order0);
        orderList.add(order1);
        orderList.add(order2);

        for (Order order : orderList) {
            orderRepository.saveAndFlush(order);
        }
    }

    @Test
    void givenCorrectACP_whenFindAllByACP_thenReturnListOfOrders() {
        assertThat(orderRepository.findAllByAcp(acpList.get(0)))
                .hasSize(2)
                .hasOnlyElementsOfType(Order.class)
                .contains(orderList.get(0), orderList.get(2));
    }

    @Test
    void givenWrongACP_whenFindAllByACP_thenReturnEmptyList() {
        acpRepository.saveAndFlush(new ACP("2", 11f));
        Optional<ACP> acp = acpRepository.findByAddress("2");
        if(acp.isEmpty()) Assertions.fail();
        assertThat(orderRepository.findAllByAcp(acp.get())).isEmpty();
    }

    @Test
    void givenCorrectOwner_whenFindAllByOwner_thenReturnListOfOrders() {
        assertThat(orderRepository.findAllByOwner(orderList.get(1).getOwner()))
                .hasSize(2)
                .hasOnlyElementsOfType(Order.class)
                .contains(orderList.get(1), orderList.get(2));
    }

    @Test
    void givenWrongOwner_whenFindAllByOwner_thenReturnEmptyList() {
        assertThat(orderRepository.findAllByOwner("wrong")).isEmpty();
    }

    @Test
    void givenCorrectShopName_whenFindAllByShopName_thenReturnListOfOrders() {
        assertThat(orderRepository.findAllByShopName(orderList.get(0).getShopName()))
                .hasSize(2)
                .hasOnlyElementsOfType(Order.class)
                .contains(orderList.get(0), orderList.get(1));
    }

    @Test
    void givenWrongShopName_whenFindAllByShopName_thenReturnEmptyList() {
        assertThat(orderRepository.findAllByShopName("wrong")).isEmpty();
    }

    @Test
    void givenCorrectDeliverer_whenFindAllByDeliverer_thenReturnListOfOrders() {
        assertThat(orderRepository.findAllByDeliverer(orderList.get(1).getDeliverer()))
                .hasSize(1)
                .hasOnlyElementsOfType(Order.class)
                .contains(orderList.get(1));
    }

    @Test
    void givenWrongDeliverer_whenFindAllByDeliverer_thenReturnEmptyList() {
        assertThat(orderRepository.findAllByDeliverer("wrong")).isEmpty();
    }
}
