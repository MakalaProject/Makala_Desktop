package org.example.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.example.interfaces.IControllerCreate;
import org.example.interfaces.IListController;
import org.example.model.Provider;
import org.example.model.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class UserParentController <D extends User> implements Initializable, IListController<D>, IControllerCreate<D> {
    @FXML
    TextField nombresField;
    @FXML TextField apellidosField;
    @FXML TextField telefonoField;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void setInfo(D object) {
        object.setFirstName(nombresField.getText());
        object.setLastName(apellidosField.getText());
        object.setPhone(telefonoField.getText());
    }

    @Override
    public void delete() {

    }

    @Override
    public void update() {

    }

    @Override
    public void add() {

    }

    @Override
    public boolean existChanges() {
        return false;
    }

    @Override
    public void putFields() {


    }

    @Override
    public void updateView() throws IOException {

    }

    @Override
    public void cleanForm() {

    }
}
