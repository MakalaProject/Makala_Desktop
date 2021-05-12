package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Rebate {
    protected Integer Id;
    protected Integer percent;
    protected LocalDateTime startDate;
    protected LocalDateTime endDate;
    private transient String route;
    protected String identifier = "Rebaja";
}
