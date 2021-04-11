package org.example.interfaces;

import org.example.model.products.Product;

public interface IControllerProducts<D> {
    String[] getIdentifier();
    String getResource();
    D getObject();
    void setObject(D object);
    D getObjectByFields();
    D findObject(Product Object);


}
