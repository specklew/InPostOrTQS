package tqs.example.impostor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tqs.example.impostor.models.ACP;
import tqs.example.impostor.models.Locker;
import tqs.example.impostor.models.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByAcp(ACP acp);

    List<Order> findAllByLocker(Locker locker);

    List<Order> findAllByOwner(String owner);

    List<Order> findAllByShopName(String shopName);

    List<Order> findAllByDeliverer(String deliverer);

}
