package org.example.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.interfaces.IChangeable;

@Data
@AllArgsConstructor
public class Department implements IChangeable<Integer> {
    private Integer idDepartment;
    private String departName;
    private boolean toDelete;

    @Override
    public String toString() {
        return departName;
    }

    @Override
    public Integer getId() {
        return idDepartment;
    }

}
