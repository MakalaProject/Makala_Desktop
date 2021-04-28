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
public class PaperProductToSend extends GiftProductsToSend{
    BigDecimal width = new BigDecimal(0);
    BigDecimal height = new BigDecimal(0);

    public PaperProductToSend(Product product, Action action){
        super(product, action);
    }
    public PaperProductToSend(Product product){
        super(product);
    }
    @Override
    public String toString(){
        return this.product.getName();
    }
}
