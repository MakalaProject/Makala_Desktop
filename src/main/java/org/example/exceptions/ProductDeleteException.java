package org.example.exceptions;

import lombok.Data;

@Data
public class ProductDeleteException extends Exception{
    private int status;

    public ProductDeleteException (String message, int statusCode) {
        super(message);
        this.status = statusCode;
    }


}
