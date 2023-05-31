package tqs.example.impostor.service;

import org.springframework.stereotype.Service;
import tqs.example.impostor.models.ACP;
import tqs.example.impostor.models.Locker;
import tqs.example.impostor.models.Order;
import tqs.example.impostor.repository.*;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ACPRepository acpRepository;
    private final LockerRepository lockerRepository;

    public OrderService(OrderRepository orderRepository, ACPRepository acpRepository, LockerRepository lockerRepository) {
        this.orderRepository = orderRepository;
        this.acpRepository = acpRepository;
        this.lockerRepository = lockerRepository;
    }

    //Create.
    /**
     *
     * Creates a new order with provided values and saves and flushes it to the database.
     *
     * @param acpAddress Address of the ACP point.
     * @param shopName Name of the shop that the order is sent from.
     * @param owner Owner of that order (the one who ordered a package).
     * @param deliverer Delivery service that should deliver the package. (Can be {@code null}).
     * @return - True if acp with provided address exits and the order is successfully saved, false otherwise.
     */
    public boolean createOrder(String acpAddress, String shopName, String owner, String deliverer) {

        if(shopName == null || owner == null) return false;

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
    /**
     * @param id The id of the order.
     * @return - Order if it exists, null if it does not.
     */
    public Order readOrder(long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.orElse(null);
    }

    /**
     * @param acpAddress The address of the ACP that the orders are sent to.
     * @return - List of orders that were sent to that ACP.
     */
    public List<Order> readOrdersByACPAddress(String acpAddress){
        Optional<ACP> acp = acpRepository.findByAddress(acpAddress);
        return acp.map(value -> orderRepository.findAllByAcp(value)).orElse(null);
    }

    public List<Order> readOrdersByLockerAddress(String lockerAddress){
        Optional<Locker> locker = lockerRepository.findByAddress(lockerAddress);
        return locker.map(value -> orderRepository.findAllByLocker(value)).orElse(null);
    }

    /**
     * @param owner The owner of the orders.
     * @return - List of orders that belong to the provided owner.
     */
    public List<Order> readOrdersByOwner(String owner){
        return orderRepository.findAllByOwner(owner);
    }

    /**
     * @param shopName The name of the shop that realizes orders.
     * @return - List of orders that are realized by the provided shop.
     */
    public List<Order> readOrdersByShopName(String shopName){
        return orderRepository.findAllByShopName(shopName);
    }

    /**
     * @param deliverer The delivery service of the orders.
     * @return - List of orders that are delivered by the provided delivery service.
     */
    public List<Order> readOrdersByDeliverer(String deliverer){
        return orderRepository.findAllByDeliverer(deliverer);
    }

    //Update

    /**
     *
     * Every parameter except id can be left out as {@code null} if it should not be updated.
     *
     * @param id The id of the order that has to be updated.
     * @param acpAddress New acp address of that order.
     * @param shopName New shop name of that order.
     * @param owner New owner of that order.
     * @param deliverer New deliverer of that order.
     * @return - True if the update succeeded, false if it did not.
     */
    public boolean updateOrder(long id, String acpAddress, String shopName, String owner, String deliverer){
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if(optionalOrder.isEmpty()) return false;

        Order order = optionalOrder.get();

        Optional<ACP> acpOptional = acpRepository.findByAddress(acpAddress);
        if(acpOptional.isEmpty() && acpAddress != null) return false;

        if(acpAddress != null) order.setAcp(acpOptional.get());
        if(shopName != null) order.setShopName(shopName);
        if(owner != null) order.setOwner(owner);
        if(deliverer != null) order.setDeliverer(deliverer);

        orderRepository.saveAndFlush(order);
        return true;
    }

    //Delete

    /**
     * @param id The id of the order that has to be deleted.
     * @return True if the order is successfully deleted, false otherwise.
     */
    public boolean deleteOrder(long id){
        if(!orderRepository.existsById(id)) return false;
        orderRepository.deleteById(id);
        return true;
    }

    // Maybe we could add some methods to clean all the orders that belong to one ACP, etc...
    // We will do that if it turns out to be necessary later in the development.
}