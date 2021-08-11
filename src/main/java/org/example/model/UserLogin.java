package org.example.model;


import lombok.Data;

@Data
public class UserLogin {
    private final String username;
    private final String password;

    public UserLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getRoute() {
        return "login";
    }
}
