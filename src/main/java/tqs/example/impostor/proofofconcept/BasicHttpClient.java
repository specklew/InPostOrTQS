package tqs.example.impostor.proofofconcept;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class BasicHttpClient {

    private final HttpClientBuilder clientBuilder;

    public BasicHttpClient() {
        clientBuilder = HttpClientBuilder.create();
    }

    public BasicHttpClient(HttpClientBuilder clientBuilder) {
        this.clientBuilder = clientBuilder;
    }

    public String doHttpGet(String url) throws IOException {
        HttpGet request = new HttpGet(url);
        try(CloseableHttpClient client = clientBuilder.build()) {
            try (CloseableHttpResponse response = client.execute(request)) {
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity);
            }
        }
    }

    public String doHttpPost(String url, List<NameValuePair> params) throws IOException, URISyntaxException {


        URIBuilder uriBuilder = new URIBuilder(url);
        for(var param : params){
            uriBuilder.addParameter(param.getName(), param.getValue());
        }

        HttpPost request = new HttpPost(uriBuilder.build().toString());
        try(CloseableHttpClient client = clientBuilder.build()){
            try (CloseableHttpResponse response = client.execute(request)) {
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity);
            }
        }
    }
}
