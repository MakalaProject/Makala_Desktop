package org.example.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Rebate {
    @EqualsAndHashCode.Exclude
    protected Integer id;
    protected Integer percent;
    @EqualsAndHashCode.Exclude
    protected LocalDateTime startDate;
    @EqualsAndHashCode.Exclude
    protected LocalDateTime endDate;
    protected transient String route;
    protected String identifier = "Rebaja";
    @EqualsAndHashCode.Exclude
    protected String type;
}
