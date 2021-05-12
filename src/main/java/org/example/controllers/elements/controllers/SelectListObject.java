package org.example.controllers.elements.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.interfaces.IChangeable;
import org.example.model.Gift;
import org.example.model.GiftRebate;
import org.example.model.products.Product;

import java.net.URL;
import java.util.ResourceBundle;

public class SelectListObject implements Initializable {
    @FXML
    ListView<IChangeable> productListView;
    @FXML
    TextField searchField;
    FilteredList<IChangeable> filteredProducts;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productListView.setOnMouseClicked(mouseEvent -> {
            Node source = (Node)  mouseEvent.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();
            stage.setUserData(productListView.getSelectionModel().getSelectedItem());
            stage.close();
        });
        searchField.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredProducts.setPredicate(object -> {
                if (newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseText = newValue.toLowerCase();
                if (object.getClass() == Product.class && ((Product)object).getName().toLowerCase().contains(lowerCaseText)) {
                    return true;
                }else return object.getClass() == Gift.class && ((Gift) object).getName().toLowerCase().contains(lowerCaseText);
            });
            if (!filteredProducts.isEmpty()) {
                productListView.setItems(filteredProducts);
            }
        } );
    }
    public void setObject(ObservableList<IChangeable> items) {
        productListView.setItems(items);
        filteredProducts = new FilteredList<>(items, p ->true);
    }
}
