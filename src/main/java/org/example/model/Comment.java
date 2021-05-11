package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Comment {
    private Integer id;
    private String comment;
    private LocalDateTime date;
}
