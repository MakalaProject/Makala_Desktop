package org.example.model.products;

import org.example.model.products.Product;

import java.math.BigDecimal;

public class RibbonProduct extends Product {
    private BigDecimal widthCm;
    private String color;
    private BigDecimal totalLengthCm;

    public BigDecimal getWidthCm() {
        return widthCm;
    }

    public void setWidthCm(BigDecimal widthCm) {
        this.widthCm = widthCm;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public BigDecimal getTotalLengthCm() {
        return totalLengthCm;
    }

    public void setTotalLengthCm(BigDecimal totalLengthCm) {
        this.totalLengthCm = totalLengthCm;
    }
    @Override
    public String getRoute(){
        return route + "/ribbons";
    }
}
