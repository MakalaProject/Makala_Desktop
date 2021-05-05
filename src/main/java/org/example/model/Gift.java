package org.example.model;

import lombok.Data;
import org.example.interfaces.IChangeable;
import org.example.model.products.Product;
import org.example.model.products.StaticProduct;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Gift {
    private int idGift;
    private String name;
    private BigDecimal price;
    private String privacy;
    private BigDecimal laborPrice;
    private List<GiftProductsToSend> staticProducts = new ArrayList<>();
    private List<RibbonProductToSend> ribbons = new ArrayList<>();
    private List<PaperProductToSend> papers = new ArrayList<>();
    private Product container;
    private List<Picture> pictures;
    private final String route ="gifts";
    private final String identifier = "regalo";
    private int rating;
    private boolean toDelete;

    public Gift(){

    }

}
