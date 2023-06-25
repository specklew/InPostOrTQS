package tqs.example.impostor.proofofconcept.lockerPanel;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import tqs.example.impostor.models.Order;
import tqs.example.impostor.proofofconcept.BasicHttpClient;

import java.io.IOException;
import java.util.Optional;

@Service
public class LockerPanelHttpRequester {

    private final BasicHttpClient httpClient;
    private final static String websiteAddress = "http://localhost:8080";
    public LockerPanelHttpRequester() {
        this.httpClient = new BasicHttpClient();
    }
    public LockerPanelHttpRequester(BasicHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public Optional<Order> getOrderFromRemoteServer(String id) throws IOException, ParseException {
        String response = httpClient.doHttpGet(websiteAddress + "/order/get/" + id);

        JSONObject orderJsonObject = (JSONObject) new JSONParser().parse(response);

        Order order = new Order();
        System.out.println(orderJsonObject);
        order.setId((Long) orderJsonObject.get("id"));
        order.setOwner((String) orderJsonObject.get("owner"));
        order.setShopName((String) orderJsonObject.get("shopName"));
        order.setDeliverer((String) orderJsonObject.get("deliverer"));
        //order.setLocker((Locker) orderJsonObject.get("locker"));
        //order.setAcp((ACP) orderJsonObject.get("acp"));
        System.out.println(order.getId());
        System.out.println(order.getOwner());
        System.out.println(order.getDeliverer());
        return Optional.of(order);
    }

}
