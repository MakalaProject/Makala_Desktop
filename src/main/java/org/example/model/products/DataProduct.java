package org.example.model.products;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DataProduct extends DataAnalysisReceived{
    private Product product;
}
