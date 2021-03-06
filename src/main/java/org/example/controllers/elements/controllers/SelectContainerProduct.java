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
import org.example.model.products.Product;
import org.example.model.products.StaticProduct;
import org.example.services.Request;

import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

public class SelectContainerProduct  implements Initializable {
    @FXML ListView<Product> productListView;
    @FXML TextField searchField;
    FilteredList<Product> filteredProducts;
    private ObservableList<Product> productsList = FXCollections.observableArrayList();

    public void setProductsList(ObservableList<Product> originalProductList, ObservableList<Product> productToRemove){
        boolean validation = false;
        if(productToRemove.size() > 0 && productToRemove.get(0) != null) {
            for (Product p : originalProductList) {
                for (Product p1 : productToRemove) {
                    if (p.getIdProduct().equals(p1.getIdProduct())) {
                        validation = true;
                        break;
                    }
                }
                if (!validation) {
                    productsList.add(p);
                }
                validation = false;
            }
        }else{
            productsList = originalProductList;
        }
        Collections.sort(productsList, (d1, d2) -> d1.getName().compareTo(d2.getName()));
        productListView.setItems(productsList);
        filteredProducts = new FilteredList<>(productsList, p ->true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productListView.setOnMouseClicked(mouseEvent -> {
            Node source = (Node)  mouseEvent.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();
            stage.setUserData(Request.find("products", productListView.getSelectionModel().getSelectedItem().getIdProduct(), Product.class));
            stage.close();
        });
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
