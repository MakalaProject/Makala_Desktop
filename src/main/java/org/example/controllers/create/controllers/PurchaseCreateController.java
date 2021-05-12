package org.example.controllers.create.controllers;

import javafx.scene.Node;
import javafx.scene.control.DateCell;
import javafx.stage.Stage;
import org.example.controllers.parent.controllers.PurchaseParentController;
import org.example.exceptions.ProductDeleteException;
import org.example.model.Employee;
import org.example.model.Purchase;
import org.example.services.Request;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class PurchaseCreateController extends PurchaseParentController {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        provider = providers.get(0);
        setProviderData();
        orderDatePicker.setValue(LocalDate.now());
        orderDatePicker.setDayCellFactory(d ->
                new DateCell() {
                    @Override public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(item.isBefore(LocalDate.now()));
                    }});
        payDatePicker.setDayCellFactory(d ->
                new DateCell() {
                    @Override public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(item.isBefore(LocalDate.now()));
                    }});
        receivedDatePicker.setDayCellFactory(d ->
                new DateCell() {
                    @Override public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(item.isBefore(LocalDate.now()));
                    }});
        updateButton.setOnMouseClicked(mouseEvent -> {
            if (actualPurchase.getProducts().size() > 0){
                Purchase purchase = new Purchase();
                setInfo(purchase);
                if ((purchase.getPayDate() != null && purchase.getReceivedDate() != null && purchase.getPayDate().compareTo(purchase.getOrderDate()) > -1 && purchase.getReceivedDate().compareTo(purchase.getOrderDate()) > -1) || (purchase.getPayDate() == null && purchase.getReceivedDate() == null) ||(purchase.getPayDate() != null && purchase.getReceivedDate() == null))
                {
                    Purchase returnedPurchase = null;
                    try {
                        returnedPurchase = (Purchase) Request.postJ(purchase.getRoute(),purchase);
                    } catch (Exception e) {
                        return;
                    }
                    Node source = (Node)  mouseEvent.getSource();
                    Stage stage  = (Stage) source.getScene().getWindow();
                    stage.close();
                    stage.setUserData(returnedPurchase);
                }else{
                    showAlertEmptyFields("Verifica las fechas");
                }

            }else {
                showAlertEmptyFields("No puedes dejar la compra sin productos");
            }
        });
    }
}
