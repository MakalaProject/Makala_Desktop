package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Comment {
    Integer id;
    String comment;

    public Comment(String comment){
        this.comment = comment;
    }

    @Override
    public String toString() {
        return comment;
    }
}
