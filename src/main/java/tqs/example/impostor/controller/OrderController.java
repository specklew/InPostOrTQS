package tqs.example.impostor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tqs.example.impostor.models.Order;
import tqs.example.impostor.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService service) {
        this.orderService = service;
    }

    @PostMapping("/create")
    public ResponseEntity<Boolean> createOrder(
            @RequestParam String acpAddress,
            @RequestParam String shopName,
            @RequestParam String owner,
            @RequestParam String deliverer){
        return ResponseEntity.ok(orderService.createOrder(acpAddress, shopName, owner, deliverer));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Order> readOrderById(@PathVariable("id") long id){
        return ResponseEntity.ok(orderService.readOrder(id));
    }

    @GetMapping("/getByACPAddress/{address}")
    public ResponseEntity<List<Order>> readOrdersByACPAddress(@PathVariable("address") String address){
        return ResponseEntity.ok(orderService.readOrdersByACPAddress(address));
    }

    @GetMapping("/getByLockerAddress/{address}")
    public ResponseEntity<List<Order>> readOrdersByLockerAddress(@PathVariable("address") String address){
        return ResponseEntity.ok(orderService.readOrdersByLockerAddress(address));
    }

    @GetMapping("/getByOwner/{owner}")
    public ResponseEntity<List<Order>> readOrdersByOwner(@PathVariable("owner") String owner){
        return ResponseEntity.ok(orderService.readOrdersByOwner(owner));
    }

    @GetMapping("/getByShopName/{name}")
    public ResponseEntity<List<Order>> readOrdersByShopName(@PathVariable("name") String name){
        return ResponseEntity.ok(orderService.readOrdersByShopName(name));
    }

    @GetMapping("/getByDeliverer/{deliverer}")
    public ResponseEntity<List<Order>> readOrdersByDeliverer(@PathVariable("deliverer") String deliverer){
        return ResponseEntity.ok(orderService.readOrdersByDeliverer(deliverer));
    }

    @PostMapping("/update")
    public ResponseEntity<Boolean> updateOrder(
            @RequestParam long id,
            @RequestParam String acpAddress,
            @RequestParam String shopName,
            @RequestParam String owner,
            @RequestParam String deliverer){
        return ResponseEntity.ok(orderService.updateOrder(id, acpAddress, shopName, owner, deliverer));
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteOrder(@PathVariable("id") long id){
        return ResponseEntity.ok(orderService.deleteOrder(id));
    }
}
