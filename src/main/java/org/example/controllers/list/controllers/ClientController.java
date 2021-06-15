package org.example.controllers.list.controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.controllers.client.subcontrollers.OrderPropertiesController;
import org.example.controllers.parent.controllers.UserParentController;
import org.example.customCells.InternalListViewCell;
import org.example.customCells.UserListViewCell;
import javafx.scene.control.*;
import org.example.model.*;
import javafx.fxml.FXML;
import org.example.services.Request;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

public class ClientController extends UserParentController<Client> {
    @FXML ListView<Order> historyList;
    @FXML TextField correoField;
    ObservableList<Order> ordersObservableList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateButton.setVisible(false);
        deleteButton.setVisible(false);
        addButton.setVisible(false);
        editSwitch.setVisible(false);
        userObservableList.addAll(Request.getJ("clients", Client[].class, true));
        userObservableList.sort(Comparator.comparing(Client::getFirstName));
        super.initialize(url,resourceBundle);
        addButton.setOnMouseClicked(mouseEvent -> {
            add("/fxml/client_create.fxml", listView, userObservableList, UserListViewCell.class);
        });
        filteredUsers = new FilteredList<>(userObservableList, p ->true);
        searchField.textProperty().addListener((observable, oldValue, newValue) ->{
            if (!existChanges()) {
                filteredUsers.setPredicate(client -> {
                    if (newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseText = newValue.toLowerCase();
                    if (client.getFirstName().toLowerCase().contains(lowerCaseText)) {
                        return true;
                    } else if (client.getLastName().toLowerCase().contains(lowerCaseText)) {
                        return true;
                    } else if (client.getPhone().toLowerCase().contains(lowerCaseText)) {
                        return true;
                    } else if (client.getMail().toLowerCase().contains(lowerCaseText)) {
                        return true;
                    } else {
                        return false;
                    }
                });
                if (!filteredUsers.isEmpty()) {
                    showList();
                }
            }
        } );

        historyList.setOnMouseClicked(mouseEvent -> {
            propertiesOrders(historyList.getSelectionModel().getSelectedItem());
        });

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

        @Override
    protected Client instanceObject() {
        return new Client();
    }


    @Override
    public void putFields() {
        super.putFields();
        ordersObservableList.clear();
        ordersObservableList.addAll(Request.getJ("orders/basic-page-filter?idClient="+ actualUser.getIdUser(), Order[].class, true));
        correoField.setText(actualUser.getMail());
        historyList.setItems(ordersObservableList);
        historyList.setPrefHeight(ordersObservableList.size() * 35 + 2);
        historyList.setCellFactory(cellList -> new InternalListViewCell<>());
    }

    @Override
    public void cleanForm() {
        super.cleanForm();
        correoField.setText("");
        historyList.getItems().clear();
    }

    @Override
    public void setInfo(Client client) {
        super.setInfo(client);
        client.setMail(correoField.getText());
    }

    @Override
    public boolean existChanges(){
        return false;
    }
}
