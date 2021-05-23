package org.example.model.products;

import javafx.scene.control.Label;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
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

    @Override
    public BigDecimal formatStock(Label label) {
        label.setText("M");
        return stock.divide(new BigDecimal(10));
    }

    @Override
    public void getRealStock(BigDecimal stock) {
        this.stock = stock.multiply(new BigDecimal(10));
    }


}
