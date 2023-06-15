package tqs.example.impostor.proofofconcept;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class BasicHttpClient {
    public String doHttpGet(String url) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        try (CloseableHttpResponse response = client.execute(request)) {
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        }
    }

    public String doHttpPost(String url, List<NameValuePair> params) throws IOException, URISyntaxException {
        CloseableHttpClient client = HttpClients.createDefault();

        URIBuilder uriBuilder = new URIBuilder(url);
        for(var param : params){
            uriBuilder.addParameter(param.getName(), param.getValue());
        }

        HttpPost request = new HttpPost(uriBuilder.build().toString());
        try (CloseableHttpResponse response = client.execute(request)) {
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        }
    }
}
