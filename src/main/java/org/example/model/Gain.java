package org.example.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Gain {
    private Integer id;
    private BigDecimal factor;
    private Integer maxPrice;
    private Integer minPrice;
}
