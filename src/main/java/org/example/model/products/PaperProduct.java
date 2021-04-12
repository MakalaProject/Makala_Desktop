package org.example.model.products;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaperProduct extends Product {
    private String rgb;


    @Override
    public String getRoute(){
        return route + "/papers";
    }
}
