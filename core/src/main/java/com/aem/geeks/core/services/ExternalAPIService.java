package com.aem.geeks.core.services;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.service.metatype.annotations.Designate;

import java.io.IOException;

@Component(service = ExternalAPIService.class)
@Designate(ocd = ExternalAPIService.Config.class)
public class ExternalAPIService {

    private String apiEndpoint;

    // Configuration interface
    @ObjectClassDefinition(name = "External API Config")
    public @interface Config {
        @AttributeDefinition(name = "API Endpoint", description = "The URL endpoint of the external API.")
        String apiEndpoint() default "https://jsonplaceholder.typicode.com/users/1";
    }

    // Activate method to initialize configuration
    @Activate
    @Modified
    protected void activate(Config config) {
        this.apiEndpoint = config.apiEndpoint();
    }

    // Method to fetch data from the external API
    public JSONObject fetchData() throws JSONException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(apiEndpoint);
            try (CloseableHttpResponse response = client.execute(request)) {
                String jsonResponse = EntityUtils.toString(response.getEntity());
                return new JSONObject(jsonResponse);
            }
        } catch (IOException e) {
            // Handle exceptions
            return null;
        }
    }
}
