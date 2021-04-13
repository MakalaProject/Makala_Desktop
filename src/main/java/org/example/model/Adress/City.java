package org.example.model.Adress;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class City {
    private int id;
    private String name;
    private State state;

    @Override
    public String toString(){
        return name;
    }
}
