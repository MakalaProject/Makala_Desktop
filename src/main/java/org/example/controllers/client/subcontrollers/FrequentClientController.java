package org.example.controllers.client.subcontrollers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.model.Client;
import org.example.model.Order;
import org.example.services.Request;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FrequentClientController implements Initializable {
    @FXML ListView<Order> historyList;
    @FXML TextField correoField;
    @FXML protected TextField nombresField;
    @FXML protected TextField apellidosField;
    @FXML protected TextField telefonoField;
    Client actualUser;
    ObservableList<Order> ordersObservableList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        historyList.setOnMouseClicked(mouseEvent -> {
            propertiesOrders(historyList.getSelectionModel().getSelectedItem());
        });
    }

    public void putFields() {
        nombresField.setText(actualUser.getFirstName());
        apellidosField.setText(actualUser.getLastName());
        telefonoField.setText(actualUser.getPhone());
        ordersObservableList.clear();
        ordersObservableList.addAll(Request.getJ("orders/basic-page-filter?idClient="+ actualUser.getIdUser(), Order[].class, true));
        correoField.setText(actualUser.getMail());
        historyList.setItems(ordersObservableList);
        historyList.getStylesheets().add(getClass().getResource("/configurations/style.css").toString());
        historyList.prefHeightProperty().bind(Bindings.size(ordersObservableList).multiply(24));
    }

    public void setClient(Client user){
        this.actualUser =  user;
        putFields();
    }

    private void propertiesOrders(Order order) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/order.fxml"));
        try {
            Parent parent = fxmlLoader.load();
            OrderPropertiesController dialogController = fxmlLoader.getController();
            dialogController.setOrder(order);
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
            Order order1 = (Order) stage.getUserData();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
