package org.example.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.interfaces.IControllerCreate;
import org.example.model.ChangedVerificationFields;
import org.example.model.Employee;
import org.example.services.Request;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminInfo implements Initializable, IControllerCreate<Employee> {
    @FXML
    TextField contraseñaField;
    @FXML private TextField nombresField;
    @FXML private TextField apellidosField;
    @FXML private TextField telefonoField;
    @FXML private FontAwesomeIconView updateButton;
    Employee employee = new Employee();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        telefonoField.textProperty().addListener(new ChangedVerificationFields(telefonoField,false,10));

        updateButton.setOnMouseClicked(mouseEvent -> {

            if(!nombresField.getText().isEmpty() || !apellidosField.getText().isEmpty() || !telefonoField.getText().isEmpty()){
                if (telefonoField.getText().length()>9) {
                    setInfo(employee);
                    Employee returnedEmployee = null;
                    try {
                        returnedEmployee = (Employee) Request.putJ(employee.getRoute(), employee);
                    } catch (Exception e) {
                        duplyElementAlert(employee.getIdentifier());
                        return;
                    }
                    Node source = (Node) mouseEvent.getSource();
                    Stage stage = (Stage) source.getScene().getWindow();
                    stage.close();
                    stage.setUserData(returnedEmployee);
                }else{
                    showAlertEmptyFields("Los dígitos del telefono debn ser 10");
                }
            }else{
                showAlertEmptyFields("No puedes dejar campos marcados con * vacios");
            }
        });
    }

    protected void checkFields(){
        if (nombresField.getText().isEmpty()) {
            nombresField.setStyle("-fx-background-color: #fea08c; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        } else {
            nombresField.setStyle("-fx-background-color: #E3DAD8; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }
        if (apellidosField.getText().isEmpty()) {
            apellidosField.setStyle("-fx-background-color: #fea08c; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        } else {
            apellidosField.setStyle("-fx-background-color: #E3DAD8; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }
        if (telefonoField.getText().isEmpty() || telefonoField.getText().length()<10) {
            telefonoField.setStyle("-fx-background-color: #fea08c; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        } else {
            telefonoField.setStyle("-fx-background-color: #E3DAD8; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }
    }
    public void setEmployee(Employee object) {
        employee = object;
        nombresField.setText(employee.getFirstName());
        apellidosField.setText(employee.getLastName());
        telefonoField.setText(employee.getPhone());
    }
    public void setInfo(Employee object) {
        object.setIdUser(employee.getIdUser());
        object.setFirstName(nombresField.getText());
        object.setLastName(apellidosField.getText());
        object.setPhone(telefonoField.getText());
        if (!contraseñaField.getText().isEmpty()){
            object.setPassword(contraseñaField.getText());
        }
    }
}
