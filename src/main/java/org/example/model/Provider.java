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
    private String cardInfo;
    private String mail;
    private int shippingTime;
    private String typeProvider;
    private Address address;
    private List<Product> products = new ArrayList<>();

    public Provider(){
        super();
    }
    @Override
    public String getRoute(){
        return route+"/providers";
    }
    @Override
    public String getIdentifier(){
        return "Proveedor";
    }

    public void setSelectedProducts() {
        products.removeIf(Product::isToDelete);
    }
}
