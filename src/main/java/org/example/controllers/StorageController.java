package org.example.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import org.example.interfaces.IControllerCreate;
import org.example.interfaces.IListController;
import org.example.model.products.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StorageController implements Initializable, IListController<Product>, IControllerCreate<Product>
{
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
    public void editView() {

    }

    @Override
    public void updateView() throws IOException {

    }

    @Override
    public void cleanForm() {

    }

    public void showList(ObservableList List) {

    }

    @Override
    public Product setInfo(Product product) {
        return null;
    }
}
