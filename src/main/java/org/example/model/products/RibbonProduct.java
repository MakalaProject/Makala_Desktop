package org.example.model.products;

import java.math.BigDecimal;

public class RibbonProduct extends Product {
    private BigDecimal widthCm;
    private String rgb;

    public BigDecimal getWidthCm() {
        return widthCm;
    }

    public void setWidthCm(BigDecimal widthCm) {
        this.widthCm = widthCm;
    }

    public String getRgb() {
        return rgb;
    }

    public void setRgb(String rgb) {
        this.rgb = rgb;
    }

    @Override
    public String getRoute(){
        return route + "/ribbons";
    }
}
