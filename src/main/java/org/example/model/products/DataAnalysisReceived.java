package org.example.model.products;

import lombok.Data;
import org.example.interfaces.IListController;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DataAnalysisReceived {
    private LocalDate date;
    private Integer amount;
}
