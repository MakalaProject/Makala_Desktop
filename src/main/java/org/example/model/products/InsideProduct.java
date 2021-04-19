package org.example.model.products;

import java.math.BigDecimal;

public class InsideProduct extends Product{
    Product internalProduct;
    BigDecimal amount;
    boolean toDelete;

    @Override
    public Integer getId() {
        return getIdProduct();
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
