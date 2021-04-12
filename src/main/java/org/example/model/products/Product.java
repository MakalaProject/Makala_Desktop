package org.example.model.products;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.interfaces.IPaths;
import org.example.model.Picture;

import java.math.BigDecimal;
import java.util.ArrayList;

@Data
@AllArgsConstructor
public class Product implements IPaths {
    protected int idProduct;
    protected String name;
    protected ProductClassDto productClassDto;
    protected String privacy;
    protected String productType;
    protected BigDecimal price;
    protected Integer max;
    protected Integer min;
    protected Integer stock;
    protected ArrayList<Picture> pictures;
    protected String route = "products";

    public Product (){
        pictures = new ArrayList<>();
    }

    @Override
    public String getRoute() {
        return route;
    }
}
