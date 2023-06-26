package tqs.example.impostor.controller;

import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(description = "Create a new order")
    @PostMapping("/create")
    public ResponseEntity<Boolean> createOrder(
            @RequestParam String acpAddress,
            @RequestParam String shopName,
            @RequestParam String owner,
            @RequestParam String deliverer){
        return ResponseEntity.ok(orderService.createOrder(acpAddress, shopName, owner, deliverer));
    }

    @Operation(description = "Get an order by ID")
    @GetMapping("/get/{id}")
    public ResponseEntity<Order> readOrderById(@PathVariable("id") long id){
        return ResponseEntity.ok(orderService.readOrder(id));
    }

    @Operation(description = "Get all orders in repository")
    @GetMapping("/get/all")
    public ResponseEntity<List<Order>> readAllOrders(){
        return ResponseEntity.ok(orderService.readAllOrders());
    }

    @Operation(description = "Get orders by ACP address")
    @GetMapping("/getByACPAddress/{address}")
    public ResponseEntity<List<Order>> readOrdersByACPAddress(@PathVariable("address") String address){
        return ResponseEntity.ok(orderService.readOrdersByACPAddress(address));
    }

    @Operation(description = "Get orders by locker address")
    @GetMapping("/getByLockerAddress/{address}")
    public ResponseEntity<List<Order>> readOrdersByLockerAddress(@PathVariable("address") String address){
        return ResponseEntity.ok(orderService.readOrdersByLockerAddress(address));
    }

    @Operation(description = "Get orders by owner")
    @GetMapping("/getByOwner/{owner}")
    public ResponseEntity<List<Order>> readOrdersByOwner(@PathVariable("owner") String owner){
        return ResponseEntity.ok(orderService.readOrdersByOwner(owner));
    }

    @Operation(description = "Get orders by shopName")
    @GetMapping("/getByShopName/{name}")
    public ResponseEntity<List<Order>> readOrdersByShopName(@PathVariable("name") String name){
        return ResponseEntity.ok(orderService.readOrdersByShopName(name));
    }

    @Operation(description = "Get orders by deliverer")
    @GetMapping("/getByDeliverer/{deliverer}")
    public ResponseEntity<List<Order>> readOrdersByDeliverer(@PathVariable("deliverer") String deliverer){
        return ResponseEntity.ok(orderService.readOrdersByDeliverer(deliverer));
    }

    @Operation(description = "Update an existing order")
    @PostMapping("/update")
    public ResponseEntity<Boolean> updateOrder(
            @RequestParam long id,
            @RequestParam String acpAddress,
            @RequestParam String shopName,
            @RequestParam String owner,
            @RequestParam String deliverer){
        return ResponseEntity.ok(orderService.updateOrder(id, acpAddress, shopName, owner, deliverer));
    }

    @Operation(description = "Delete an order by ID")
    @PostMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteOrder(@PathVariable("id") long id){
        return ResponseEntity.ok(orderService.deleteOrder(id));
    }
}
