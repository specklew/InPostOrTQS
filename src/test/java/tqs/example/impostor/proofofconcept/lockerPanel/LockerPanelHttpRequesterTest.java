package tqs.example.impostor.proofofconcept.lockerPanel;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs.example.impostor.models.Order;
import tqs.example.impostor.proofofconcept.BasicHttpClient;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LockerPanelHttpRequesterTest {

    @Mock(lenient = true)
    BasicHttpClient httpClient;

    @InjectMocks
    LockerPanelHttpRequester requester;

    @Test
    void whenGetOrderFromRemoteServer_thenReturnOptionalOrder() throws IOException, ParseException {
        // Mock the response for the HTTP request
        String orderResponse = """
            {
              "id": 1,
              "shopName": "pokeShop",
              "owner": "da",
              "deliverer": "FedUp"
            }""";

        when(httpClient.doHttpGet(Mockito.any())).thenReturn(orderResponse);

        // Arrange
        String orderId = "1";
        Order expectedOrder = new Order();
        expectedOrder.setId(1L);
        expectedOrder.setShopName("pokeShop");
        expectedOrder.setOwner("da");
        expectedOrder.setDeliverer("FedUp");

        // Act
        Optional<Order> actualOrder = requester.getOrderFromRemoteServer(orderId);

        // Assert
        assertThat(actualOrder).isPresent();
        assertThat(actualOrder.get()).isEqualTo(expectedOrder);
    }
}
