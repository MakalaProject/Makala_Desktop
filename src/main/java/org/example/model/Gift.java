package org.example.model;

import java.math.BigDecimal;

public class Gift {
    private int idGitft;
    private String name;
    private BigDecimal price;
    //private List<Product> productList;

    public Gift(){

    }

    public int getIdGitft() {
        return idGitft;
    }

    public void setIdGitft(int idGitft) {
        this.idGitft = idGitft;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
