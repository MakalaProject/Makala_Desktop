package org.example.controllers.create.controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.example.controllers.parent.controllers.ProductParentController;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.example.model.Picture;
import org.example.services.ImageService;
import org.example.services.Request;
import org.example.interfaces.IControllerProducts;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.example.model.products.Product;
import org.example.model.products.ProductClassDto;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProductCreateController extends ProductParentController {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url,resourceBundle);
        clasificacionComboBox.getSelectionModel().select(0);
        tipoComboBox.getSelectionModel().select(0);
        privacidadComboBox.getSelectionModel().select(0);
        nombreField.setText("Nuevo Producto");
        changeType(propertiesControllers[0]);
        clasificacionComboBox.valueProperty().addListener(new ChangeListener<ProductClassDto>() {
            @Override
            public void changed(ObservableValue<? extends ProductClassDto> observableValue, ProductClassDto productClassDto, ProductClassDto t1) {
                for (IControllerProducts controller : propertiesControllers) {
                    if (Arrays.asList(controller.getIdentifier()).contains(t1.getProductType())){
                        tipoComboBox.setValue(t1.getProductType());
                        actualPropertiesController = controller;
                        changeType(controller);
                    }
                }
            }
        });

        privacidadComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (privacidadComboBox.getValue().equals("Publico") || tipoComboBox.equals("Premium")){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Producto no editable");
                    alert.setContentText("Una vez establecido este producto no podras cambiarlo después");
                    alert.showAndWait();
                }
            }
        });

        updateButton.setOnMouseClicked(mouseEvent -> {
            if( !nombreField.getText().isEmpty()){
                Product product = (Product) actualPropertiesController.getObject();
                if (product != null){
                    setInfo(product);
                    ArrayList<Picture> pictures = new ArrayList<>();
                    files = ImageService.uploadImages(files);
                    if(files != null){
                        for(String s: files){
                            pictures.add(new Picture(s));
                        }
                    }
                    product.setPictures(pictures);
                    Product newProduct = (Product) Request.postJ(product.getRoute(), product);
                    if(newProduct != null) {
                        product.setIdProduct(newProduct.getIdProduct());
                    }
                    Node source = (Node)  mouseEvent.getSource();
                    Stage stage  = (Stage) source.getScene().getWindow();
                    files = new ArrayList<>();
                    deleteFiles = new ArrayList<>();
                    imageIndex = 0;
                    stage.close();
                    stage.setUserData(product);
                }
            }else{
                showAlertEmptyFields("No puedes dejar campos indispensables vacios");
            }
        });
    }
}
