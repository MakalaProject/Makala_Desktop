package org.example.model.products;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CraftedProduct {
    private int id;
    private Product containerProduct;
    private Product internalProduct;
    private float weightGr;

    public CraftedProduct (Product cProduct, Product iProduct){
        this.containerProduct = cProduct;
        this.internalProduct = iProduct;
    }

}
