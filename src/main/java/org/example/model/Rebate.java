package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Rebate {
    protected Integer Id;
    protected Integer percent;
    protected LocalDate startDate;
    protected LocalDate endDate;
    private transient String route;
    protected String identifier = "Rebaja";
}
