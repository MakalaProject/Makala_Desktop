package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Set;

@Data
@NoArgsConstructor
public class GiftEditable {
    protected Integer idOrderGift;
    protected Integer idGift;
    protected Gift gift;
    protected Integer amountGifts;
    protected ArrayList<ProductEdition> productsEdition;
    protected boolean toDelete = false;

    @Override
    public String toString() {
        return gift.getName();
    }
}
