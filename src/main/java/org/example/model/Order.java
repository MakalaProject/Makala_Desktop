package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.Adress.Address;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

@Data
@NoArgsConstructor
public class Order {
    protected Integer idOrder;
    protected BigDecimal totalPrice;
    protected LocalDateTime date;
    protected LocalDateTime totalPaymentDate;
    protected Integer idClient;
    protected String status;
    protected BigDecimal shippingPrice;
    protected LocalDateTime shippingDate;
    protected LocalDate estimatedShippingDate;
    protected LocalDateTime advanceDate;
    protected Address address;
    protected ArrayList<Comment> comments;
    protected ArrayList<GiftEditable> gifts;
    private String route = "orders";

    @Override
    public String toString(){
        return "Venta: " + this.getIdOrder().toString();
    }
}
