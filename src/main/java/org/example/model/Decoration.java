package org.example.model;

import lombok.Data;
import org.example.interfaces.IChangeable;

import java.math.BigDecimal;

@Data
public class Decoration implements IChangeable<Integer> {
    private Integer idDeco;
    protected boolean toDelete;
    private BigDecimal price;
    private String name;
    private String description;
    private String path = "/images/bow.png";
    private final String route = "decorations";
    private final String identifier = "Decoración";

    @Override
    public Integer getId() {
        return this.idDeco;
    }


    public void setId(Integer idDeco) {
        this.idDeco = idDeco;
    }
}
