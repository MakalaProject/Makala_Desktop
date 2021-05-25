package org.example.model.products;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.model.Catalog;

@EqualsAndHashCode(callSuper = true)
@Data
public class DataCatalog extends DataAnalysisReceived{
    private Catalog catalog;
}
