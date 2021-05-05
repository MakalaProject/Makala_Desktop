package org.example.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Catalog {
    private int idCatalog;
    private String name;
    private String path;
    private CatalogClassification catalogClassification;
    private List<Gift> gifts = new ArrayList<>();
}
