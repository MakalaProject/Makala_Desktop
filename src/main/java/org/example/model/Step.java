package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.interfaces.IChangeable;
import org.example.model.products.Action;

@Data
@NoArgsConstructor
public class Step  implements IChangeable<Integer> {
    private Integer idStep;
    private String path;
    private Integer stepNumber;
    private String instruction;
    private Integer time;
    private Action action = Action.UPDATE;
    private boolean toDelete = false;

    @Override
    public String toString() {
        return "Paso " + stepNumber;
    }

    @Override
    public Integer getId() {
        return idStep;
    }
}
