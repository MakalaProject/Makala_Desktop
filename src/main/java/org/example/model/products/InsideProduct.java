package org.example.model.products;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.interfaces.IChangeable;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsideProduct implements IChangeable<Integer> {
    Integer idProduct;
    String name;
    BigDecimal amount;
    boolean toDelete;

    @Override
    public Integer getId() {
        return getIdProduct();
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
