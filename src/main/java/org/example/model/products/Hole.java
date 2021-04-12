package org.example.model.products;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.interfaces.IChangeable;

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
    public String toString(){
        return "Orificio " + holeNumber;
    }

    @Override
    public Integer getId() {
        return holeNumber;
    }
}
