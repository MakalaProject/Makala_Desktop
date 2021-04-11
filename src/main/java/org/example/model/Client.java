package org.example.model;

public class Client extends User{
    private String password;
    private String mail;
    public Client(){
        super();
        mail = "xx";
        password = "";
        firstName = "Nuevo";
        lastName = "Cliente";
        phone = "33000";
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public boolean compareClient(Client client){
        if (firstName.equals(client.firstName)
        && lastName.equals(client.lastName)
        && phone.equals(client.phone)
        && mail.equals(client.mail)){
            return true;
        }
        return false;
    }
    @Override
    public String getRoute() {
        return "clients";
    }
}
