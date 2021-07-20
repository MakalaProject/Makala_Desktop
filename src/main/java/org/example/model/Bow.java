package org.example.model;

import lombok.Data;
import org.example.interfaces.IChangeable;

import java.math.BigDecimal;

@Data
public class Bow implements IChangeable<Integer> {
    private Integer id;
    protected boolean toDelete;
    private BigDecimal price;
    private Integer amountRibbons;
    private String name;
    private String description;
    private String path;
    private final String route = "decorations";
    private final String identifier = "Decoración";

}
