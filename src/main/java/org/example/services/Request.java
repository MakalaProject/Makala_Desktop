package org.example.services;

import com.google.gson.*;
import org.example.exceptions.ProductDeleteException;
import org.example.exceptions.ProductErrorRequest;
import org.example.model.products.Product;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Request<D> {
    public static final String REST_URL = "http://25.4.107.19:9080/";

    public static Object putJ(String link, Object ob) throws ProductDeleteException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                            throws JsonParseException {
                        return LocalDate.parse(json.getAsString(),
                                DateTimeFormatter.ofPattern("yyyy-MM-dd").withLocale(Locale.ENGLISH));
                    }
                })
                .create();
        String jsonString = gson.toJson(ob);
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(REST_URL + link))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 500 || response.statusCode() == 400 || response.statusCode() == 423 ) {
                ProductErrorRequest exception = gson.fromJson(response.body(), ProductErrorRequest.class);
                exception.setStatus(response.statusCode());
                throw new ProductDeleteException(exception.getMessage(), exception.getStatus());
            }
            return gson.fromJson(response.body(), ob.getClass());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static  void deleteJ(String s, int id) throws Exception {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(REST_URL + s + "?id=" + id))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 423 ){
                Gson gson = new Gson();
                ProductErrorRequest exception = gson.fromJson(response.body(), ProductErrorRequest.class);
                exception.setStatus(response.statusCode());
                throw new ProductDeleteException(exception.getMessage(), exception.getStatus());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static <D> List<D> getJ(String link, Class<D[]> classType, boolean isPage){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                            throws JsonParseException {
                        return LocalDateTime.parse(json.getAsString(),
                                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").withLocale(Locale.ENGLISH));
                    }
                })
                .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                            throws JsonParseException {
                        return LocalDate.parse(json.getAsString(),
                                DateTimeFormatter.ofPattern("yyyy-MM-dd").withLocale(Locale.ENGLISH));
                    }
                })
                .create();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(REST_URL + link))
                .build();
        List<D> list = null;
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
                D[] arrays = gson.fromJson(body.getAsJsonArray("content"), classType);
                list = new ArrayList<D>(Arrays.asList(arrays));
                Page<D> userResponseEntity = gson.fromJson(response.body(), Page.class);
                list = new ArrayList<D>(Arrays.asList(arrays));
                userResponseEntity.setContent(list);
            }else {
                D[] arrays = gson.fromJson(response.body(), classType);
                list = new ArrayList<D>(Arrays.asList(arrays));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Object getJ(String link, Class<?> classType){
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(REST_URL + link))
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
            return gson.fromJson(response.body(), classType);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Object find(String link, int id, Class<?> dClass){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                            throws JsonParseException {
                        return LocalDateTime.parse(json.getAsString(),
                                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").withLocale(Locale.ENGLISH));
                    }
                })
                .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                            throws JsonParseException {
                        return LocalDate.parse(json.getAsString(),
                                DateTimeFormatter.ofPattern("yyyy-MM-dd").withLocale(Locale.ENGLISH));
                    }
                })
                .create();
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
