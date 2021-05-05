package org.example.model;

import lombok.Data;

@Data
public class CatalogClassification {
    private int idCatalogType;
    private String name;

    @Override
    public String toString(){
        return name;
    }
}
