package org.example.model.products;

import javafx.scene.control.Label;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.interfaces.IPaths;

import java.math.BigDecimal;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class StaticProduct extends Product {
    protected Measure3Dimensions measures;

    public StaticProduct(){
        super();
        measures = new Measure3Dimensions();
    }

    public StaticProduct(Integer idProduct) {
        this.idProduct = idProduct;
    }
    public StaticProduct(Integer idProduct, String name) {
        this.idProduct = idProduct;
        this.name = name;
    }

    @Override
    public String getRoute() {
        return route + "/statics";
    }

    @Override
    public String toString(){
        return name;
    }

    @Override
    public BigDecimal formatStock(Label label) {
        label.setText("pzas");
        return this.stock;
    }

    @Override
    public void getRealStock(BigDecimal stock) {
        this.stock = stock;
    }


    public BigDecimal getVolume(){
        return measures.getX().multiply(measures.getZ().multiply(measures.getY()));
    }
}
