package org.example.model.Adress;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Address {
    private int id;
    private String address;
    private Integer cp;
    private City city;

}
