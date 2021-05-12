package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.model.PurchaseProduct;
import org.example.model.products.PackageProduct;

import java.net.URL;
import java.util.ResourceBundle;

public class ExpirationProductInfoController implements Initializable {
    @FXML
    Label productName;
    @FXML
    ImageView productImage;
    @FXML
    DatePicker expirationDate;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setProduct(PackageProduct product){
        productName.setText(product.getProduct().getName());
        if (product.getProduct().getPictures().size() != 0){
            productImage.setImage(new Image(product.getProduct().getPictures().get(0).getPath()));
        }
        expirationDate.setValue(product.getExpiryDate());
    }
}
