package org.example.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
public class Client extends User{
    private String password;
    private String mail;
    private int gifts;
    private boolean frequentClient;
   @Override
   public String getIdentifier(){
        return "Cliente";
   }
    @Override
    public String getRoute() {
        return "clients";
    }
}
