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
public class PaperProductToSend extends GiftProductsParent{
    BigDecimal widthCm = new BigDecimal(0);
    BigDecimal heightCm = new BigDecimal(0);
    private Product product;

    public PaperProductToSend(Product product){
        this.product = product;
    }
    @Override
    public String toString(){
        return this.product.getName();
    }
}
