package org.example.controllers.elements.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.model.DataAnalysisReceived;
import org.example.model.products.Product;

import java.net.URL;
import java.util.ResourceBundle;

public class ProductionPlanifierInfo implements Initializable {
    @FXML
    private TextField stockField;
    @FXML private TextField nombreField;
    @FXML private TextField minField;
    @FXML private TextField maxField;
    @FXML private TextField clasificacionField;
    @FXML private Label unidadField1;
    @FXML private Label unidadField2;
    @FXML private Label unidadField3;
    @FXML private ImageView imagen;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setObject(Product data){
        stockField.setText(data.getStock().toString());
        minField.setText(data.getMin().toString());
        maxField.setText(data.getMax().toString());
        nombreField.setText(data.toString());
        clasificacionField.setText(data.getProductClassDto().getClassification());
        if(data.getPictures().size() != 0) {
            imagen.setImage(new Image(data.getPictures().get(0).getPath()));
        }else {
            imagen.setImage(new Image("/images/product.png"));
        }
        if(data.getProductClassDto().getProductType().equals("Listones")){
            unidadField1.setText("m");
            unidadField2.setText("m");
            unidadField3.setText("m");
        }
        if (data.getProductClassDto().getProductType().equals("Papeles")){
            unidadField1.setText("m2");
            unidadField2.setText("m2");
            unidadField3.setText("m2");
        }
    }
}
