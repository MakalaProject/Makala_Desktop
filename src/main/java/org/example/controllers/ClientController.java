package org.example.controllers;

import org.example.customCells.ClientListViewCell;
import org.example.customDialogs.ClientCreateController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.interfaces.IControllerCreate;
import org.example.interfaces.IListController;
import org.example.model.Client;
import org.example.model.Department;
import org.controlsfx.control.ToggleSwitch;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.example.services.Request;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable, IListController<Client>, IControllerCreate<Client> {
    @FXML FontAwesomeIconView updateButton;
    @FXML FontAwesomeIconView deleteButton;
    @FXML FontAwesomeIconView addButton;
    @FXML ListView<Client> listView;
    @FXML ListView<Department> historyList;
    @FXML TextField searchField;
    @FXML TextField nombresField;
    @FXML TextField apellidosField;
    @FXML TextField telefonoField;
    @FXML TextField correoField;
    @FXML AnchorPane fieldsAnchorPane;
    @FXML SplitPane principalSplitPane;
    @FXML ToggleSwitch editSwitch;
    private Client actualClient;

    private final ObservableList<Client> clientObservableList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clientObservableList.addAll(Request.getJ("clients", Client[].class, true));

        showList(clientObservableList);
        if(!listView.getItems().isEmpty()){
            listView.getSelectionModel().select(0);
            updateView();
        }

        FilteredList<Client> filteredClients = new FilteredList<>(FXCollections.observableArrayList(clientObservableList), p ->true);
        searchField.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredClients.setPredicate(client -> {
                if (newValue.isEmpty()){
                    return true;
                }
                String lowerCaseText = newValue.toLowerCase();
                if (client.getFirstName().toLowerCase().contains(lowerCaseText)){
                    return true;
                }else if(client.getLastName().toLowerCase().contains(lowerCaseText)){
                    return true;
                }else if(client.getPhone().toLowerCase().contains(lowerCaseText)){
                    return true;
                }else if(client.getMail().toLowerCase().contains(lowerCaseText)){
                    return true;
                }else {
                    return false;
                }

            });
            SortedList<Client> sortedEmployees = new SortedList<>(filteredClients);
            showList(FXCollections.observableList(sortedEmployees));
        } );

        telefonoField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    telefonoField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        listView.setOnMouseClicked(mouseEvent -> {
            if (existChanges()) {
                updateView();
            }
        });

        updateButton.setOnMouseClicked(mouseEvent -> {
            if (actualClient != null)
                update();
        });

        editSwitch.setOnMouseClicked(mouseEvent -> {
            //editView();
        });

        deleteButton.setOnMouseClicked(mouseEvent -> {
            deleteAlert("cliente");
        });

        addButton.setOnMouseClicked(mouseEvent -> {
            add();
        });
    }




    @Override
    public void delete() {
        Request.deleteJ( actualClient.getRoute(), actualClient.getId());
        if (listView.getItems().size() > 1) {
            clientObservableList.remove(actualClient);
            listView.getSelectionModel().select(0);
            showList(clientObservableList);
            updateView();
        }else {
            clientObservableList.remove(actualClient);
            showList(clientObservableList);
        }
    }

    @Override
    public void update() {
        if (!nombresField.getText().isEmpty() ){
            clientObservableList.set(clientObservableList.indexOf(actualClient), setInfo(actualClient));
            showList(clientObservableList);
            Request.putJ(actualClient.getRoute(), actualClient);
            editSwitch.setSelected(false);
            //editView();
        }else{
            showAlertEmptyFields("No puedes dejar campos indispensables vacios");
        }
    }

    @Override
    public void add() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client_create.fxml"));
        try {
            Parent parent = fxmlLoader.load();
            ClientCreateController dialogController = fxmlLoader.<ClientCreateController>getController();
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            //stage.setMaxHeight(712);
            //stage.setMaxWidth(940);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
            Client client = (Client) stage.getUserData();
            if(client != null){
                clientObservableList.add(client);
                showList(clientObservableList);
                listView.getSelectionModel().select(client);
                listView.scrollTo(client);
                updateView();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean existChanges() {
        if(actualClient ==null){
            return true;
        }
        if (!actualClient.compareClient(setInfo(new Client()))){
            if(!showAlertUnsavedElement(actualClient.getFirstName(), "Cliente")) {
                listView.getSelectionModel().select(actualClient);
            }else{
                updateView();
            }
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void putFields() {
        nombresField.setText(actualClient.getFirstName());
        apellidosField.setText(actualClient.getLastName());
        telefonoField.setText(actualClient.getPhone());
        correoField.setText(actualClient.getMail());
    }

    @Override
    public void updateView(){
        editSwitch.setSelected(false);
        //editView();
        actualClient = listView.getSelectionModel().getSelectedItem();
        putFields();
    }


    public void showList(ObservableList<Client> clientsList) {
        if (!clientsList.isEmpty()){
            listView.setDisable(false);
            listView.setItems(clientsList);
            listView.setCellFactory(employeeListView -> new ClientListViewCell());
        }else {
            listView.setDisable(true);
            cleanForm();
        }
    }

    @Override
    public void cleanForm() {
        nombresField.setText("");
        apellidosField.setText("");
        telefonoField.setText("");
        correoField.setText("");
    }

    @Override
    public Client setInfo(Client client) {
        client.setFirstName(nombresField.getText());
        client.setLastName(apellidosField.getText());
        client.setPhone(telefonoField.getText());
        client.setMail(correoField.getText());
        return client;
    }
}
