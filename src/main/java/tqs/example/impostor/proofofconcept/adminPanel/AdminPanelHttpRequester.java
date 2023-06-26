package tqs.example.impostor.proofofconcept.adminPanel;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import tqs.example.impostor.models.ACP;
import tqs.example.impostor.models.Locker;
import tqs.example.impostor.models.Order;
import tqs.example.impostor.proofofconcept.BasicHttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminPanelHttpRequester {

    private final BasicHttpClient httpClient;
    private static final String websiteAddress = "http://localhost:8080";

    public AdminPanelHttpRequester() {
        this.httpClient = new BasicHttpClient();
    }

    public AdminPanelHttpRequester(BasicHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public Optional<ACP> getAcpFromRemoteServer(String id) throws IOException, ParseException {
        String response = httpClient.doHttpGet(websiteAddress + "/acp/" + id);

        JSONObject acpJsonObject = (JSONObject) new JSONParser().parse(response);

        ACP acp = new ACP();
        acp.setId((Long) acpJsonObject.get("id"));
        acp.setAddress((String) acpJsonObject.get("address"));
        acp.setCapacity((double) acpJsonObject.get("capacity"));

        return Optional.of(acp);
    }

    public List<Order> getOrdersFromRemoteServer() throws IOException, ParseException {
        String response = httpClient.doHttpGet(websiteAddress + "/deliveries/all");
        System.out.println("Response: " + response);

        // Assuming response is a JSON object
        JSONObject jsonResponse = (JSONObject) new JSONParser().parse(response);
        JSONArray orderJsonArray = (JSONArray) jsonResponse.get("orders");

        List<Order> result = new ArrayList<>();

        for (Object object : orderJsonArray) {
            JSONObject orderJsonObject = (JSONObject) object;

            Order order = new Order();
            order.setId((Long) orderJsonObject.get("id"));
            order.setShopName((String) orderJsonObject.get("shopName"));
            order.setOwner((String) orderJsonObject.get("owner"));
            order.setDeliverer((String) orderJsonObject.get("deliverer"));
            order.setAcp((ACP) orderJsonObject.get("acp"));
            order.setLocker((Locker) orderJsonObject.get("locker"));

            result.add(order);
        }

        return result;
    }
}
