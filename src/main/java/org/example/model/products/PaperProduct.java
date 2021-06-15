package org.example.model.products;

import javafx.scene.control.Label;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaperProduct extends Product {
    private String rgb;


    @Override
    public String getRoute(){
        return route + "/papers";
    }

    @Override
    public BigDecimal formatStock(Label label) {
        label.setText("m2");
        return stock.divide(new BigDecimal(100));
    }

    public void setRealStock(BigDecimal stock) {
        this.stock = stock.multiply(new BigDecimal(100));
    }

    @Override
    public Integer formatMax() {
        return max/100;
    }

    @Override
    public Integer formatMin() {
        return min/100;
    }

    @Override
    public void setMax(Integer max) {
        this.max = max*100;
    }

    @Override
    public void setMin(Integer min) {
        this.min = min*100;
    }
}
