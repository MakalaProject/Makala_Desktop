package org.example.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
public class Catalog {
    private int idCatalog;
    private String name;
    private String path = "/images/catalog.png";
    private CatalogClassification catalogClassification;
    private List<Gift> gifts = new ArrayList<>();
    private String route = "catalogs";
    private String identifier = "catalogo";

    public void setSelectedGifts() {
        gifts.removeIf(Gift::isToDelete);
    }

    public void sortList(){
        gifts.sort(Comparator.comparing(Gift::getId));
    }
}
