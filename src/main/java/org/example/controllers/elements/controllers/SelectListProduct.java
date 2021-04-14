package org.example.controllers.elements.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.CheckListView;
import org.example.interfaces.ListToChangeTools;
import org.example.model.Department;
import org.example.model.Provider;
import org.example.model.products.Product;
import org.example.services.Request;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SelectListProduct implements Initializable {
    @FXML
    TextField titleLabel;
    @FXML
    FontAwesomeIconView saveButton;
    @FXML
    CheckListView<Product> checkListView;
    private Provider provider;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleLabel.setText("Productos");
        checkListView.setItems(FXCollections.observableArrayList(Request.getJ("products/basics/filter-list?idClass="+provider.getProductClassDto(), Product[].class, false)));
        checkListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        saveButton.setOnMouseClicked(mouseEvent -> {
            List<Product> productList = checkListView.getCheckModel().getCheckedItems();
            new ListToChangeTools<Product,Integer>().setToDeleteItems(provider.getProducts(), productList);
            provider.setProducts(productList);
            Node source = (Node)  mouseEvent.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();
            stage.close();
            stage.setUserData(provider);
        });
    }

    public void setProvider(Provider provider){
        this.provider = provider;
        if (provider.getProducts() != null){
            for (Product product : checkListView.getItems()) {
                for (Product product1 : provider.getProducts()) {
                    if (product.getIdProduct() == product1.getIdProduct())
                        checkListView.getCheckModel().check(product);
                }
            }
        }

    }
}
