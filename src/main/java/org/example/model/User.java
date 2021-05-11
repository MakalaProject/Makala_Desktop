package org.example.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    protected Integer idUser;
    protected String firstName;
    protected String lastName;
    protected String phone;
    protected final String route = "users";
    protected final String identifier = "Usuario";
}
