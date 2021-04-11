package org.example.services;

import org.example.model.Catalog;
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

public class CatalogService {
    private static final String API_URL = "http://localhost:9080/";

    public static List<Catalog> getCatalogs(){
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(API_URL + "catalog"))
                .build();
        List<Catalog> catalogsList = null;
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
            Catalog [] catalogs = new Gson().fromJson(body.getAsJsonArray("content"),Catalog[].class);
            catalogsList = new ArrayList<Catalog>(Arrays.asList(catalogs));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return  catalogsList;
    }

    public static Catalog InsertCatalog(Catalog catalog){
        Gson gson = new Gson();
        String jsonString = gson.toJson(catalog);
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "catalog"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Catalog returnCatalog = gson.fromJson(response.body(), Catalog.class);
            System.out.println(response.body());
            return returnCatalog;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void UpdateCatalog(Catalog catalog){
        Gson gson = new Gson();
        String jsonString = gson.toJson(catalog);
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "catalog"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void DeleteCatalog(int id){
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "catalog?id=" + id))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
