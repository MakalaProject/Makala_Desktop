package org.example.model.products;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductClassDto {

    private int idClassification;
    private String classification;

    @Override
    public String toString() {
        return this.classification;
    }

}
