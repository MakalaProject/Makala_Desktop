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

    @Override
    public String getRout() {
        return route + "/statics";
    }
}
