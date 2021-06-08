package org.example.controllers.elements.controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.model.DataAnalysisReceived;
import org.example.model.Provider;
import org.example.model.products.Product;
import org.example.services.Request;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ProductionPlanifierInfo implements Initializable {
    @FXML
    private TextField stockField;
    @FXML private TextField nombreField;
    @FXML private TextField minField;
    @FXML private TextField pedidoField;
    @FXML private TextField cantidadOrdenarField;
    @FXML private TextField clasificacionField;
    @FXML private Label unidadField;
    @FXML private Label unidadField1;
    @FXML private Label unidadField2;
    @FXML private ImageView imagen;
    @FXML private ListView<Provider> providerListView;
    ObservableList<Provider> providerObservableList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        providerListView.setOnMouseClicked(mouseEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/provider_filtered_info.fxml"));
            try {
                ProviderInfo dialogController = new ProviderInfo ();
                fxmlLoader.setController(dialogController);
                Parent parent = fxmlLoader.load();
                dialogController.setObject((Provider) Request.find("users/providers", providerListView.getSelectionModel().getSelectedItem().getIdUser(), Provider.class ));
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
            }catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void setObject(Product data){
        stockField.setText(data.getStock().toString());
        minField.setText(data.getMin().toString());
        nombreField.setText(data.toString());
        clasificacionField.setText(data.getProductClassDto().getClassification());
        if(data.getPictures().size() != 0) {
            imagen.setImage(new Image(data.getPictures().get(0).getPath()));
        }else {
            imagen.setImage(new Image("/Images/product.png"));
        }
        if(data.getProductClassDto().getProductType().equals("Listones")){
            unidadField.setText("m");
            unidadField1.setText("m");
            unidadField2.setText("m");
        }
        if (data.getProductClassDto().getProductType().equals("Papeles")){
            unidadField.setText("m2");
            unidadField1.setText("m2");
            unidadField2.setText("m2");
        }

        pedidoField.setText(new BigDecimal(data.getMin()).subtract(data.getStock()).multiply(data.getPrice()).toString());
        cantidadOrdenarField.setText(new BigDecimal(data.getMin()).subtract(data.getStock()).toString());
        providerObservableList.setAll(Request.getJ("users/providers/general/filter-list?idProduct=" + data.getId(), Provider[].class, false));
        providerListView.getItems().setAll(providerObservableList);
        providerListView.prefHeightProperty().bind(Bindings.size(FXCollections.observableList(providerObservableList) ).multiply(23.7));
    }


}
