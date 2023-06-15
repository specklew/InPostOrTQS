package tqs.example.impostor.proofofconcept.adminPanel;


import org.springframework.stereotype.Service;
import tqs.example.impostor.proofofconcept.BasicHttpClient;

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
}
