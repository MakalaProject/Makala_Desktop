package org.example.controllers.list.controllers;

import javafx.beans.binding.Bindings;
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
import org.example.interfaces.IControllerCreate;
import org.example.interfaces.IListController;
import org.example.model.Purchase;
import org.example.model.PurchaseProduct;
import org.example.model.products.PackageProduct;
import org.example.model.products.Product;
import org.example.services.Request;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ExpirationProductsController implements Initializable, IListController<PackageProduct> {
    @FXML ListView<PackageProduct> expiredListView;
    @FXML ListView<PackageProduct> urgentListView;
    @FXML ListView<PackageProduct> warningListView;
    @FXML ListView<PackageProduct> stableListView;

    private ObservableList<PackageProduct> expiredObservableList;
    private ObservableList<PackageProduct> urgentObservableList;
    private ObservableList<PackageProduct> warningObservableList;
    private ObservableList<PackageProduct> stableObservableList;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        expiredObservableList = FXCollections.observableList(Request.getJ("packages/filter-list?classification=EXPIRED", PackageProduct[].class,false));
        urgentObservableList = FXCollections.observableList(Request.getJ("packages/filter-list?classification=URGENT", PackageProduct[].class,false));
        warningObservableList = FXCollections.observableList(Request.getJ("packages/filter-list?classification=WARNING", PackageProduct[].class,false));
        stableObservableList = FXCollections.observableList(Request.getJ("packages/filter-list?classification=STABLE", PackageProduct[].class,false));
        expiredListView.getItems().setAll(expiredObservableList);
        expiredListView.prefHeightProperty().bind(Bindings.size(expiredObservableList).multiply(23.7));

        urgentListView.getItems().setAll(urgentObservableList);
        urgentListView.prefHeightProperty().bind(Bindings.size(urgentObservableList).multiply(23.7));

        warningListView.getItems().setAll(warningObservableList);
        warningListView.prefHeightProperty().bind(Bindings.size(warningObservableList).multiply(23.7));

        stableListView.getItems().setAll(stableObservableList);
        stableListView.prefHeightProperty().bind(Bindings.size(stableObservableList).multiply(23.7));

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
    public void loadDialog(PackageProduct purchaseProduct){
        if (purchaseProduct.getProduct().getProductClassDto() == null){
            Product product = (Product) Request.find("products",purchaseProduct.getProduct().getIdProduct(), Product.class);
            purchaseProduct.setProduct(product);
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/expiration_product_info.fxml"));
        try {
            Parent parent = fxmlLoader.load();
            ExpirationProductInfoController dialogController = fxmlLoader.getController();
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

    @Override
    public void delete() {

    }

    @Override
    public void update() {

    }

    @Override
    public boolean existChanges() {
        return false;
    }

    @Override
    public void putFields() {

    }

    @Override
    public void updateView() {

    }

    @Override
    public void cleanForm() {

    }
}
