package org.example.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.model.Adress.Address;
import org.example.model.products.Product;
import org.example.model.products.ProductClassDto;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class Provider extends User {
    private ProductClassDto productClassDto;
    private boolean productReturn;
    private String cardNumber;
    private String mail;
    private int shippingTime;
    private String typeProvider;
    private String clabe;
    private Address address;
    private List<Product> products = new ArrayList<>();

    public Provider(){
        super();
        firstName = "Nuevo";
        lastName = "Proveedor";
        mail = "@gmail.com";
        clabe = "xxxxx";
        phone = "33000";
        cardNumber = "xxxxxx";
        shippingTime = 0 ;
        typeProvider = "Emprendedor";
        productReturn = true;
    }

    @Override
    public String getIdentifier(){
        return "Proovedor";
    }

}
