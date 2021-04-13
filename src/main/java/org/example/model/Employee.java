package org.example.model;

import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class Employee extends User {
    private String password;
    private List<Department> departments;

    public Employee(){
        setFirstName("Nuevo empleado");
        setLastName("");
        setPhone("");
        password = "";
    }

    public boolean isDepartment(String departName){
        for (Department department: departments) {
            if (department.getDepartName().toLowerCase().contains(departName)){
                return true;
            }

        }
        return false;

    }

    @Override
    public String getRoute() {
        return route + "/employees";
    }

}
