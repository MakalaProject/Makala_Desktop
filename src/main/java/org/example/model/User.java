package org.example.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    protected int idUser;
    protected String firstName;
    protected String lastName;
    protected String phone;
    protected final String route = "users";
}
