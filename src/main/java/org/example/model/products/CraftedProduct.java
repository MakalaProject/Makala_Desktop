package org.example.model.products;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CraftedProduct extends StaticProduct{
    private Product productContainer;
    private List<InsideProduct> productsInside = new ArrayList<>();

    @Override
    public String getRoute() {
        return "products/crafted";
    }

}
