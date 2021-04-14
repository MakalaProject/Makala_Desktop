package org.example.customDialogs;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.interfaces.IControllerCreate;
import org.example.model.Department;
import org.example.model.Employee;
import org.example.model.ChangedVerificationFields;
import org.example.services.Request;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeCreateController implements Initializable, IControllerCreate<Employee> {
    @FXML FontAwesomeIconView saveButton;
    @FXML FontAwesomeIconView editDepartmentButton;
    @FXML ListView<Department> departmentList;
    @FXML TextField nombresField;
    @FXML TextField apellidosField;
    @FXML TextField contraseñaField;
    @FXML TextField telefonoField;
    Employee employee = new Employee();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        telefonoField.textProperty().addListener(new ChangedVerificationFields(telefonoField,false,10));

        saveButton.setOnMouseClicked(mouseEvent -> {
            if(!nombresField.getText().isEmpty() || !apellidosField.getText().isEmpty() || !telefonoField.getText().isEmpty() || !contraseñaField.getText().isEmpty()){
                setInfo(employee);
                Employee returnedEmployee = (Employee) Request.postJ(employee.getRoute(),employee);
                Node source = (Node)  mouseEvent.getSource();
                Stage stage  = (Stage) source.getScene().getWindow();
                stage.close();
                stage.setUserData(returnedEmployee);
            }else{
                showAlertEmptyFields("No puedes dejar campos indispensables vacios");
            }
        });

        editDepartmentButton.setOnMouseClicked(mouseEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/select_list_generic.fxml"));
            try {
                Parent parent = fxmlLoader.load();
                SelectListDepart dialogController = fxmlLoader.<SelectListDepart>getController();
                dialogController.setEmployee(employee);
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
                Employee employeeDepartmentsInstance = (Employee) stage.getUserData();
                if (employee!=null) {
                    employee.setDepartments(employeeDepartmentsInstance.getDepartments());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void setInfo(Employee employee) {
        employee.setFirstName(nombresField.getText());
        employee.setLastName(apellidosField.getText());
        employee.setPhone(telefonoField.getText());
        employee.setPassword(contraseñaField.getText());
    }
}
