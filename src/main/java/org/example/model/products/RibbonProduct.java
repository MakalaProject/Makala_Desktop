package org.example.model.products;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class RibbonProduct extends Product {
    private BigDecimal widthIn;
    private String rgb;
    @Override
    public String getRoute(){
        return route + "/ribbons";
    }
}
