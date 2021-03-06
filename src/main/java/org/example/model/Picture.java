package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.interfaces.IChangeable;

import java.io.File;

@Data
@EqualsAndHashCode
@AllArgsConstructor
public class Picture implements IChangeable<Integer> {
    private Integer id;
    private String path;
    private boolean toDelete;

    public Picture(String path){
        this.path = path;
    }


}
