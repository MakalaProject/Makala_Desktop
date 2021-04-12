package org.example.customDialogs;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import org.controlsfx.control.CheckListView;
import org.example.interfaces.ListToChangeTools;
import org.example.model.Department;
import org.example.model.Employee;
import org.example.model.products.Hole;
import org.example.services.Request;

import java.net.URL;
import java.util.ResourceBundle;

public class SelectListDepart implements Initializable {
    @FXML
    ListView<Department> listView;
    @FXML
    FontAwesomeIconView saveButton;
    @FXML
    CheckListView<Department> checkListView;
    private Employee employee;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        checkListView.setItems(FXCollections.observableArrayList(Request.getJ("departments", Department[].class, false)));
        checkListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


        saveButton.setOnMouseClicked(mouseEvent -> {
            //new ListToChangeTools<Hole,Integer>().setToDeleteItems(employee.getDepartments(), checkListView.getCheckModel().getCheckedItems());
            employee.setDepartments(checkListView.getCheckModel().getCheckedItems());
            Request.putJ(employee.getRoute(), employee);
            Node source = (Node)  mouseEvent.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();
            stage.close();
            stage.setUserData(employee);
        });

    }

    public void setEmployee(Employee employee){
        this.employee = employee;
        for (Department department : checkListView.getItems()) {
            for (Department departmentE : employee.getDepartments()) {
                if (department.getIdDepartment() == departmentE.getIdDepartment())
                    checkListView.getCheckModel().check(department);
            }
        }
    }

}
