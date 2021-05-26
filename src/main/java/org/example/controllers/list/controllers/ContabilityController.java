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
import javafx.stage.Modality;
import javafx.stage.Stage;
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
    @FXML DatePicker datePicker;
    @FXML ComboBox<String> timeFilterCB;
    @FXML Button calcularButton;
    @FXML ListView<Accounting> contabilityListView;


    private static final ObservableList<String> comboBoxItems = FXCollections.observableArrayList("Mes", "Año");
    private static final ObservableList<Accounting> accountingObservableList = FXCollections.observableArrayList(Request.getJ("data-analysis/accounting?criteria=2021-06-26", Accounting[].class, false));
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        timeFilterCB.getItems().addAll(comboBoxItems);
        timeFilterCB.setValue("Mes");

        calcularButton.setOnMouseClicked(mouseEvent -> {
            if(timeFilterCB.getValue().equals("Mes")){
                Accounting accounting = (Accounting) Request.find("data-analysis/accounting/find-one?criteria="+ datePicker.getValue().toString(),Accounting.class);
                contabilityListView.setItems(FXCollections.observableArrayList());
                contabilityListView.prefHeightProperty().bind(Bindings.size(accountingObservableList).multiply(23.7));
                loadObject(accounting);
            }else{
                contabilityListView.setItems(accountingObservableList);
                contabilityListView.prefHeightProperty().bind(Bindings.size(accountingObservableList).multiply(23.7));
            }
        });

        contabilityListView.setOnMouseClicked(mouseEvent -> {
            loadObject(contabilityListView.getSelectionModel().getSelectedItem());
        });

        datePicker.setDayCellFactory(d ->
                new DateCell() {
                    @Override public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                    }});
    }

    public void loadObject(Accounting accounting){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/contability_info .fxml"));
        try {
            ContabilityDetails loadDialog = new ContabilityDetails();
            fxmlLoader.setController(loadDialog);
            Parent parent = fxmlLoader.load();
            loadDialog.setObject(accounting);
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
