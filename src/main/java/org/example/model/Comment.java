package org.example.model;

import lombok.Data;

@Data
public class Comment {
    Integer id;
    String comment;
    public Comment(String comment){
        this.comment = comment;
    }
}
