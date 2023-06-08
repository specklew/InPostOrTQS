package tqs.example.impostor.proofofconcept.eshop;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import tqs.example.impostor.proofofconcept.BasicHttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EShopHttpRequester {

    BasicHttpClient httpClient = new BasicHttpClient();

    private static final int API_SUCCESS = 200;

    public List<String> getAcpAddressesFromRemoteServer() throws IOException, ParseException {
        String response = httpClient.doHttpGet("http://localhost:8080/acp/get/all");

        JSONArray acpJsonArray = (JSONArray) new JSONParser().parse(response);

        List<String> result = new ArrayList<>();

        for(Object object : acpJsonArray){
            JSONObject acp = (JSONObject) object;
            result.add((String) acp.get("address"));
        }

        return result;
    }
}
