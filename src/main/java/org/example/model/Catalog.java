package org.example.model;

import java.util.List;

public class Catalog {
    private int idCatalog;
    private String name;
    private CatalogClassification catalogClassification;
    private List<Gift> gifts;

    public Catalog(){

    }

    public int getIdCatalog() {
        return idCatalog;
    }

    public void setIdCatalog(int idCatalog) {
        this.idCatalog = idCatalog;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CatalogClassification getCatalogClassification() {
        return catalogClassification;
    }

    public void setCatalogClassification(CatalogClassification catalogClassification) {
        this.catalogClassification = catalogClassification;
    }

    public List<Gift> getGifts() {
        return gifts;
    }

    public void setGifts(List<Gift> gifts) {
        this.gifts = gifts;
    }
}
