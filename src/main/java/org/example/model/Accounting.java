package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Accounting {
    private LocalDate month;
    private BigDecimal bought;
    private BigDecimal sold;
    private BigDecimal balance;

    @Override
    public String toString(){
        return "Contabilidad del mes " + month.getMonth().toString();
    }
}
