package org.example.model;

import lombok.Data;
import org.example.interfaces.IChangeable;

import java.math.BigDecimal;

@Data
public class Bow implements IChangeable<Integer> {
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
