package org.example.model;

import lombok.Data;
import org.example.model.products.PaperProduct;
import org.example.model.products.Product;
import org.example.model.products.RibbonProduct;
import org.example.model.products.StaticProduct;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Gift {
    private int idGift;
    private String name;
    private BigDecimal price;
    private BigDecimal labor;
    private List<GiftProductsToSend> staticProducts;
    private List<RibbonProductToSend> ribbons;
    private List<PaperProductToSend> papers;
    private Product container;
    private List<Picture> pictures;
    private final String route ="gifts";
    private final String identifier = "regalo";

    public Gift(){

    }

    public void addProduct(GiftProductsToSend product){
        staticProducts.add(product);
    }
    public void addPaper(PaperProductToSend product){
        staticProducts.add(product);
    }
    public void addRibbon(RibbonProductToSend product){
        staticProducts.add(product);
    }

}
