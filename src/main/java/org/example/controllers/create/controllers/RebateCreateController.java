package org.example.controllers.create.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.example.controllers.parent.controllers.RebateParentController;

import org.example.model.Rebate;
import org.example.services.Request;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class RebateCreateController extends RebateParentController {
    @FXML
    ComboBox<String> productTypeComboBox;
    private ObservableList<String> items = FXCollections.observableArrayList("Producto","Regalo");
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        productTypeComboBox.getItems().setAll(items);
        productTypeComboBox.getSelectionModel().select(0);
        productTypeComboBox.setOnAction(actionEvent -> {
            objectLabel.setText(productTypeComboBox.getValue());
            identifier = productTypeComboBox.getValue();
        });
        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(LocalDate.now());
        updateButton.setOnMouseClicked(mouseEvent -> {
            Rebate rebate = getInstance();
            verifyClassType(rebate);
            setInfo(rebate);
            Rebate returnedRebate = null;
            try {
                returnedRebate = (Rebate) Request.postJ(rebate.getRoute(),rebate);
                System.out.println(returnedRebate);
            } catch (Exception e) {
                return;
            }
            Node source = (Node)  mouseEvent.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();
            stage.close();
            stage.setUserData(returnedRebate);
        });
    }


    public void verifyClassType(Rebate rebate){
        identifier = productTypeComboBox.getSelectionModel().getSelectedItem();
    }
}
