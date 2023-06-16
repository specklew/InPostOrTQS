package tqs.example.impostor.proofofconcept.adminPanel;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import tqs.example.impostor.models.ACP;
import tqs.example.impostor.proofofconcept.BasicHttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminPanelHttpRequester {

    private final BasicHttpClient httpClient;
    private final String websiteAddress = "http://localhost:8080";

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

        return Optional.of(acp);
    }

}
