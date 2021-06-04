package org.example.model.products;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.interfaces.IChangeable;
import org.example.interfaces.IPaths;
import org.example.model.Picture;

import java.math.BigDecimal;
import java.util.ArrayList;

@Data
@EqualsAndHashCode
@AllArgsConstructor
public class Product implements IPaths, IChangeable<Integer> {
    protected Integer idProduct;
    protected String name;
    protected ProductClassDto productClassDto;
    protected String privacy;
    protected BigDecimal price;
    protected Integer max;
    protected Integer min;
    protected BigDecimal avgDays;
    @EqualsAndHashCode.Exclude
    protected BigDecimal stock;
    @EqualsAndHashCode.Exclude
    protected ObservableList<ProductClassDto> classificationsPerType = FXCollections.observableArrayList();

    protected ArrayList<Picture> pictures;
    protected String route = "products";
    private boolean toDelete;

    public Product (){
        pictures = new ArrayList<>();
        stock = new BigDecimal("0");
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

    public void sortList(){

    }

    public BigDecimal formatStock(Label stockLabel) {
        stockLabel.setText("pzas");
        return null;
    };

    public void getRealStock(BigDecimal stock){

    };

    public Integer formatMax(){
      return max;
    };

    public Integer formatMin(){
        return min;
    };

    public void getMax(Integer minMax){
        this.max = minMax;
    }

    public void getMin(Integer minMax){
        this.min = minMax;
    }
}
