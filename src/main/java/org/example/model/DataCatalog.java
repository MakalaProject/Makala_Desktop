package org.example.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DataCatalog extends DataAnalysisReceived<Catalog>{
    private Catalog catalog;

    @Override
    public String toString() {
        return catalog.getName();
    }

    @Override
    public String getDescription() {
        return catalog.getName() + "\n"+
                "Cantidad: " + getAmount();
    }

    @Override
    public String getImagePath() {
        return catalog.getPath();
    }

    @Override
    public String getClasification() {
        return catalog.getCatalogClassification().getName();
    }

    @Override
    public void setObject(Catalog object) {
        catalog = object;
    }

    @Override
    public Integer getId() {
        return catalog.getIdCatalog();
    }

    @Override
    public String getRoute() {
        return catalog.getRoute();
    }

    @Override
    public Catalog getObject() {
        return catalog;
    }
}
