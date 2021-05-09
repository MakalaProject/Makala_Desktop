package org.example.model;

import lombok.Data;

import javax.xml.transform.sax.SAXResult;
import java.util.ArrayList;
import java.util.List;

@Data
public class Catalog {
    private int idCatalog;
    private String name;
    private String path;
    private CatalogClassification catalogClassification;
    private List<Gift> gifts = new ArrayList<>();
    private String route = "catalogs";
    private String identifier = "catalogo";

    public void setSelectedGifts() {
        gifts.removeIf(Gift::isToDelete);
    }
}
