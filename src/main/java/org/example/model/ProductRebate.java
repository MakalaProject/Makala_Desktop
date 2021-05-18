package org.example.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.model.products.Product;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ProductRebate extends Rebate{
    @EqualsAndHashCode.Exclude
    private Product product;

    @Override
    public String getRoute() {
        return "product-rebates";
    }
}
