package org.example.model;

import lombok.Data;
import org.example.model.products.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class Purchase {
    Integer id;
    Integer employee;
    Integer idProvider;
    LocalDate orderDate;
    LocalDate payDate;
    LocalDate receivedDate;
    String payMethod;
    BigDecimal price;
    List<PurchaseProduct> products = new ArrayList<>();
    Comment comment;
    String route = "purchases";
    String identifier = "Compra";

    public void setSelectedProducts(){
        products.removeIf(PurchaseProduct::getToDelete);
    }

    @Override
    public String toString(){
        return "Compra: " + id;
    }
}
