package org.example.model.products;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.interfaces.IPaths;

import java.math.BigDecimal;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class StaticProduct extends Product implements IPaths {
    protected Measure3Dimensions measures;
    private int totalAmount = 0;

    public StaticProduct(){
        super();
        measures = new Measure3Dimensions();
    }


    /*@Override
    public boolean compareProduct(Product p){
        StaticProduct product = (StaticProduct) p;
        if(name.equals(product.name)
                && privacy.equals(product.privacy)
                && productType.equals(product.getProductType())
                && price.equals(product.getPrice())
                && max == product.getMax()
                && min == product.getMin()
                && productClassDto.getClassification().equals((product.getProductClass().getClassification()))
                && dimensions.getZ().compareTo(product.dimensions.getZ()) == 0
                && dimensions.getX().compareTo(product.dimensions.getX()) == 0
                && dimensions.getY().compareTo(product.dimensions.getY()) == 0){
            return true;
        }
        return false;
    }*/

    @Override
    public String getRout() {
        return route + "/statics";
    }
}
