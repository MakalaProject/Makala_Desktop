package org.example.controllers.create.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DateCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.controllers.parent.controllers.PurchaseParentController;
import org.example.exceptions.ProductDeleteException;
import org.example.model.Employee;
import org.example.model.Purchase;
import org.example.services.Request;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class PurchaseCreateController extends PurchaseParentController {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        editProduct = true;
        super.initialize(url, resourceBundle);
        provider = providers.get(0);
        setProviderData();
        orderDatePicker.setValue(LocalDate.now());
        receivedDatePicker.setDayCellFactory(d ->
                new DateCell() {
                    @Override public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(item.isAfter(LocalDate.now()));
                    }});
        updateButton.setOnMouseClicked(mouseEvent -> {
            checkFields();
            if (!productListView.getItems().isEmpty() && !priceField.getText().isEmpty()){
                if (Float.parseFloat(priceField.getText()) > 0) {
                    setInfo(purchase);

                    if ((purchase.getPayDate() != null && purchase.getReceivedDate() != null) || (purchase.getPayDate() == null && purchase.getReceivedDate() == null) || (purchase.getPayDate() != null && purchase.getReceivedDate() == null)) {
                        if (purchase.getReceivedDate() != null){
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirmar compra");
                            alert.setHeaderText("Se confirmar?? la compra y se agregaran los productos al almacen");
                            alert.setContentText("??Seguro quieres confirmar?");
                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.get() == ButtonType.OK){
                                purchase.setEmployee(actualEmployee);
                                createPurchase(mouseEvent);
                            }else {
                                purchase.setReceivedDate(null);
                                receivedDatePicker.setValue(null);
                            }
                        }else {
                            createPurchase(mouseEvent);
                        }

                    } else {
                        showAlertEmptyFields("No puedes dejar la fecha de pago vacia si ya estableciste fecha de entrega");
                    }
                }else {
                    showAlertEmptyFields("El precio de la compra no puede ser 0");
                }
            }else {
                showAlertEmptyFields("No puedes dejar la compra sin productos o sin precio");
            }
        });
    }


    private void createPurchase(MouseEvent mouseEvent){
        Purchase returnedPurchase = null;
        try {
            returnedPurchase = (Purchase) Request.postJ(purchase.getRoute(), purchase);
        } catch (Exception e) {
            return;
        }
        Node source = (Node) mouseEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
        stage.setUserData(returnedPurchase);
    }
}
