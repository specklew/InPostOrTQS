package tqs.example.impostor.proofofconcept;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasicHttpClientTest {

    @Mock(lenient = true)
    private CloseableHttpClient httpClient;

    @Mock(lenient = true)
    private CloseableHttpResponse response;

    @Mock(lenient = true)
    private HttpClientBuilder builder;

    @InjectMocks
    BasicHttpClient sut;

    @BeforeEach
    void setUp() throws IOException {
        when(response.getEntity()).thenReturn(new StringEntity("{'result':'ok'}"));
        when(httpClient.execute(any())).thenReturn(response);
        when(builder.build()).thenReturn(httpClient);
    }

    @Test
    void whenDoHttpGet_thenReturnResultOk() throws IOException {
        assertThat(sut.doHttpGet("whatever")).isEqualTo("{'result':'ok'}");
    }

    @Test
    void doHttpPost() throws IOException, URISyntaxException {
        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("name", "value"));
        assertThat(sut.doHttpPost("whatever", list)).isEqualTo("{'result':'ok'}");
    }
}