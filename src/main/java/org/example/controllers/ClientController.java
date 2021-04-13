package org.example.controllers;

import org.example.customCells.ClientListViewCell;
import org.example.customCells.EmployeeListViewCell;
import org.example.customDialogs.ClientCreateController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.model.Client;
import org.example.model.Department;
import org.controlsfx.control.ToggleSwitch;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import org.example.model.RegexVerificationFields;
import org.example.services.Request;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class ClientController extends UserParentController<Client> {
    @FXML ListView<Department> historyList;
    @FXML TextField correoField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userObservableList.addAll(Request.getJ("clients", Client[].class, true));
        super.initialize(url,resourceBundle);
        addButton.setOnMouseClicked(mouseEvent -> {
            add("/fxml/client_create.fxml", listView, userObservableList, ClientListViewCell.class);
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
    protected void showList() {
        showList(userObservableList, listView, EmployeeListViewCell.class);
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
}
