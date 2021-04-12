package org.example.model.products;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BoxProduct extends StaticProduct{
    private Measure3Dimensions internalMeasures;
    private List<Hole> holesDimensions;
}
