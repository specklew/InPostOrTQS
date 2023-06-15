package tqs.example.impostor.proofofconcept.eshop;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs.example.impostor.proofofconcept.BasicHttpClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EShopHttpRequesterTest {

    @Mock(lenient = true)
    BasicHttpClient httpClient;

    @InjectMocks
    EShopHttpRequester requester;

    List<NameValuePair> parameters1 = new ArrayList<>();
    String postResponse1 = "true";

    @BeforeEach
    void setUp() throws IOException, URISyntaxException {

        String getResponse = """
                [
                  {
                    "id": 0,
                    "orders": [
                      {
                        "id": 1,
                        "acp": 0,
                        "locker": null,
                        "shopName": "pokeShop",
                        "owner": "da",
                        "deliverer": "FedUp"
                      }
                    ],
                    "address": "R. Dos Marnotos 58, Aveiro",
                    "capacity": 1.2
                  }
                ]""";

        parameters1.add(new BasicNameValuePair("acpAddress", "1"));
        parameters1.add(new BasicNameValuePair("shopName", "1"));
        parameters1.add(new BasicNameValuePair("owner", "1"));
        parameters1.add(new BasicNameValuePair("deliverer", "1"));


        when(httpClient.doHttpGet(Mockito.any())).thenReturn(getResponse);
        when(httpClient.doHttpPost("http://localhost:8080/order/create", parameters1)).thenReturn(postResponse1);
    }

    @Test
    void whenGetAcpAddressesFromRemoteServer_thenReturnListOfStrings() throws IOException, ParseException {
        assertThat(requester.getAcpAddressesFromRemoteServer()).contains("R. Dos Marnotos 58, Aveiro");
    }

    @Test
    void givenParameters_whenPostNewOrder_thenReturnTrue() throws IOException, URISyntaxException, ParseException {
        assertThat(requester.postNewOrder(
                parameters1.get(0).getValue(),
                parameters1.get(1).getValue(),
                parameters1.get(2).getValue(),
                parameters1.get(3).getValue())).isEqualTo(true);
    }
}