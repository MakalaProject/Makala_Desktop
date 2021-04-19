package org.example.model.products;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CraftedProduct extends StaticProduct{
    private Product productContainer;
    private List<InsideProduct> productsInside = new ArrayList<>();

}
