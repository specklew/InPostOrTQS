package tqs.example.impostor.proofofconcept.eshop;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import tqs.example.impostor.proofofconcept.BasicHttpClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class EShopHttpRequester {

    private final BasicHttpClient httpClient;
    private static final String websiteAddress = "http://localhost:8080";

    public EShopHttpRequester() {
        this.httpClient = new BasicHttpClient();
    }

    public EShopHttpRequester(BasicHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public List<String> getAcpAddressesFromRemoteServer() throws IOException, ParseException {
        String response = httpClient.doHttpGet(websiteAddress + "/acp/get/all");

        JSONArray acpJsonArray = (JSONArray) new JSONParser().parse(response);

        List<String> result = new ArrayList<>();

        for(Object object : acpJsonArray){
            JSONObject acp = (JSONObject) object;
            result.add((String) acp.get("address"));
        }

        return result;
    }

    public boolean postNewOrder(String address, String shop, String owner, String deliverer) throws IOException, URISyntaxException {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("acpAddress", address));
        parameters.add(new BasicNameValuePair("shopName", shop));
        parameters.add(new BasicNameValuePair("owner", owner));
        parameters.add(new BasicNameValuePair("deliverer", deliverer));
        String response = httpClient.doHttpPost(websiteAddress + "/order/create", parameters);

        return Objects.equals(response, "true");
    }
}
