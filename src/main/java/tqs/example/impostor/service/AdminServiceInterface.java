package tqs.example.impostor.service;

import tqs.example.impostor.models.ACP;
import tqs.example.impostor.models.Order;

import java.util.List;

public interface AdminServiceInterface {
    boolean passwordVerification(String userName, String password);
    String login(String userName, String password);
    List<Order> getPendingOrders();
    ACP searchACPById(String acpId);
    void addACP(ACP acp);
}
