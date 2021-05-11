package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
public class ProductExpiration {
    LocalDate expiryDate;
    BigDecimal amount;
}
