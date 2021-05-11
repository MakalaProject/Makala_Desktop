package org.example.model;

import lombok.Data;
import org.example.model.products.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class Purchase {
    Integer id;
    Integer idProvider;
    LocalDate orderDate;
    LocalDate payDate;
    LocalDate receivedDate;
    String payMethod;
    BigDecimal price;
    List<PurchaseProduct> products;
    Comment comment;
    String route = "purchases";
    String identifier = "Compra";

    public void setSelectedProducts(){
        products.removeIf(PurchaseProduct::getToDelete);
    }
}
