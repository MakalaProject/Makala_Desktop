package org.example.controllers.elements.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.CheckListView;
import org.example.interfaces.ListToChangeTools;
import org.example.model.Department;
import org.example.model.Employee;
import org.example.services.Request;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SelectListDepart implements Initializable {
    @FXML
    Label titleLabel;
    @FXML
    FontAwesomeIconView saveButton;
    @FXML
    CheckListView<Department> checkListView;
    private Employee employee;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleLabel.setText("Departamentos");
        checkListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        saveButton.setOnMouseClicked(mouseEvent -> {
            List<Department> departmentList = new ArrayList<>(checkListView.getCheckModel().getCheckedItems());
            new ListToChangeTools<Department,Integer>().setToDeleteItems(employee.getDepartments(), departmentList);
            employee.setDepartments(departmentList);
            Node source = (Node)  mouseEvent.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();
            stage.close();
            stage.setUserData(employee);
        });
    }

    public void setEmployee(Employee employee){
        checkListView.setItems(FXCollections.observableArrayList(Request.getJ("departments", Department[].class, false)));
        this.employee = employee;
        if (employee.getDepartments() != null){
            for (Department department : checkListView.getItems()) {
                for (Department departmentE : employee.getDepartments()) {
                    if (department.getIdDepartment().equals(departmentE.getIdDepartment()))
                        checkListView.getCheckModel().check(department);
                }
            }
        }

    }

}
