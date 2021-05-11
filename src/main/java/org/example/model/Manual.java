package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Set;

@Data
@NoArgsConstructor
public class Manual {
    private Integer id;
    private String name;
    private String type;
    private Integer totalTime;
    private ArrayList<Step> steps;
    private String route = "manuals";

}
