package org.example.model.products;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.interfaces.IPaths;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class BoxProduct extends StaticProduct {
    private Measure3Dimensions internalMeasures = new Measure3Dimensions();
    private List<Hole> holesDimensions = new ArrayList<>();

    @Override
    public String getRoute() {
        return route + "/boxes";
    }

    public BigDecimal getArea(){
        return internalMeasures.getY().multiply(internalMeasures.getX());
    }

    public BigDecimal getAvailableArea() {
        BigDecimal totalHolesArea = new BigDecimal(0);
        for (Hole hole : holesDimensions) {
            totalHolesArea = totalHolesArea.add(hole.getArea());
        }
        return getArea().subtract(totalHolesArea);
    }

    public BoxProduct(){
        this.internalMeasures.setZ(new BigDecimal(0));
        this.internalMeasures.setX(new BigDecimal(0));
        this.internalMeasures.setY(new BigDecimal(0));

    }
}
