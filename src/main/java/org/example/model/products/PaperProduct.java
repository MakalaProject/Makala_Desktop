package org.example.model.products;

public class PaperProduct extends Product {
    private String rgb;

    public String getRgb() {
        return rgb;
    }

    public void setRgb(String rgb) {
        this.rgb = rgb;
    }

    public PaperProduct(){
        super();
    }

    @Override
    public String getRoute(){
        return route + "/papers";
    }
}
