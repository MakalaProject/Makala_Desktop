package org.example.model.products;

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

}
