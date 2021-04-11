package org.example.model;

import java.util.List;

public class Employee extends User {
    private String password;
    private List<Department> departments;

    public Employee(){
        super();
        setFirstName("Nuevo empleado");
        setLastName("");
        setPhone("");
        password = "";
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public boolean isDepartment(String departName){
        for (Department department: departments) {
            if (department.getDepartName().toLowerCase().contains(departName)){
                return true;
            }

        }
        return false;

    }

    public void setDepartments(List<Department> department) {
        this.departments = department;
    }

    public boolean compareEmployee(Employee employee) {
        if(firstName.equals(employee.firstName)
                && phone.equals(employee.phone)
                && (employee.password == null || password.equals(employee.password))
                && lastName.equals(employee.lastName)){
            return true;
        }
        return false;
    }

    @Override
    public String getRoute() {
        return route + "/employees";
    }

}
