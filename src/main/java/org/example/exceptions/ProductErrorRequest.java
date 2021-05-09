package org.example.exceptions;


import lombok.Data;

@Data
public class ProductErrorRequest {
    private String message;
    private int status;
    ProductErrorRequest(){

    }

}
