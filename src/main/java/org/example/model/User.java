package org.example.model;



public class User {
    protected int idUser;
    protected String firstName;
    protected String lastName;
    protected String phone;
    protected String route = "users";

    public User(){
        //this.phone = new char[10];
    }

    public int getId() {
        return idUser;
    }

    public String getRoute() {
        return route;
    }


    public void setId(int idUser) {
        this.idUser = idUser;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }
}
