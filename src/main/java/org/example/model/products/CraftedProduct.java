package org.example.model.products;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class CraftedProduct extends StaticProduct{
    private List<InsideProduct> internalProducts = new ArrayList<>();

}
