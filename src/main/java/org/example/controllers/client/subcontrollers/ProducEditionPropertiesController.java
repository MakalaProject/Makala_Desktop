package org.example.controllers.client.subcontrollers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.example.model.ProductEdition;
import org.example.model.products.Product;
import org.example.services.Request;

import java.net.URL;
import java.util.ResourceBundle;

public class ProducEditionPropertiesController implements Initializable {
    @FXML TextField nombreField;
    @FXML TextField cantidadField;
    @FXML TextField originalNombreField;

    ProductEdition actualProductEdition;
    Product originalProduct;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setObject(ProductEdition selectedItem) {
        actualProductEdition = selectedItem;
        originalProduct = (Product) Request.find("products", actualProductEdition.getIdGiftContent(), Product.class);
        originalNombreField.setText(originalProduct.getName());
        nombreField.setText(actualProductEdition.getNewProduct().getName());
        cantidadField.setText(actualProductEdition.getNewAmount().toString());
    }
}
