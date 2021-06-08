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
import org.example.controllers.elements.controllers.ProductionPlanifierInfo;
import org.example.controllers.elements.controllers.StatisticsProductInfo;
import org.example.customCells.ProductListViewCell;
import org.example.customCells.ProductPlanifierListViewCell;
import org.example.interfaces.IListController;
import org.example.model.products.Product;
import org.example.services.Request;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;

public class ProductionPlanifierController implements Initializable, IListController<Product> {
    @FXML private ListView<Product> listView;
    private ObservableList<Product> productAvg = FXCollections.observableArrayList(Request.getJ("products/basics-avg/filter-list?showMin=true&size=1000",Product[].class,false));
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productAvg.sort(Comparator.comparing(Product::isHasGifts).reversed());
        for (Product product: productAvg
             ) {
            if(product.getProductClassDto().getProductType().equals("Listones")){
                product.setStock(product.getStock().divide(new BigDecimal(10)));
                product.setMin(product.getMin()/10);
            }
            if(product.getProductClassDto().getProductType().equals("Papeles")){
                product.setStock(product.getStock().divide(new BigDecimal(100)));
                product.setMin(product.getMin()/100);
            }
        }
        showList(productAvg,listView, ProductPlanifierListViewCell.class);
        listView.setOnMouseClicked(mouseEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/production_planifier_info.fxml"));
            try {
                Parent parent = fxmlLoader.load();
                ProductionPlanifierInfo dialogController = fxmlLoader.getController();
                dialogController.setObject(listView.getSelectionModel().getSelectedItem());
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            };
        });
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
