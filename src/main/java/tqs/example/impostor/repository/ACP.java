package tqs.example.impostor.repository;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "ACP")
public class ACP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "acp", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Order> orders;

    @Column(name = "address")
    private String address;

    @Column(name = "capacity")
    private float capacity;

    public ACP() {
    }

    public ACP(String address, float capacity) {
        this.address = address;
        this.capacity = capacity;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> order) {
        this.orders = order;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getCapacity() {
        return capacity;
    }

    public void setCapacity(float capacity) {
        this.capacity = capacity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ACP acp = (ACP) o;
        return Objects.equals(id, acp.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
