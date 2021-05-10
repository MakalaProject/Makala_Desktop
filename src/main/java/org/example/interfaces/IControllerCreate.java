package org.example.interfaces;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.example.model.Gift;
import org.example.model.products.BoxProduct;
import org.example.model.products.Product;

import java.util.Optional;

public interface IControllerCreate<D> {
    default void showAlertEmptyFields(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Información incompleta");
        alert.setHeaderText(message);
        alert.setContentText("Por favor verifica los campos");
        alert.showAndWait();
    }
     void setInfo(D object);

    default void duplyElementAlert(String identifier){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(identifier +" duplicado");
        alert.setHeaderText(identifier +" duplicado");
        alert.setContentText("No puedes introducir un"+identifier.toLowerCase()+"con el mismo nombre");
        alert.showAndWait();
    }
    default void errorAlert(String errorMessage){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Error");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    default void setProduct(D product, boolean isCreate){

    }
    default void setProduct(D product, boolean isCreate, BoxProduct container, Gift gift){

    }
}
