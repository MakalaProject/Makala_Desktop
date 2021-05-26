package org.example.controllers.list.controllers;

import javafx.beans.binding.Bindings;
import org.example.controllers.parent.controllers.UserParentController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.controllers.elements.controllers.SelectListDepart;
import org.example.customCells.UserListViewCell;
import org.example.model.Department;
import org.example.model.Employee;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import org.example.services.Request;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EmployeeController extends UserParentController<Employee> {
    @FXML FontAwesomeIconView editDepartmentButton;
    @FXML ListView<Department> departmentList;
    @FXML TextField contraseñaField;
    @FXML DatePicker startDatePicker;
    @FXML DatePicker endDatePicker;

    private static final ObservableList<Department> departmentsItems = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userObservableList.addAll(Request.getJ("users/employees", Employee[].class, true));
        super.initialize(url,resourceBundle);
        //Check if the list is empty to update the view and show its values at the beggining
        addButton.setOnMouseClicked(mouseEvent -> {
            if(!existChanges()) {
                add("/fxml/employee_create.fxml", listView, userObservableList, UserListViewCell.class);
            }
        });
        searchField.textProperty().addListener((observable, oldValue, newValue) ->{
            if (!existChanges()) {
                filteredUsers.setPredicate(employee -> {
                    if (newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseText = newValue.toLowerCase();
                    if (employee.getFirstName().toLowerCase().contains(lowerCaseText)) {
                        return true;
                    } else if (employee.getLastName().toLowerCase().contains(lowerCaseText)) {
                        return true;
                    } else if (employee.getPhone().toLowerCase().contains(lowerCaseText)) {
                        return true;
                    } else return employee.isDepartment(lowerCaseText);
                });
                if (!filteredUsers.isEmpty()) {
                    showList(filteredUsers, listView, UserListViewCell.class);
                }
            }
        } );

        editDepartmentButton.setOnMouseClicked(mouseEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/select_list_generic.fxml"));
            try {
                SelectListDepart dialogController = new SelectListDepart();
                fxmlLoader.setController(dialogController);
                Parent parent = fxmlLoader.load();
                Employee sendEmployee = new Employee();
                sendEmployee.setDepartments(new ArrayList<>(departmentsItems));
                dialogController.setEmployee(sendEmployee);
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
                Employee employee = (Employee) stage.getUserData();
                if (employee!= null) {
                    departmentsItems.setAll(employee.getDepartments());
                    showDepartmentsList(departmentsItems);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void update(){
        if(startDatePicker.getValue().compareTo(endDatePicker.getValue()) < 0){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Información invalida");
            alert.setHeaderText("Fechas incorrectas");
            alert.setContentText("Las fechas no són validas");
            alert.showAndWait();
            return;
        }
        super.update();
        actualUser.setSelectedDepartments();
        actualUser.setPassword(null);
        contraseñaField.setText("");
    }

    @Override
    protected Employee instanceObject() {
        return new Employee();
    }

    @Override
    public void putFields() {
       super.putFields();
        departmentsItems.clear();
        contraseñaField.setText("");
        if (actualUser.getDepartments().size() > 0 ) {
            departmentsItems.addAll(actualUser.getDepartments());
            showDepartmentsList(departmentsItems);
        }
    }

    private void showDepartmentsList(ObservableList<Department> list){
        departmentList.setItems(FXCollections.observableList(list.stream().filter(l -> !l.isToDelete()).collect(Collectors.toList())));
        departmentList.prefHeightProperty().bind(Bindings.size(departmentList.getItems()).multiply(23.7));
    }

    @Override
    public void cleanForm() {
        super.cleanForm();
        contraseñaField.setText("");
        departmentList.getItems().clear();
    }

    @Override
    public void setInfo(Employee employee) {
        super.setInfo(employee);
        ArrayList<Department> departments = new ArrayList<>(departmentList.getItems());
        employee.setDepartments(departments);
        employee.setIdUser(actualUser.getIdUser());
        if(!contraseñaField.getText().isEmpty()){
            employee.setPassword(contraseñaField.getText());
        }
    }
}
