package org.example.controllers.elements.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.model.products.Product;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class StockTimeInfo implements Initializable {
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
    @FXML private TextField promedioField;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setObject(Product data){
        if(data.getProductClassDto().getProductType().equals("Listones")){
            data.setStock(data.getStock().divide(new BigDecimal(10)));
            data.setMin(data.getMin()/10);
            data.setMax(data.getMax()/10);
        }
        if(data.getProductClassDto().getProductType().equals("Papeles")){
            data.setStock(data.getStock().divide(new BigDecimal(100)));
            data.setMin(data.getMin()/100);
            data.setMax(data.getMax()/100);
        }
        stockField.setText(data.getStock().toString());
        minField.setText(data.getMin().toString());
        maxField.setText(data.getMax().toString());
        nombreField.setText(data.toString());
        clasificacionField.setText(data.getProductClassDto().getClassification());
        promedioField.setText(data.getAvgDays().toString());
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
