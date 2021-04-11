package org.example.model.products;

import java.math.BigDecimal;

public class BulkProduct extends Product {
    private BigDecimal lossPercent;

    public BulkProduct() {
        super();
    }

    public BigDecimal getLossPercentage() {
        return lossPercent;
    }

    public void setLossPercentage(BigDecimal lossPercentage) {
        this.lossPercent = lossPercentage;
    }

    @Override
    public String getRoute(){
        return route + "/bulks";
    }


}
