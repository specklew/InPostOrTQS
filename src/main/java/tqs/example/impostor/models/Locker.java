package tqs.example.impostor.models;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "LOCKER")
public class Locker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address")
    private String address;

    @Column(name = "capacity")
    private int capacity;

    @OneToMany(mappedBy = "locker", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Order> orders;

    public Locker() {

    }

    public Locker(String address, int capacity) {
        this.address = address;
        this.capacity = capacity;
        this.orders = new ArrayList<>();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<Order> getOrders() {
       return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Locker locker)) return false;
        return capacity == locker.capacity && Objects.equals(id, locker.id) && Objects.equals(address, locker.address) && Objects.equals(orders, locker.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address, capacity, orders);
    }
}

