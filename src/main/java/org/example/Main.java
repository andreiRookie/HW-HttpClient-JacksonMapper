package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class Main {

    private static final String BASE_URL = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setSocketTimeout(10000)
                .setRedirectsEnabled(false)
                .build();

        HttpGet request = new HttpGet(BASE_URL);
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());

        try (CloseableHttpClient httpClient = provideClient(requestConfig)) {

            CloseableHttpResponse response = httpClient.execute(request);

            System.out.println(">>>>>>>>>>>Response headers:");
            Arrays.stream(response.getAllHeaders()).forEach(System.out::println);

            String responseBody = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);

//            List<Fact> list = mapper.readValue(response.getEntity().getContent(), new TypeReference<List<Fact>>() {});

            List<Fact> list = mapper.readValue(responseBody, new TypeReference<List<Fact>>() {});
            System.out.println(">>>>>>>>>>>List<Fact> list ");
            list.forEach(System.out::println);

            List<Fact> filteredList = list.stream()
                    .filter(fact -> fact.getUpvotes() != null)
                    .toList();
            System.out.println(">>>>>>>>>>>List<Fact> filteredList ");
            filteredList.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static CloseableHttpClient provideClient(RequestConfig requestConfig) {
        return HttpClientBuilder.create()
                .setUserAgent("Test user")
                .setDefaultRequestConfig(requestConfig)
                .build();
    }
}