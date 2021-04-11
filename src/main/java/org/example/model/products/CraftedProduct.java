package org.example.model.products;

public class CraftedProduct {
    private int id;
    private Product containerProduct;
    private Product internalProduct;
    private float weightGr;

    public CraftedProduct (Product cProduct, Product iProduct){
        this.containerProduct = cProduct;
        this.internalProduct = iProduct;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getContainerProduct() {
        return containerProduct;
    }

    public void setContainerProduct(Product containerProduct) {
        this.containerProduct = containerProduct;
    }

    public Product getInternalProduct() {
        return internalProduct;
    }

    public void setInternalProduct(Product internalProduct) {
        this.internalProduct = internalProduct;
    }

    public float getWeightGr() {
        return weightGr;
    }

    public void setWeightGr(float weightGr) {
        this.weightGr = weightGr;
    }
}
