package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.products.Product;

@Data
@NoArgsConstructor
public class ProductEdition {
    protected Integer idEdition;
    protected Product newProduct;
    protected Integer idGiftContent;
    protected Integer newAmount;
    protected Integer lessAmountOldProduct;
    protected boolean toDelete = false;
}
