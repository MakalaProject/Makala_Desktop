package org.example.model;

public class UserLogin {
    private final String id;
    private final String password;

    public UserLogin(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getRoute() {
        return "users/employees/verify";
    }
}
