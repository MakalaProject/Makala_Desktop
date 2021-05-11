package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Step {
    private Integer idStep;
    private String path;
    private Integer stepNumber;
    private String instruction;
    private Integer time;
}
