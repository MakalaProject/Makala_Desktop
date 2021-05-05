package org.example.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.interfaces.IChangeable;
import org.example.model.products.Action;
import org.example.model.products.StaticProduct;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class GiftProductsToSend extends GiftProductsParent{
    private StaticProduct product;


    public GiftProductsToSend(StaticProduct product){
        GiftProductsToSend.this.product = product;
    }

    @Override
    public String toString(){
        return this.product.getName();
    }

}
