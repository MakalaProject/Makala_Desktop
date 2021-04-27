package org.example.interfaces;

import org.example.model.products.Product;
import org.example.services.Request;

public interface IControllerProducts<D> {
    String[] getIdentifier();
    String getResource();
    D getObject();
    void setObject(D object);
    default void clearController(){};

    /**
     * Function to get the specific information of the object with an external server
     * @param object The product to get the id and the general info of the product
     * @return The object obtained
     */
    D findObject(Product object);

    /**
     * Function to call the method {@link Request#find(String, int, Class)} of the class {@link Request}
     * @param object The object to get the id.
     * @param path The path of the service
     * @param dClass The class type to make a new instance
     * @return The object found
     */
    default D findObject(Product object, String path, Class<D> dClass){
        D response = (D) Request.find(path, object.getIdProduct(), dClass);
        setObject(response);
        return response;
    }

    void updateList();

    default boolean indispensableChanges(){
        return false;
    };
}
