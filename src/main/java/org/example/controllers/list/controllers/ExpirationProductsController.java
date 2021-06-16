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
import org.example.customCells.InternalListViewCell;
import org.example.interfaces.IControllerCreate;
import org.example.interfaces.IListController;
import org.example.model.Purchase;
import org.example.model.PurchaseProduct;
import org.example.model.products.PackageProduct;
import org.example.model.products.Product;
import org.example.services.Request;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class ExpirationProductsController implements Initializable, IListController<PackageProduct> {
    @FXML ListView<PackageProduct> expiredListView;
    @FXML ListView<PackageProduct> urgentListView;
    @FXML ListView<PackageProduct> warningListView;
    @FXML ListView<PackageProduct> stableListView;

    private ObservableList<PackageProduct> expiredObservableList;
    private ObservableList<PackageProduct> urgentObservableList;
    private ObservableList<PackageProduct> warningObservableList;
    private ObservableList<PackageProduct> stableObservableList;
    private ObservableList<PackageProduct> allObservableList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allObservableList = FXCollections.observableList(Request.getJ("packages/filter-list?classification=ALL", PackageProduct[].class,false));
        expiredObservableList = FXCollections.observableList(Request.getJ("packages/filter-list?classification=EXPIRED", PackageProduct[].class,false));
        urgentObservableList = FXCollections.observableList(Request.getJ("packages/filter-list?classification=URGENT", PackageProduct[].class,false));
        warningObservableList = FXCollections.observableList(Request.getJ("packages/filter-list?classification=WARNING", PackageProduct[].class,false));
        stableObservableList = FXCollections.observableList(Request.getJ("packages/filter-list?classification=STABLE", PackageProduct[].class,false));

        LocalDate date = LocalDate.now();
        for(PackageProduct p : allObservableList) {
            if(p.getExpiryDate().compareTo(date) < 0){
                expiredObservableList.add(p);
            }
            else if(p.getExpiryDate().compareTo(date) >= 0 && p.getExpiryDate().compareTo(date.plusWeeks(2)) <= 0){
                urgentObservableList.add(p);
            }
            else if(p.getExpiryDate().compareTo(date.plusWeeks(2)) > 0){
                warningObservableList.add(p);
            }
            else{
                stableObservableList.add(p);
            }
        }
        
        expiredListView.getItems().setAll(expiredObservableList);
        expiredListView.setPrefHeight(expiredObservableList.size() * 35 + 2);
        expiredListView.setCellFactory(listCell -> new InternalListViewCell<>());

        urgentListView.getItems().setAll(urgentObservableList);
        urgentListView.setPrefHeight(urgentObservableList.size() * 35 + 2);
        urgentListView.setCellFactory(listCell -> new InternalListViewCell<>());

        warningListView.getItems().setAll(warningObservableList);
        warningListView.setPrefHeight(warningObservableList.size() * 35 + 2);
        warningListView.setCellFactory(listCell -> new InternalListViewCell<>());

        stableListView.getItems().setAll(stableObservableList);
        stableListView.setPrefHeight(stableObservableList.size() * 35 + 2);
        stableListView.setCellFactory(listCell -> new InternalListViewCell<>());

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
