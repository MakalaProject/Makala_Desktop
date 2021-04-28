package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.products.Action;
import org.example.model.products.Product;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiftProductsToSend {
    Integer id;
    String category;
    Integer amount = 0;
    Product product;
    Action action;
    Integer holeNumber = 1;

    public GiftProductsToSend(Product product, Action action){
        this.product = product;
        this.action = action;
    }

    public GiftProductsToSend(Product product){
        this.product = product;
    }

    @Override
    public String toString(){
        return this.product.getName();
    }

}
