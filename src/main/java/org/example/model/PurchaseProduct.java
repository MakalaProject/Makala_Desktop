package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.interfaces.IChangeable;
import org.example.model.products.Action;
import org.example.model.products.Product;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
public class PurchaseProduct implements IChangeable<Integer> {
    Integer id;
    BigDecimal amount;
    Product product;
    ProductExpiration packageP;
    Boolean toDelete;
    Action action;

    public PurchaseProduct(Product product, BigDecimal amount){
        this.product = product;
        this.amount = amount;

    }
    @Override
    public void setToDelete(boolean toDelete) {
        this.toDelete = toDelete;
    }

    @Override
    public String toString(){
        return product.getName();
    }
}
