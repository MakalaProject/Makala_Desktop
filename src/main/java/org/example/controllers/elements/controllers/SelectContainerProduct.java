package org.example.controllers.elements.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.example.model.products.Product;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SelectContainerProduct  implements Initializable {
@FXML ListView<Product> productListView;
    public void setProductsList(ObservableList<Product> productsList){
        productListView.setItems(productsList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productListView.setOnMouseClicked(mouseEvent -> {
            Node source = (Node)  mouseEvent.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();
            stage.setUserData(productListView.getSelectionModel().getSelectedItem());
            stage.close();
        });
    }
}
