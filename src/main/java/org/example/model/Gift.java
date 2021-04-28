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
    private int idGitft;
    private String name;
    private BigDecimal price;
    private BigDecimal labor;
    private List<StaticProduct> staticProducts;
    private List<RibbonProduct> ribbons;
    private List<PaperProduct> papers;
    private Product container;
    private List<Picture> pictures;
    private final String route ="gifts";
    private final String identifier = "regalo";

    public Gift(){

    }

}
