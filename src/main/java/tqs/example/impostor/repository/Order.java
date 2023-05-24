package tqs.example.impostor.repository;

import jakarta.persistence.*;

@Entity
@Table(name = "ORDERS")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false)
    @JoinColumn(
            name = "acp_id",
            nullable = false)
    private ACP acp;

    @Column(nullable = false)
    private String deliverer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ACP getAcp() {
        return acp;
    }

    public void setAcp(ACP acp) {
        this.acp = acp;
    }

    public String getDeliverer() {
        return deliverer;
    }

    public void setDeliverer(String deliverer) {
        this.deliverer = deliverer;
    }
}
