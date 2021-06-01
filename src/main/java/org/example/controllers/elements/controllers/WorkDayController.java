package org.example.controllers.elements.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import org.example.exceptions.ProductDeleteException;
import org.example.interfaces.IListController;
import org.example.interfaces.ListToChangeTools;
import org.example.model.Step;
import org.example.model.WorkDays;
import org.example.model.products.Action;
import org.example.services.Request;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class WorkDayController implements Initializable, IListController<WorkDays> {
    @FXML FontAwesomeIconView deleteButton;
    @FXML FontAwesomeIconView updateButton;
    @FXML DatePicker startDatePicker;
    @FXML DatePicker endDatePicker;
    WorkDays workDays;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        deleteButton.setOnMouseClicked(mouseEvent -> {
            delete();
            Node source = (Node)  mouseEvent.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();
            workDays.setAction(Action.DELETE);
            stage.setUserData(workDays);
            stage.close();
        });

        updateButton.setOnMouseClicked(mouseEvent -> {
            if(updateV()) {
                Node source = (Node) mouseEvent.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                if (workDays.getAction() == Action.UPDATE)
                    stage.setUserData(workDays);
                else {
                    workDays.setAction(Action.UPDATE);
                    stage.setUserData(workDays);
                }
                stage.close();
            }
        });
    }

    @Override
    public void delete() {
        try {
            Request.deleteJ("employee-work-days", workDays.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {

    }

    public boolean updateV() {
        if(startDatePicker.getValue().compareTo(endDatePicker.getValue()) > 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Información invalida");
            alert.setHeaderText("Fechas invalidas");
            alert.setContentText("Por favor verifica las fechas");
            alert.showAndWait();
            return false;
        }
        setInfo(workDays);
        if(workDays.getAction() == Action.UPDATE){
            try {
                Request.putJ("employee-work-days", workDays);
            } catch (ProductDeleteException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Información invalida");
                alert.setHeaderText("Intervalo ya registrado");
                alert.setContentText("El intervalo que estas tratando de ingresar, ya está registrado, ingrese uno nuevo");
                alert.showAndWait();
                return false;
            }
        }else{
            try {
                Request.postk("employee-work-days", workDays, WorkDays.class);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Información invalida");
                alert.setHeaderText("Intervalo ya registrado");
                alert.setContentText("El intervalo que estas tratando de ingresar, ya está registrado, ingrese uno nuevo");
                alert.showAndWait();
                return false;
            }
        }
        return true;
    }

    public void setInfo(WorkDays workDays){
        workDays.setStartDate(startDatePicker.getValue());
        workDays.setFinishDate(endDatePicker.getValue());
    }

    @Override
    public boolean existChanges() {
        return false;
    }

    @Override
    public void putFields() {
        startDatePicker.setValue(workDays.getStartDate());
        endDatePicker.setValue(workDays.getFinishDate());
    }

    @Override
    public void updateView() {

    }

    @Override
    public void cleanForm() {

    }

    public void setworkDay(WorkDays day){
        workDays = day;
        if(day.getAction() == Action.UPDATE) {
            deleteButton.setVisible(true);
            putFields();
        }else{
            deleteButton.setVisible(false);
        }

    }
}
