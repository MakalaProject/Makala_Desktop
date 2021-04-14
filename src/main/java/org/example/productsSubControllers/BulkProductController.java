package org.example.productsSubControllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.example.interfaces.IControllerProducts;
import org.example.model.ChangedVerificationFields;
import org.example.model.products.BulkProduct;
import org.example.model.products.Product;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class BulkProductController implements Initializable, IControllerProducts<BulkProduct> {
    @FXML public TextField porcentajeField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        porcentajeField.textProperty().addListener(new ChangedVerificationFields(porcentajeField, true, 2,2));
    }

    @Override
    public String[] getIdentifier() {
        return new String[]{"Granel"};
    }

    @Override
    public String getResource() {
        return "/fxml/bulk_product_properties.fxml";
    }

    @Override
    public BulkProduct getObject() {
        BulkProduct bulkProduct = new BulkProduct();
        bulkProduct.setLossPercent(new BigDecimal(porcentajeField.getText()));
        return bulkProduct;
    }

    @Override
    public void setObject(BulkProduct bulkProduct) {
        porcentajeField.setText(bulkProduct.getLossPercent().toString());
    }

    @Override
    public BulkProduct findObject(Product object) {
        return findObject( object,"products/bulks", BulkProduct.class);
    }
}
