package org.example.model;

import lombok.Data;
import lombok.ToString;
import org.example.interfaces.IChangeable;

@Data
public class DecorationToSend implements IChangeable<Integer> {
    private Integer id;
    private final Integer amount = 1;
    private Decoration decoration;
    private boolean toDelete;

    public DecorationToSend(Decoration decoration) {
        this.decoration = decoration;
        this.id = null;
    }

    @Override
    public String toString(){
        return decoration.getName();
    }
    @Override
    public Integer getId(){
        return decoration.getIdDeco();
    }
}
