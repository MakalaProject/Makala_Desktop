package org.example.model;


public class Department {
    private int idDepartment;
    private String departName;
    public Department(){

    }

    @Override
    public String toString() {
        return departName;
    }

    public int getIdDepartment() {
        return idDepartment;
    }

    public void setIdDepartment(int idDepartment) {
        this.idDepartment = idDepartment;
    }

    public String getDepartName() {
        return departName;
    }


    public void setDepartName(String departName) {
        this.departName = departName;
    }
}
