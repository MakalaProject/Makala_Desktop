package org.example.controllers.list.controllers;

import com.calendarfx.model.Entry;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.model.*;
import org.example.services.Request;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ActivityPropertiesController implements Initializable {
    @FXML ComboBox<Employee> empleadoComboBox;
    @FXML TextField fechaInicioField;
    @FXML TextField fechaFinField;
    @FXML TextField tiempoField;
    @FXML TextField repeticionesField;
    @FXML TextArea tareaTextArea;
    @FXML FontAwesomeIconView updateButton;
    Entry<CalendarDetailedActivity> actualActivity;
    ObservableList<WorkDays> employeeObservableList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateButton.setOnMouseClicked(mouseEvent -> {
            setInfo(actualActivity);
            Node source = (Node)  mouseEvent.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();
            stage.setUserData(actualActivity);
            stage.close();
        });
    }

    public void setObject(Entry<CalendarDetailedActivity> activity){
        actualActivity = activity;
        putFields();
    }

    private void setInfo(Entry<CalendarDetailedActivity> entry){
        actualActivity.getUserObject().setEmployee(empleadoComboBox.getValue());
        actualActivity.getUserObject().setIdEmployee(empleadoComboBox.getValue().getIdUser());
        actualActivity.setTitle("Actividad de " + empleadoComboBox.getValue().getFirstName());
    }


    private void putFields(){
        employeeObservableList = FXCollections.observableArrayList(Request.getJ("employee-work-days/list-criteria?date="+actualActivity.getStartDate().toString(), WorkDays[].class, false));
        WorkDays workDay = new WorkDays();
        ObservableList<Employee> employees = FXCollections.observableArrayList();
        for(WorkDays workDays : employeeObservableList){
            employees.add(workDays.getEmployee());
            if(workDays.getEmployee().getIdUser() == actualActivity.getUserObject().getIdEmployee()){
                workDay = workDays;
            }
        }
        empleadoComboBox.setItems(employees);
        empleadoComboBox.setValue(workDay.getEmployee());
        fechaInicioField.setText(Formatter.FormatDate(actualActivity.getStartDate()));
        fechaFinField.setText(Formatter.FormatDate(actualActivity.getEndDate()));
        tiempoField.setText(actualActivity.getUserObject().getTime().toString());
        tareaTextArea.setText(actualActivity.getUserObject().getTask());
        repeticionesField.setText(actualActivity.getUserObject().getAmount().toString());
    }


}
