package org.example.model.products;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.interfaces.IPaths;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoxProduct extends StaticProduct {
    private Measure3Dimensions internalMeasures = new Measure3Dimensions();
    private List<Hole> holesDimensions;

    @Override
    public String getRoute() {
        return route + "/boxes";
    }
}
