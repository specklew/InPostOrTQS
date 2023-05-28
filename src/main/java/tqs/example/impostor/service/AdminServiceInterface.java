package tqs.example.impostor.service;

import tqs.example.impostor.repository.ACP;
import tqs.example.impostor.repository.Order;

import java.util.List;
import java.util.Optional;

public interface AdminServiceInterface {
    boolean passwordVerification(String userName, String password);

    String login(String userName, String password);
    List<Order> getPendingOrders();
    ACP searchACPById(String acpId);
    ACP searchACPByOwnerSurname(String ownerSurname);
    void addACP(ACP acp);
}
