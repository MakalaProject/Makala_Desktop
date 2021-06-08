package org.example.controllers.list.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.controllers.elements.controllers.ProductionPlanifierInfo;
import org.example.controllers.elements.controllers.StockTimeInfo;
import org.example.customCells.ProductListViewCell;
import org.example.interfaces.IListController;
import org.example.model.products.Product;
import org.example.services.Request;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class StockTimeController implements Initializable, IListController<Product> {
    @FXML
    private ListView<Product> listView;
    @FXML private TextField searchField;
    FilteredList<Product> filteredProducts;
    private ObservableList<Product> productObservableList = FXCollections.observableArrayList(Request.getJ("products/basics-avg/filter-list",Product[].class,false));
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showList(productObservableList,listView, ProductListViewCell.class);
        listView.setOnMouseClicked(mouseEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/product_stock_time_info.fxml"));
            try {
                Parent parent = fxmlLoader.load();
                StockTimeInfo dialogController = fxmlLoader.getController();
                Product product = (Product) Request.find("products", listView.getSelectionModel().getSelectedItem().getIdProduct(),Product.class);
                product.setAvgDays(listView.getSelectionModel().getSelectedItem().getAvgDays());
                dialogController.setObject(product);
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            };
        });
        filteredProducts = new FilteredList<>(productObservableList, p ->true);
        //Search filter
        searchField.textProperty().addListener((observable, oldValue, newValue) ->{
            if (!existChanges()) {
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
                    showList(FXCollections.observableArrayList(filteredProducts), listView, ProductListViewCell.class);
                }
            }
        } );
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