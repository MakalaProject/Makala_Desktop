package org.example.controllers.elements.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.model.Provider;
import org.example.model.products.Product;
import org.example.services.Request;

import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

public class SelectListProviders implements Initializable {
    @FXML
    ListView<Provider> productListView;
    @FXML TextField searchField;
    @FXML
    Label titleLabel;
    FilteredList<Provider> providerFilteredList;

    public void setProvider(Provider provider, ObservableList<Provider> providers){
        providers.removeIf(p -> p.getIdUser().equals(provider.getIdUser()));
        productListView.setItems(providers);
        providerFilteredList = new FilteredList<>(providers, p ->true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleLabel.setText("Proveedores");
        productListView.setOnMouseClicked(mouseEvent -> {
            Node source = (Node)  mouseEvent.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();
            stage.setUserData(productListView.getSelectionModel().getSelectedItem());
            stage.close();
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) ->{
            providerFilteredList.setPredicate(provider -> {
                if (newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseText = newValue.toLowerCase();
                if (provider.getFirstName().toLowerCase().contains(lowerCaseText)) {
                    return true;
                }if (provider.getLastName().toLowerCase().contains(lowerCaseText)) {
                    return true;
                } else return provider.getProductClassDto().getProductType().toLowerCase().contains(lowerCaseText);
            });
            if (!providerFilteredList.isEmpty()) {
                productListView.setItems(providerFilteredList);
            }
        } );
    }
}
