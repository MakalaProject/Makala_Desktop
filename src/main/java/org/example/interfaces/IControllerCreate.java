package org.example.interfaces;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public interface IControllerCreate<D> {
    default void showAlertEmptyFields(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Información incompleta");
        alert.setHeaderText(message);
        alert.setContentText("Por favor verifica los campos");
        Optional<ButtonType> result = alert.showAndWait();
    }
     D setInfo(D object);
}
