package org.example.model;

import lombok.Data;
import org.example.interfaces.IChangeable;
import org.example.model.products.Product;
import org.example.model.products.StaticProduct;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class Gift implements IChangeable<Integer> {
    private Integer idGift;
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
    @Override
    public String toString(){
        return name;
    }

    public void setSelectedProducts(){
        staticProducts.removeIf(GiftProductsToSend::isToDelete);
        ribbons.removeIf(RibbonProductToSend::isToDelete);
        papers.removeIf(PaperProductToSend::isToDelete);
    }
    public void setInternalProducts(List<StaticProduct> staticProducts){
        for(StaticProduct p: staticProducts){
            for(GiftProductsToSend p2: this.staticProducts){
                if (p.getId().equals(p2.getId())){
                    p2.setProduct(p);
                }
            }
        }
    }

    public BigDecimal getProductsVolume(){
        BigDecimal totalVolume= new BigDecimal(0);
        for(GiftProductsToSend g:  staticProducts){
            totalVolume = totalVolume.add(g.getProduct().getVolume().multiply(new BigDecimal(g.amount)));
        }
        return totalVolume;
    }
    
    public BigDecimal getProductsTotalPrice(){
        BigDecimal totalPrice = new BigDecimal(0);
        //totalPrice.add(container.getPrice());
        for (GiftProductsToSend giftProductsToSend : staticProducts){
            totalPrice.add(giftProductsToSend.getProduct().getPrice().multiply(new BigDecimal(giftProductsToSend.amount)));
        }
        for (RibbonProductToSend ribbonProductToSend : ribbons){
            totalPrice.add(ribbonProductToSend.getLengthCm().divide(new BigDecimal(10)).multiply(new BigDecimal(ribbonProductToSend.amount)));
        }
        for (PaperProductToSend paperProductToSend : papers){
            totalPrice.add(paperProductToSend.getHeightCm().multiply(paperProductToSend.getWidthCm()).divide(new BigDecimal(10000)).multiply(new BigDecimal(paperProductToSend.amount)));
        }
        return totalPrice;
    }

    @Override
    public Integer getId() {
        return idGift;
    }
}
