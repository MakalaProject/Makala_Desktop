package org.example.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
public class Client extends User{
    private String password;
    private String mail;
    public Client(){
        mail = "xx";
        password = "";
        firstName = "Nuevo";
        lastName = "Cliente";
        phone = "33000";
    }
   @Override
   public String getIdentifier(){
        return "Cliente";
   }
    @Override
    public String getRoute() {
        return "clients";
    }
}
