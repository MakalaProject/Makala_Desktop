package org.example.model;

import javafx.fxml.FXML;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.products.Action;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class WorkDays {
    private Integer id;
    private Employee employee;
    private LocalDate startDate;
    private LocalDate finishDate;
    private boolean monToFri = true;
    private Action action = Action.UPDATE;
    @Override
    public String toString() {
        return "Intervalo de trabajo";
    }
}
