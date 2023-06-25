package tqs.example.impostor.proofofconcept.adminPanel;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs.example.impostor.models.ACP;
import tqs.example.impostor.models.Order;
import tqs.example.impostor.proofofconcept.BasicHttpClient;
import tqs.example.impostor.proofofconcept.eshop.EShopHttpRequester;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminPanelHttpRequesterTest {

    @Mock(lenient = true)
    BasicHttpClient httpClient;

    @InjectMocks
    AdminPanelHttpRequester requester;

    @BeforeEach
    void setUp() throws IOException, ParseException {
        // Mock the responses for the HTTP requests
        String acpResponse = """
            {
              "id": 0,
              "address": "R. Dos Marnotos 58, Aveiro",
              "capacity": 1.2
            }""";

        String ordersResponse = """
            {
              "orders": [
                {
                  "id": 1,
                  "shopName": "pokeShop",
                  "owner": "da",
                  "deliverer": "FedUp"
                }
              ]
            }""";

        when(httpClient.doHttpGet(Mockito.any())).thenReturn(acpResponse, ordersResponse);
    }

    @Test
    void whenGetAcpFromRemoteServer_thenReturnAcpObject() throws IOException, ParseException {
        // Arrange
        String acpId = "0";
        ACP expectedAcp = new ACP();
        expectedAcp.setId(0L);
        expectedAcp.setAddress("R. Dos Marnotos 58, Aveiro");
        expectedAcp.setCapacity(1.2);

        // Act
        Optional<ACP> actualAcp = requester.getAcpFromRemoteServer(acpId);

        // Assert
        assertThat(actualAcp).isPresent();
        assertThat(actualAcp.get()).isEqualTo(expectedAcp);
    }

    @Test
    void whenGetOrdersFromRemoteServer_thenReturnListOfOrders() throws IOException, ParseException {
        // Arrange
        Order expectedOrder = new Order();
        expectedOrder.setId(1L);
        expectedOrder.setShopName("pokeShop");
        expectedOrder.setOwner("da");
        expectedOrder.setDeliverer("FedUp");
        List<Order> expectedOrders = List.of(expectedOrder);

        // Act
        List<Order> actualOrders = requester.getOrdersFromRemoteServer();

        // Assert
        assertThat(actualOrders).hasSameSizeAs(expectedOrders);
        assertThat(actualOrders).containsExactlyElementsOf(expectedOrders);
    }
}
