package org.example.controllers.parent.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.example.interfaces.IControllerCreate;
import org.example.model.ChangedVerificationFields;
import org.example.model.FocusVerificationFields;
import org.example.model.User;
import java.net.URL;
import java.util.ResourceBundle;

public class UserGenericController <D extends User> implements Initializable, IControllerCreate<D> {
    @FXML protected TextField nombresField;
    @FXML protected TextField apellidosField;
    @FXML protected TextField telefonoField;
    @FXML protected FontAwesomeIconView updateButton;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        telefonoField.textProperty().addListener(new ChangedVerificationFields(telefonoField,false,10));
        telefonoField.focusedProperty().addListener(new FocusVerificationFields(telefonoField,false,10));
    }
    protected void checkFields() {
        if (nombresField.getText().isEmpty()) {
            nombresField.setStyle("-fx-background-color: #fea08c; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        } else {
            nombresField.setStyle("-fx-background-color: #E3DAD8; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }
        if (telefonoField.getText().isEmpty() || telefonoField.getText().length() < 10) {
            telefonoField.setStyle("-fx-background-color: #fea08c; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        } else {
            telefonoField.setStyle("-fx-background-color: #E3DAD8; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }
    }

    @Override
    public void setInfo(D object) {
        object.setFirstName(nombresField.getText());
        object.setLastName(apellidosField.getText());
        object.setPhone(telefonoField.getText());
    }
}
