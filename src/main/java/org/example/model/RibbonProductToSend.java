package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.model.products.Action;
import org.example.model.products.Product;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RibbonProductToSend extends GiftProductsParent{
    BigDecimal lengthCm = new BigDecimal(0);
    private Product product;

    public RibbonProductToSend(Product product){
        this.product = product;
    }

    @Override
    public String toString(){
        return this.product.getName();
    }

}
