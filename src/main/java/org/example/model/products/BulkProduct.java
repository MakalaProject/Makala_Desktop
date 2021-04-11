package org.example.model.products;

import java.math.BigDecimal;

public class BulkProduct extends Product {
    private BigDecimal lossPercent;
    private BigDecimal totalAmountGr;

    public BulkProduct() {
        super();
    }

    public BigDecimal getLossPercentage() {
        return lossPercent;
    }

    public void setLossPercentage(BigDecimal lossPercentage) {
        this.lossPercent = lossPercentage;
    }

    public BigDecimal getTotalAmount() {
        return totalAmountGr;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmountGr = totalAmount;
    }

    @Override
    public String getRoute(){
        return route + "/bulks";
    }


}
