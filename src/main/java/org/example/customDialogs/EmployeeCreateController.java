package org.example.customDialogs;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.interfaces.IControllerCreate;
import org.example.model.Department;
import org.example.model.Employee;
import org.example.services.Request;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeCreateController implements Initializable, IControllerCreate<Employee> {
    @FXML FontAwesomeIconView saveButton;
    @FXML ListView<Department> departmentList;
    @FXML TextField nombresField;
    @FXML TextField apellidosField;
    @FXML TextField contraseñaField;
    @FXML TextField telefonoField;
    private static final ObservableList<Department> departmentsItems = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        telefonoField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    telefonoField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        saveButton.setOnMouseClicked(mouseEvent -> {
            if(!nombresField.getText().isEmpty() || !apellidosField.getText().isEmpty() || !telefonoField.getText().isEmpty() || !contraseñaField.getText().isEmpty()){
                Employee returnedEmployee = (Employee) Request.postJ("employees",setInfo(new Employee()));
                Node source = (Node)  mouseEvent.getSource();
                Stage stage  = (Stage) source.getScene().getWindow();
                stage.close();
                stage.setUserData(returnedEmployee);
            }else{
                showAlertEmptyFields();
            }
        });
    }

    @Override
    public Employee setInfo(Employee employee) {
        employee.setFirstName(nombresField.getText());
        employee.setLastName(apellidosField.getText());
        employee.setPhone(telefonoField.getText());
        employee.setPassword(contraseñaField.getText());
        return employee;
    }
}
