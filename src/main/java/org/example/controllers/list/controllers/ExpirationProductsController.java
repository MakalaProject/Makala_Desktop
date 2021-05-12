package org.example.controllers.list.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.controllers.ExpirationProductInfoController;
import org.example.controllers.elements.controllers.SelectContainerProduct;
import org.example.model.Purchase;
import org.example.model.PurchaseProduct;
import org.example.model.products.Product;
import org.example.services.Request;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ExpirationProductsController implements Initializable {
    @FXML ListView<PurchaseProduct> expiredListView;
    @FXML ListView<PurchaseProduct> urgentListView;
    @FXML ListView<PurchaseProduct> warningListView;
    @FXML ListView<PurchaseProduct> stableListView;

    private ObservableList<PurchaseProduct> expiredObservableList;
    private ObservableList<PurchaseProduct> urgentObservableList;
    private ObservableList<PurchaseProduct> warningObservableList;
    private ObservableList<PurchaseProduct> stableObservableList;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        expiredObservableList = FXCollections.observableList(Request.getJ("packages/filter-list?classification=EXPIRED", PurchaseProduct[].class,false));
        urgentObservableList = FXCollections.observableList(Request.getJ("packages/filter-list?classification=URGENT", PurchaseProduct[].class,false));
        warningObservableList = FXCollections.observableList(Request.getJ("packages/filter-list?classification=WARNING", PurchaseProduct[].class,false));
        stableObservableList = FXCollections.observableList(Request.getJ("packages/filter-list?classification=STABLE", PurchaseProduct[].class,false));
        expiredListView.getItems().setAll(expiredObservableList);
        urgentListView.getItems().setAll(urgentObservableList);
        warningListView.getItems().setAll(warningObservableList);
        stableListView.getItems().setAll(stableObservableList);

        urgentListView.setOnMouseClicked(mouseEvent -> {
            loadDialog(urgentListView.getSelectionModel().getSelectedItem());
        });
        expiredListView.setOnMouseClicked(mouseEvent -> {
            loadDialog(expiredListView.getSelectionModel().getSelectedItem());
        });
        warningListView.setOnMouseClicked(mouseEvent -> {
            loadDialog(warningListView.getSelectionModel().getSelectedItem());
        });
        stableListView.setOnMouseClicked(mouseEvent -> {
            loadDialog(stableListView.getSelectionModel().getSelectedItem());
        });
    }
    public void loadDialog(PurchaseProduct purchaseProduct){
        if (purchaseProduct.getProduct().getProductClassDto() == null){
            Product product = (Product) Request.find("products",purchaseProduct.getProduct().getIdProduct(), Product.class);
            purchaseProduct.setProduct(product);
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/expiration_product_info.fxml"));
        try {
            ExpirationProductInfoController dialogController = (ExpirationProductInfoController) fxmlLoader.getController();
            Parent parent = fxmlLoader.load();
            dialogController.setProduct(purchaseProduct);
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
