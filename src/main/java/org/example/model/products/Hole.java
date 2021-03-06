package org.example.model.products;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.interfaces.IChangeable;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Hole implements IChangeable<Integer> {
    private Integer holeNumber;
    private Measure2Dimensions holeDimensions;
    private boolean toDelete;

    public Hole(){
        holeDimensions = new Measure2Dimensions();
    }


    @Override
    public Integer getId() {
        return holeNumber;
    }

    public BigDecimal getArea(){
        return holeDimensions.getX().multiply(holeDimensions.getY());
    }

    @Override
    public String toString(){
        return "División: " + holeNumber.toString() + " x: " + holeDimensions.getX().toString() + " y: " +  holeDimensions.getY().toString();
    }
}

