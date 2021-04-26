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
        actualPropertiesController = propertiesControllers[0];
        changeType(actualPropertiesController);
        clasificacionComboBox.valueProperty().addListener(new ChangeListener<ProductClassDto>() {
            @Override
            public void changed(ObservableValue<? extends ProductClassDto> observableValue, ProductClassDto productClassDto, ProductClassDto t1) {
                for (IControllerProducts controller : propertiesControllers) {
                    if (Arrays.asList(controller.getIdentifier()).contains(t1.getProductType()) || Arrays.asList(controller.getIdentifier()).contains(t1.getClassification())){
                        tipoComboBox.setValue(t1.getProductType());
                        actualPropertiesController = controller;
                        changeType(controller);
                    }
                }
            }
        });

        updateButton.setOnMouseClicked(mouseEvent -> {
            if( !nombreField.getText().isEmpty()){
                Product product = (Product) actualPropertiesController.getObject();
                if (product != null){
                    setInfo(product);
                    Product newProduct = null;
                    try {
                        product.setPictures(new ArrayList<>());
                        newProduct = (Product) Request.postJ(product.getRoute(), product);
                        product.setIdProduct(newProduct.getIdProduct());
                        newProduct = product;
                        ArrayList<Picture> pictures = new ArrayList<>();
                        files = ImageService.uploadImages(files);
                        if(files != null){
                            for(String s: files){
                                pictures.add(new Picture(s));
                            }
                        }
                        newProduct.setPictures(new ArrayList<>());
                        newProduct.setPictures(pictures);
                        product = (Product)Request.putJ(product.getRoute(), product);
                        newProduct.setPictures(product.getPictures());
                    } catch (Exception e) {
                        ImageService.deleteImages(files);
                        duplyElementAlert(product.getIdentifier());
                        return;
                    }
                    Node source = (Node)  mouseEvent.getSource();
                    Stage stage  = (Stage) source.getScene().getWindow();
                    files = new ArrayList<>();
                    deleteFiles = new ArrayList<>();
                    imageIndex = 0;
                    if(newProduct != null) {
                        product.setIdProduct(newProduct.getIdProduct());
                        stage.setUserData(newProduct);
                    }
                    stage.close();

                }
            }else{
                showAlertEmptyFields("No puedes dejar campos indispensables vacios");
            }
        });
    }
}
