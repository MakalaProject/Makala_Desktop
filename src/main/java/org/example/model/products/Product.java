package org.example.model.products;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.interfaces.IChangeable;
import org.example.interfaces.IPaths;
import org.example.model.Picture;

import java.math.BigDecimal;
import java.util.ArrayList;

@Data
@AllArgsConstructor
public class Product implements IPaths, IChangeable<Integer> {
    protected Integer idProduct;
    protected String name;
    protected ProductClassDto productClassDto;
    protected String privacy;
    protected BigDecimal price;
    protected Integer max;
    protected Integer min;
    protected Integer stock;
    protected ArrayList<Picture> pictures;
    protected String route = "products";
    private boolean toDelete;

    public Product (){
        pictures = new ArrayList<>();
    }

    public Product(Integer idProduct, String name) {
        this.idProduct = idProduct;
        this.name = name;
    }

    public Product(Integer idProduct){
        this.idProduct = idProduct;
    }

    public String getIdentifier(){
        return "Producto";
    }
    @Override
    public String getRoute() {
        return route;
    }

    @Override
    public String toString(){
        return name;
    }
    @Override
    public Integer getId() {
        return idProduct;
    }
}
