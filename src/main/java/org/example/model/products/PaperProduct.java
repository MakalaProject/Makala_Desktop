package org.example.model.products;

public class PaperProduct extends Product {
    private String color;
    private float TotalWeightGr;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public PaperProduct(){
        super();
    }
    public float getTotalWeightGr() {
        return TotalWeightGr;
    }

    public void setTotalWeightGr(float totalWeightGr) {
        TotalWeightGr = totalWeightGr;
    }

    @Override
    public String getRoute(){
        return route + "/papers";
    }
}
