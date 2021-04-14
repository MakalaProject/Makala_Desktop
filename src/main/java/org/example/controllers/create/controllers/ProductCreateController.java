package org.example.controllers.create.controllers;

import org.example.controllers.parent.controllers.ProductParentController;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.example.model.Picture;
import org.example.model.ChangedVerificationFields;
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
import java.util.ResourceBundle;

public class ProductCreateController extends ProductParentController {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url,resourceBundle);
        clasificacionComboBox.getSelectionModel().select(0);
        tipoComboBox.getSelectionModel().select(0);
        privacidadComboBox.getSelectionModel().select(0);
        nombreField.setText("Nuevo Producto");

        //Verifications with regex
        maxField.textProperty().addListener(new ChangedVerificationFields(maxField, true, 3));
        minField.textProperty().addListener(new ChangedVerificationFields(minField, true, 3));
        precioField.textProperty().addListener(new ChangedVerificationFields(precioField, true, 4,2));

        clasificacionComboBox.valueProperty().addListener(new ChangeListener<ProductClassDto>() {
            @Override
            public void changed(ObservableValue<? extends ProductClassDto> observableValue, ProductClassDto productClassDto, ProductClassDto t1) {
                for (IControllerProducts controller : propertiesControllers) {
                    if (Arrays.asList(controller.getIdentifier()).contains(t1)){
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
                    ArrayList<Picture> pictures = new ArrayList<>();
                    files = ImageService.uploadImages(files);
                    if(files != null){
                        for(String s: files){
                            pictures.add(new Picture(s));
                        }
                    }
                    product.setPictures(pictures);
                    Product newProduct = (Product) Request.postJ(product.getRoute(), product);
                    product.setIdProduct(newProduct.getIdProduct());
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


    @Override
    public void setInfo(Product product){
        super.setInfo(product);
        product.setStock(0);
    }
}
