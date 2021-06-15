package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.products.Product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

@Data
@NoArgsConstructor
public class CalendarDetailedActivity {
    Integer idActivity;
    LocalDateTime date;
    Integer idEmployee;
    String status;
    Integer dailyNumber;
    Gift gift;
    String task;
    Integer time;
    Integer amount;
    String imageLink;
    Integer idOrder;
    Integer idOrderGift = 0;
    ArrayList<Product> productsUsed;
    Employee employee = new Employee();
}
