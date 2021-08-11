package org.example.model;

import lombok.Data;

import java.util.List;

@Data
public class Payload {
    String sub;
    List<Authorities> authorities;
    String iat;
    String exp;

    public Payload() {

    }
}
