package org.example.services;

import org.example.model.Provider;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProviderService {
    private static final String API_URL = "http://localhost:9080/";

    public static List<Provider> getProviders(){
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(API_URL + "users/providers"))
                .build();
        List<Provider> providersList = null;
        try {
            HttpResponse<String> response = HttpClient.newBuilder()
                    .authenticator(new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(
                                    "user",
                                    "123".toCharArray()
                            );
                        }
                    }).build().send(request, HttpResponse.BodyHandlers.ofString());
            JsonParser jsonParser = new JsonParser();
            JsonObject body = (JsonObject) jsonParser.parse(response.body());
            Provider[] providers = new Gson().fromJson(body.getAsJsonArray("content"), Provider[].class);
            providersList = new ArrayList<Provider>(Arrays.asList(providers));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return  providersList;
    }

    public static Provider InsertProvider(Provider provider){
        Gson gson = new Gson();
        String jsonString = gson.toJson(provider);
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "users/providers"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Provider returnProvider = gson.fromJson(response.body(), Provider.class);
            System.out.println(response.body());
            return returnProvider;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void UpdateProvider(Provider provider){
        Gson gson = new Gson();
        String jsonString = gson.toJson(provider);
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "users/providers"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void DeleteProvider(int id){
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "users/providers?id=" + id))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
