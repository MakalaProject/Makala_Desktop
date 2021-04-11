package org.example.model.products;



public class ProductClassDto {

    private int idClassification;
    private String classification;

    public int getIdClassification() {
        return idClassification;
    }


    public void setIdClassification(int idClassification) {
        this.idClassification = idClassification;
    }

    public String getClassification() {
        return classification;
    }


    public void setClassification(String classification) {
        this.classification = classification;
    }

    @Override
    public String toString() {
        return this.classification;
    }

}
