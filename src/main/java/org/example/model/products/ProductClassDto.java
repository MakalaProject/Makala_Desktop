package org.example.model.products;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class ProductClassDto {

    private int idClassification;
    private String classification;
    protected String productType;

    @Override
    public String toString() {
        return this.classification;
    }

}
