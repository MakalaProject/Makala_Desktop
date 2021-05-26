package org.example.controllers.elements.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.model.DataAnalysisReceived;

import java.net.URL;
import java.util.ResourceBundle;

public class StatisticsProductInfo implements Initializable {
    @FXML private TextField cantidadField;
    @FXML private TextField nombreField;
    @FXML private TextField clasificacionField;
    @FXML private ImageView imagen;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setObject(DataAnalysisReceived data){
        cantidadField.setText(data.getAmount().toString());
        nombreField.setText(data.toString());
        clasificacionField.setText(data.getClasification());
        imagen.setImage(new Image(data.getImagePath()));
    }
}
