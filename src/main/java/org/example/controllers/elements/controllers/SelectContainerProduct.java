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
import org.example.customCells.ProductListViewCell;
import org.example.model.Department;
import org.example.model.products.Product;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SelectContainerProduct  implements Initializable {
    @FXML ListView<Product> productListView;
    @FXML TextField searchField;
    private ObservableList<Product> productsList = FXCollections.observableArrayList();
    public void setProductsList(ObservableList<Product> originalProductList, ObservableList<Product> productToRemove){
        for (Product p : originalProductList) {
            for (Product p1 : productToRemove) {
                if (p.getIdProduct().equals(p1.getIdProduct()))
                    break;
                productsList.add(p);
            }
        }
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

        FilteredList<Product> filteredProducts = new FilteredList<>(productsList, p ->true);
        searchField.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredProducts.setPredicate(product -> {
                if (newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseText = newValue.toLowerCase();
                if (product.getName().toLowerCase().contains(lowerCaseText)) {
                    return true;
                } else if (product.getProductClassDto().getProductType().toLowerCase().contains(lowerCaseText)) {
                    return true;
                } else if(product.getProductClassDto().getClassification().toLowerCase().contains(lowerCaseText)){
                    return true;
                }else{
                    return false;
                }
            });
            if (!filteredProducts.isEmpty()) {
                productListView.setItems(filteredProducts);
            }
        } );
    }
}
