package org.example.interfaces;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public interface IControllerCreate<D> {
    default void showAlertEmptyFields(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Campos vacios");
        alert.setHeaderText("No puedes dejar campos sin llenar'");
        alert.setContentText("Por favor completa los datos ");
        Optional<ButtonType> result = alert.showAndWait();
    }
     D setInfo(D object);
}
