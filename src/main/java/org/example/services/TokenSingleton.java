package org.example.services;

import org.example.model.Payload;

public class TokenSingleton {
    private String token;
    private Payload payload;
    private static TokenSingleton singleton;

    private TokenSingleton (String token) {
        this.token = token;
    }

    public static TokenSingleton getInstance(String token){
        if (singleton==null) {
            singleton = new TokenSingleton(token);
        }
        return singleton;
    }

    public static TokenSingleton getSingleton(){
        return singleton;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }
}
