package org.example.model.products;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class PackageProduct {
    private Integer idPackage;
    private Integer amount;
    private LocalDate expiryDate;
    private Product product;

    @Override
    public String toString(){
        return product.getName();
    }
}
