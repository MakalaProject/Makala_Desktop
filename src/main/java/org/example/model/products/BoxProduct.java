package org.example.model.products;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoxProduct extends StaticProduct {
    private Measure3Dimensions internalMeasures = new Measure3Dimensions();
    private List<Hole> holesDimensions = new ArrayList<>();
    private boolean closed;

    @Override
    public String getRoute() {
        return route + "/boxes";
    }

    public BigDecimal getArea(){
        return internalMeasures.getY().multiply(internalMeasures.getX());
    }

    public BigDecimal getTotalHolesArea(){
        BigDecimal totalHolesArea = new BigDecimal(0);
        for (Hole hole : holesDimensions) {
            totalHolesArea = totalHolesArea.add(hole.getArea());
        }
        return totalHolesArea;
    }
    public BigDecimal getVolume(){
        if(closed){
            return internalMeasures.getY().multiply(internalMeasures.getZ()).multiply(internalMeasures.getX());
        }
        return internalMeasures.getY().multiply(new BigDecimal(1.25)).multiply(internalMeasures.getZ().multiply(new BigDecimal(1.25))).multiply(internalMeasures.getX());
    }
    public boolean verifyProductBounds(int holeNumber, StaticProduct staticProduct){
        if(holesDimensions.size() == 0){
            if (closed){
                return internalMeasures.getY().compareTo(staticProduct.getMeasures().getY()) >= 0 && internalMeasures.getX().compareTo(staticProduct.getMeasures().getX()) >= 0 && internalMeasures.getZ().compareTo(staticProduct.getMeasures().getZ()) >= 0;
            }else {
                return internalMeasures.getY().multiply(new BigDecimal(1.25)).compareTo(staticProduct.getMeasures().getY()) >= 0 && internalMeasures.getX().compareTo(staticProduct.getMeasures().getX()) >= 0 && internalMeasures.getZ().multiply(new BigDecimal(1.25)).compareTo(staticProduct.getMeasures().getZ()) >= 0;
            }
        }else{
            Hole hole = holesDimensions.stream().filter(ho -> ho.getId() == holeNumber).findAny().orElse(null);
            if (closed){
                return hole.getHoleDimensions().getY().compareTo(staticProduct.getMeasures().getY()) >= 0 && hole.getHoleDimensions().getX().compareTo(staticProduct.getMeasures().getX()) >= 0 && internalMeasures.getZ().compareTo(staticProduct.getMeasures().getZ()) >= 0;
            }else {
                return hole.getHoleDimensions().getY().multiply(new BigDecimal(1.25)).compareTo(staticProduct.getMeasures().getY()) >= 0 && hole.getHoleDimensions().getX().compareTo(staticProduct.getMeasures().getX()) >= 0 && internalMeasures.getZ().multiply(new BigDecimal(1.25)).compareTo(staticProduct.getMeasures().getZ()) >= 0;
            }
        }
    }
    public BigDecimal getAvailableVolume(int idHole, BigDecimal productsVolume){
        BigDecimal returned = new BigDecimal(0);
        if(holesDimensions.size() == 0){
            returned = getVolume().subtract(productsVolume);
        }else{
            for(Hole hole : holesDimensions){
                if(hole.getHoleNumber() == idHole){
                    returned  = hole.getArea().multiply(internalMeasures.getZ()).subtract(productsVolume);
                    break;
                }
            }
        }
        return returned;
    }

    public BigDecimal getAvailableArea() {
        BigDecimal returned = getArea().subtract(getTotalHolesArea());
        return returned;
    }

    @Override
    public String toString(){
        return name;
    }
}
