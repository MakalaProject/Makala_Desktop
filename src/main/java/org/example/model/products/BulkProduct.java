package org.example.model.products;

import javafx.scene.control.Label;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class BulkProduct extends Product {
    private BigDecimal lossPercent;

    @Override
    public String getRoute(){
        return route + "/bulks";
    }

    @Override
    public BigDecimal formatStock(Label label) {
        label.setText("gr");
        return this.stock;
    }

    @Override
    public void setRealStock(BigDecimal stock) {
        this.stock = stock;
    }

}
