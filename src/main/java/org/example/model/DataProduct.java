package org.example.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.model.products.Product;

@EqualsAndHashCode(callSuper = true)
@Data
public class DataProduct extends DataAnalysisReceived<Product>{
    private Product product;

    @Override
    public String toString() {
        return product.getName();
    }

    @Override
    public String getDescription() {
        return product.getName() +
                " Clasificación: " +
                product.getProductClassDto().getClassification()
                + "\n" +
                " Tipo: " +
                product.getProductClassDto().getProductType()
                + "\n"+
                "Cantidad: " + getAmount();
    }

    @Override
    public String getImagePath() {
        if (product.getPictures().size()>0) {
            return product.getPictures().get(0).getPath();
        }
        return "/images/product.png";
    }

    @Override
    public String getClasification() {
        return product.getProductClassDto().getClassification();
    }

    @Override
    public void setObject(Product object) {
        product = object;
    }

    @Override
    public Integer getId() {
        return product.getIdProduct();
    }

    @Override
    public String getRoute() {
        return product.getRoute();
    }

    @Override
    public Product getObject() {
        return product;
    }
}
