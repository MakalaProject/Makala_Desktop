package org.example.controllers.list.controllers;

import org.example.controllers.parent.controllers.UserParentController;
import org.example.customCells.UserListViewCell;
import javafx.scene.control.*;
import org.example.model.Client;
import org.example.model.Department;
import javafx.fxml.FXML;
import org.example.services.Request;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientController extends UserParentController<Client> {
    @FXML ListView<Department> historyList;
    @FXML TextField correoField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateButton.setVisible(false);
        deleteButton.setVisible(false);
        addButton.setVisible(false);
        editSwitch.setVisible(false);
        userObservableList.addAll(Request.getJ("clients", Client[].class, true));
        super.initialize(url,resourceBundle);
        addButton.setOnMouseClicked(mouseEvent -> {
            add("/fxml/client_create.fxml", listView, userObservableList, UserListViewCell.class);
        });
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
    }

    @Override
    protected Client instanceObject() {
        return new Client();
    }


    @Override
    public void putFields() {
        super.putFields();
        correoField.setText(actualUser.getMail());
        //history
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
