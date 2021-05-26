package org.example.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DataGift extends DataAnalysisReceived<Gift>{
    private Gift gift;
    @Override
    public String toString() {
        return gift.getName();
    }
    @Override
    public String getDescription() {
        return gift.getName()
                + "\n"+
                "Cantidad: " + getAmount();
    }

    @Override
    public String getImagePath() {
        if (gift.getPictures().size()>0) {
            return gift.getPictures().get(0).getPath();
        }
        return "/images/gift.png";
    }

    @Override
    public String getClasification() {
        return gift.getIdentifier();
    }

    @Override
    public void setObject(Gift object) {
        gift = object;
    }

    @Override
    public Integer getId() {
        return gift.getId();
    }

    @Override
    public String getRoute() {
        return gift.getRoute();
    }

    @Override
    public Gift getObject() {
        return gift;
    }
}
