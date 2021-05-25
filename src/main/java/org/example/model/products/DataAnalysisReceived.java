package org.example.model.products;

import lombok.Data;
import org.example.interfaces.IListController;

import java.time.LocalDateTime;

@Data
public class DataAnalysisReceived {
    private LocalDateTime date;
    private Integer amount;
}
