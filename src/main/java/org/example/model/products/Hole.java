package org.example.model.products;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Hole {
    private Integer holeNumber;
    private Measure2Dimensions holeDimensions;

    public Hole(){
        holeDimensions = new Measure2Dimensions();
    }
    @Override
    public String toString(){
        return "Orificio " + holeNumber;
    }
}
