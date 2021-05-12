package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Comment {
    Integer id;
    String comment;
    LocalDateTime date;
    public Comment(String comment){
        this.comment = comment;
    }

    @Override
    public String toString() {
        return comment;
    }
}
