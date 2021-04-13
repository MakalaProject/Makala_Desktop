package org.example.controllers;

import org.example.customCells.DepartmentListViewCell;
import org.example.customCells.EmployeeListViewCell;
import org.example.customDialogs.EmployeeCreateController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.customDialogs.SelectListDepart;
import org.example.interfaces.IControllerCreate;
import org.example.interfaces.IListController;
import org.example.model.Department;
import org.example.model.Employee;
import org.controlsfx.control.ToggleSwitch;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.example.services.Request;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable, IListController<Employee>, IControllerCreate<Employee> {
    @FXML FontAwesomeIconView deleteButton;
    @FXML FontAwesomeIconView addButton;
    @FXML FontAwesomeIconView saveButton;
    @FXML FontAwesomeIconView editDepartmentButton;
    @FXML ListView<Employee> listView;
    @FXML ListView<Department> departmentList;
    @FXML TextField searchField;
    @FXML TextField nombresField;
    @FXML TextField apellidosField;
    @FXML TextField contraseñaField;
    @FXML TextField telefonoField;
    @FXML ToggleSwitch editSwitch;
    @FXML AnchorPane fieldsAnchorPane;
    @FXML SplitPane principalSplitPane;
    Employee actualEmployee;

    private ObservableList<Employee> employeeObservableList = FXCollections.observableArrayList();
    private static final ObservableList<Department> departmentsItems = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        employeeObservableList.addAll(Request.getJ("users/employees", Employee[].class, true));
        showList(employeeObservableList);

        //Check if the list is empty to update the view and show its values at the beggining
        if(!employeeObservableList.isEmpty()){
            listView.getSelectionModel().select(0);
            updateView();
        }

        FilteredList<Employee> filteredEmployees = new FilteredList<>(FXCollections.observableArrayList(employeeObservableList), p ->true);
        searchField.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredEmployees.setPredicate(employee -> {
                if (newValue.isEmpty()){
                    return true;
                }
                String lowerCaseText = newValue.toLowerCase();
                if (employee.getFirstName().toLowerCase().contains(lowerCaseText)){
                    return true;
                }else if(employee.getLastName().toLowerCase().contains(lowerCaseText)){
                    return true;
                }else if(employee.getPhone().toLowerCase().contains(lowerCaseText)){
                    return true;
                }else if(employee.isDepartment(lowerCaseText)){
                    return true;
                }else {
                    return false;
                }

            });
            SortedList<Employee> sortedEmployees = new SortedList<>(filteredEmployees);
            showList(sortedEmployees);
        } );

        telefonoField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    telefonoField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        editSwitch.setOnMouseClicked(mouseEvent -> {
            //editView();
        });

        listView.setOnMouseClicked(mouseEvent -> {
            if (existChanges()) {
                updateView();
            }
        });

        saveButton.setOnMouseClicked(mouseEvent -> {
            update();
        });

        deleteButton.setOnMouseClicked(mouseEvent -> {
            deleteAlert("empleado");
        });

        addButton.setOnMouseClicked(mouseEvent -> {
           add();
        });

        editDepartmentButton.setOnMouseClicked(mouseEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/select_list_generic.fxml"));
            try {
                Parent parent = fxmlLoader.load();
                SelectListDepart dialogController = fxmlLoader.<SelectListDepart>getController();
                dialogController.setEmployee(actualEmployee);
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
                Employee employee = (Employee) stage.getUserData();
                if (employee!=null) {
                    employeeObservableList.set(employeeObservableList.indexOf(actualEmployee), employee);
                    listView.getSelectionModel().select(employee);
                    listView.scrollTo(employee);
                    updateView();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


    }


    private void showListDepartments(ObservableList<Department> departmentsList){
        departmentList.setItems(departmentsList);
        departmentList.setCellFactory(employeeListView -> new DepartmentListViewCell());
    }

    @Override
    public void delete() {
        Request.deleteJ( actualEmployee.getRoute(), actualEmployee.getId());
        if (listView.getItems().size() > 1) {
            employeeObservableList.remove(actualEmployee);
            listView.getSelectionModel().select(0);
            showList(employeeObservableList);
            updateView();
        }else {
            employeeObservableList.remove(actualEmployee);
            showList(employeeObservableList);
        }

    }

    @Override
    public void update() {
        if(!nombresField.getText().isEmpty() || apellidosField.getText().isEmpty() || telefonoField.getText().isEmpty() ){
            employeeObservableList.set(employeeObservableList.indexOf(actualEmployee), setInfo(actualEmployee));
            showList(employeeObservableList);
            Request.putJ(actualEmployee.getRoute(), actualEmployee);
            editSwitch.setSelected(false);
            //editView();
        }else{
            showAlertEmptyFields("No puedes dejar campos indispensables vacios");
        }

    }

    @Override
    public void add() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/employee_create.fxml"));
        try {
            Parent parent = fxmlLoader.load();
            EmployeeCreateController dialogController = fxmlLoader.<EmployeeCreateController>getController();
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            //stage.setMaxHeight(712);
            //stage.setMaxWidth(940);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
            Employee employee = (Employee) stage.getUserData();
            if(employee != null){
                employeeObservableList.add(employee);
                showList(employeeObservableList);
                listView.getSelectionModel().select(employee);
                listView.scrollTo(employee);
                updateView();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean existChanges() {
        if (actualEmployee == null){
            return true;
        }
        if (actualEmployee.compareEmployee(setInfo(new Employee()))){
            if(!showAlertUnsavedElement(actualEmployee.getFirstName(), "Empleado")) {
                listView.getSelectionModel().select(actualEmployee);
            }else{
                updateView();
            }
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void putFields() {
        nombresField.setText(actualEmployee.getFirstName());
        apellidosField.setText(actualEmployee.getLastName());
        telefonoField.setText(actualEmployee.getPhone());
        departmentsItems.clear();
        contraseñaField.setText("");
        if (actualEmployee.getDepartments() != null) {
            departmentsItems.addAll(actualEmployee.getDepartments());
            showListDepartments(departmentsItems);
        }
    }

    @Override
    public void updateView(){
        editSwitch.setSelected(false);
        //editView();
        actualEmployee = listView.getSelectionModel().getSelectedItem();
        putFields();
    }

    public void showList(ObservableList<Employee> employeeList) {
        if (!employeeList.isEmpty()){
            listView.setDisable(false);
            listView.setItems(employeeList);
            listView.setCellFactory(employeeListView -> new EmployeeListViewCell());
        }else {
            listView.setDisable(true);
            cleanForm();
        }
    }

    @Override
    public void cleanForm() {
        nombresField.setText("");
        apellidosField.setText("");
        telefonoField.setText("");
        contraseñaField.setText("");
        departmentList.getItems().clear();
    }


    @Override
    public Employee setInfo(Employee employee) {
        employee.setFirstName(nombresField.getText());
        employee.setLastName(apellidosField.getText());
        employee.setPhone(telefonoField.getText());
        if(contraseñaField.getText() != null){
            employee.setPassword(contraseñaField.getText());
        }
        return employee;
    }
}
