package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CalendarActivity {
    protected Integer idActivity;
    protected LocalDateTime date;
    protected Integer idEmployee;
    protected String status;
    protected Integer dailyNumber;
    protected Employee employee = new Employee();
}
