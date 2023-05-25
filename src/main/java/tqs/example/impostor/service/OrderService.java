package tqs.example.impostor.service;

import org.springframework.stereotype.Service;
import tqs.example.impostor.repository.ACP;
import tqs.example.impostor.repository.ACPRepository;
import tqs.example.impostor.repository.Order;
import tqs.example.impostor.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    OrderRepository orderRepository;
    ACPRepository acpRepository;

    public OrderService(OrderRepository orderRepository, ACPRepository acpRepository) {
        this.orderRepository = orderRepository;
        this.acpRepository = acpRepository;
    }

    //Create.
    public boolean createOrder(String acpAddress, String shopName, String owner, String deliverer) {

        Optional<ACP> acp = acpRepository.findByAddress(acpAddress);
        if (acp.isEmpty()) return false;

        Order order = new Order();
        order.setAcp(acp.get());
        order.setShopName(shopName);
        order.setOwner(owner);
        order.setDeliverer(deliverer);

        orderRepository.saveAndFlush(order);

        return true;
    }

    //Read.
    //Probably we should have methods for each of Order attributes, but it takes time to do so.
    public Order readOrder(long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.orElse(null);
    }

    public List<Order> readOrdersByACPAddress(String acpAddress){
        Optional<ACP> acp = acpRepository.findByAddress(acpAddress);
        return acp.map(value -> orderRepository.findAllByAcp(value)).orElse(null);
    }

    public List<Order> readOrdersByOwner(String owner){
        return orderRepository.findAllByOwner(owner);
    }

    public List<Order> readOrdersByShopName(String shopName){
        return orderRepository.findAllByShopName(shopName);
    }

    public List<Order> readOrdersByDeliverer(String deliverer){
        return orderRepository.findAllByDeliverer(deliverer);
    }

}
