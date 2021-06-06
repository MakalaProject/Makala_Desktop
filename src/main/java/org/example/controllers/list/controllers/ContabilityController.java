package org.example.controllers.list.controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.controllers.ContabilityListController;
import org.example.controllers.ExpirationProductInfoController;
import org.example.controllers.elements.controllers.ContabilityDetails;
import org.example.interfaces.IListController;
import org.example.model.Accounting;
import org.example.model.Rebate;
import org.example.model.products.Product;
import org.example.services.Request;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ContabilityController implements Initializable, IListController<Accounting> {
    @FXML DatePicker startDatePicker;
    @FXML DatePicker endDatePicker;
    @FXML ComboBox<String> timeFilterCB;
    @FXML AnchorPane propertiesAnchorPane;
    @FXML Button calcularButton;

    @FXML Label startLabel;
    @FXML Label endLabel;
    Parent root = null;

    private static final ObservableList<String> comboBoxItems = FXCollections.observableArrayList("Mes", "Año","Intervalo");
    private static final ObservableList<Accounting> accountingObservableList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        timeFilterCB.getItems().addAll(comboBoxItems);
        timeFilterCB.setValue("Mes");
        startLabel.setVisible(false);
        endLabel.setVisible(false);
        endDatePicker.setVisible(false);
        timeFilterCB.setOnAction(actionEvent -> {
            if (timeFilterCB.getValue().equals("Intervalo")){
                startLabel.setVisible(true);
                endLabel.setVisible(true);
                endDatePicker.setDisable(true);
            }else {
                startLabel.setVisible(false);
                endLabel.setVisible(false);
                endDatePicker.setVisible(false);
            }
        });

        calcularButton.setOnMouseClicked(mouseEvent -> {
            if(timeFilterCB.getValue().equals("Mes")){
                Accounting accounting = (Accounting) Request.find("data-analysis/accounting/find-one?criteria="+ startDatePicker.getValue().toString(),Accounting.class);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/contability_info .fxml"));
                try {
                    root = loader.load();
                    ContabilityDetails controller = loader.getController();
                    controller.setObject(accounting);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                ObservableList<Accounting> accountingObservableList = FXCollections.observableArrayList(Request.getJ("data-analysis/accounting?criteria=" + startDatePicker.getValue(), Accounting[].class, false));
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/contability_list_properties.fxml"));
                try {
                    root = loader.load();
                    ContabilityListController controller = loader.getController();
                    controller.setData(accountingObservableList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            AnchorPane.setTopAnchor(root, 0D);
            AnchorPane.setBottomAnchor(root, 0D);
            AnchorPane.setRightAnchor(root, 0D);
            AnchorPane.setLeftAnchor(root, 0D);
            propertiesAnchorPane.getChildren().setAll(root);
        });


        startDatePicker.setDayCellFactory(d ->
                new DateCell() {
                    @Override public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                    }});
    }

    @Override
    public void delete() {

    }

    @Override
    public void update() {

    }

    @Override
    public boolean existChanges() {
        return false;
    }

    @Override
    public void putFields() {

    }

    @Override
    public void updateView() {

    }

    @Override
    public void cleanForm() {

    }
}
