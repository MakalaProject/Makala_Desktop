package org.example.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.model.products.Product;

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

public class Request<D> {
    public static final String REST_URL = "http://25.4.107.19:9080/";

    public static Object putJ(String link, Object ob) throws Exception {
        Gson gson = new Gson();
        String jsonString = gson.toJson(ob);
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(REST_URL + link))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 500 || response.statusCode() == 400) {
                throw new Exception();
            }
            return gson.fromJson(response.body(), ob.getClass());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void deleteJ(String s, int id) {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(REST_URL + s + "?id=" + id))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static <D> List<D> getJ(String link, Class<D[]> classType, boolean isPage){
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(REST_URL + link))
                .build();
        List<D> list = null;
        Product product = new Product();
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
            if (isPage){
                JsonObject body = (JsonObject) jsonParser.parse(response.body());
                D[] arrays = new Gson().fromJson(body.getAsJsonArray("content"), classType);
                list = new ArrayList<D>(Arrays.asList(arrays));
                Page<D> userResponseEntity = new Gson().fromJson(response.body(), Page.class);
                list = new ArrayList<D>(Arrays.asList(arrays));
                userResponseEntity.setContent(list);
            }else {
                D[] arrays = new Gson().fromJson(response.body(), classType);
                list = new ArrayList<D>(Arrays.asList(arrays));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Object find(String link, int id, Class<?> dClass){
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(REST_URL +link+"/find?id=" + id))
                .build();
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
            Gson gson = new Gson();
            return gson.fromJson(response.body(), dClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public static Object postJ(String link, Object object) throws Exception {
        Gson gson = new Gson();
        String jsonString = gson.toJson(object);
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(REST_URL + link))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 500 || response.statusCode() == 400) {
                throw new Exception();
            }
            Object ob = gson.fromJson(response.body(), object.getClass());
            System.out.println(response.body());
            return ob;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <D> D post(String link, Object object, Class<D> dclass){
        Gson gson = new Gson();
        String jsonString = gson.toJson(object);
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(REST_URL + link))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            D ob = (D)gson.fromJson(response.body(), dclass);
            System.out.println(response.body());
            if (response.statusCode() != 404){
                return ob;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <D> List<D> postArray(String link, Object object, Class<D[]> classType){
        Gson gson = new Gson();
        String jsonString = gson.toJson(object);
        HttpClient client = HttpClient.newBuilder().build();
        List<D> list = null;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(REST_URL + link))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            D[] arrays = new Gson().fromJson(response.body(), classType);
            list = new ArrayList<D>(Arrays.asList(arrays));
            System.out.println(response.body());
            if (response.statusCode() != 404){
                return list;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
