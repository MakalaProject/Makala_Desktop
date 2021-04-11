package org.example.model;

import org.example.model.products.ProductClassDto;

public class Provider extends User {
    private ProductClassDto productClassDto;
    private boolean productReturn;
    private String cardNumber;
    private String mail;
    private int shippingTime;
    private String typeProvider;
    private String clabe;

    public Provider(){
        super();
        firstName = "Nuevo";
        lastName = "Proveedor";
        mail = "@gmail.com";
        clabe = "xxxxx";
        phone = "33000";
        cardNumber = "xxxxxx";
        shippingTime = 0 ;
        typeProvider = "Emprendedor";
        productReturn = true;
    }

    public ProductClassDto getProductClass() {
        return productClassDto;
    }

    public void setProductClass(ProductClassDto productClass) {
        this.productClassDto = productClass;
    }


    public boolean isProductReturn() {
        return productReturn;
    }

    public void setProductReturn(boolean productReturn) {
        this.productReturn = productReturn;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getEmail() {
        return mail;
    }

    public void setEmail(String email) {
        this.mail = email;
    }

    public int getShippingTime() {
        return shippingTime;
    }

    public void setShippingTime(int shippingTime) {
        this.shippingTime = shippingTime;
    }

    public String getType() {
        return typeProvider;
    }

    public void setType(String type) {
        this.typeProvider = type;
    }

    public String getClabe() {
        return clabe;
    }

    public void setClabe(String clabe) {
        this.clabe = clabe;
    }

    public boolean CompareProvider(Provider provider){
        if (firstName.equals(provider.firstName)
        && lastName.equals(provider.lastName)
        && phone.equals(provider.phone)
        && productReturn == provider.productReturn
        && clabe.equals(provider.clabe)
        && cardNumber.equals(provider.cardNumber)
        && mail.equals(provider.mail)
        && shippingTime == provider.shippingTime
        && typeProvider.equals(provider.typeProvider)
        && productClassDto.getClassification().equals(provider.productClassDto.getClassification())){
            return true;
        }
        return false;
    }
}
