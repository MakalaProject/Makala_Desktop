package org.example.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.interfaces.IChangeable;
import org.example.model.products.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
public class Gift implements IChangeable<Integer> {
    private Integer idGift;
    private String name;
    @EqualsAndHashCode.Exclude
    private BigDecimal price;
    @EqualsAndHashCode.Exclude
    private BigDecimal flowers;
    @EqualsAndHashCode.Exclude
    private BigDecimal applications;
    private String privacy;
    private BigDecimal laborPrice;
    private List<GiftProductsToSend> staticProducts = new ArrayList<>();
    private List<Decoration> decorations = new ArrayList<>();
    private List<PaperProductToSend> papers = new ArrayList<>();
    private Product container;
    private List<Picture> pictures;
    private final String route ="gifts";
    private final String identifier = "regalo";
    private int rating;
    private boolean toDelete;
    @EqualsAndHashCode.Exclude
    private int totalTime = 0;
    @EqualsAndHashCode.Exclude
    private int stock = 0;
    private ArrayList<Step> steps = new ArrayList<>();

    public Gift(){
    }
    @Override
    public String toString(){
        return name;
    }

    public void setSelectedProducts(){
        staticProducts.removeIf(GiftProductsToSend::isToDelete);
        decorations.removeIf(Decoration::isToDelete);
        papers.removeIf(PaperProductToSend::isToDelete);
    }
    public void setInternalProducts(List<StaticProduct> staticProducts){
        for(StaticProduct p: staticProducts){
            for(GiftProductsToSend p2: this.staticProducts){
                if (p.getId().equals(p2.getProduct().getIdProduct())){
                    p2.setProduct(p);
                }
            }
        }
    }

    public BigDecimal getProductsVolume(int holeNumber, BoxProduct container){
        BigDecimal totalVolume= new BigDecimal(0);
        if (container.getHolesDimensions().size() != 0){
            for(GiftProductsToSend g:  staticProducts){
                if (g.holeNumber == holeNumber) {
                    totalVolume = totalVolume.add(g.getProduct().getVolume().multiply(new BigDecimal(g.amount)));
                }
            }
        }else {
            for (GiftProductsToSend g : staticProducts) {
                totalVolume = totalVolume.add(g.getProduct().getVolume().multiply(new BigDecimal(g.amount)));
            }
        }
        return totalVolume;
    }
    
    public BigDecimal getProductsTotalPrice(){
        BigDecimal totalPrice = new BigDecimal(0);
        totalPrice = totalPrice.add(container.getPrice());
        for (GiftProductsToSend giftProductsToSend : staticProducts){
            if (!giftProductsToSend.toDelete) {
                totalPrice = totalPrice.add(giftProductsToSend.getProduct().getPrice().multiply(new BigDecimal(giftProductsToSend.amount)));
            }
        }

        for (PaperProductToSend paperProductToSend : papers){
            if (!paperProductToSend.toDelete) {
                totalPrice = totalPrice.add(paperProductToSend.getHeightCm().multiply(paperProductToSend.getWidthCm()).divide(new BigDecimal(10000)).multiply(paperProductToSend.getProduct().getPrice()).multiply(new BigDecimal(paperProductToSend.amount)));
            }
        }
        return totalPrice;
    }

    public void sortList(){
        decorations.sort(Comparator.comparing(Decoration::getId));
        papers.sort(Comparator.comparing(PaperProductToSend::getIdProduct));
        staticProducts.sort(Comparator.comparing(GiftProductsToSend::getIdProduct));
    }

    @Override
    public Integer getId() {
        return idGift;
    }


    public void setInternalPapers(List<PaperProduct> papers) {
        for(PaperProduct p: papers){
            for(PaperProductToSend p2: this.papers){
                if (p.getId().equals(p2.getProduct().getIdProduct())){
                    p2.setProduct(p);
                }
            }
        }
    }
}
