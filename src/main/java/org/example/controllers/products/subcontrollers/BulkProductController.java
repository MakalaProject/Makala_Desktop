package org.example.controllers.products.subcontrollers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.example.interfaces.IControllerCreate;
import org.example.interfaces.IControllerProducts;
import org.example.model.ChangedVerificationFields;
import org.example.model.products.BulkProduct;
import org.example.model.products.Product;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class BulkProductController implements Initializable, IControllerProducts<BulkProduct>, IControllerCreate<BulkProduct> {
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
        if (!porcentajeField.getText().isEmpty()){
            if(Float.parseFloat(porcentajeField.getText())>0) {
                BulkProduct bulkProduct = new BulkProduct();
                bulkProduct.setLossPercent(new BigDecimal(porcentajeField.getText()));
                return bulkProduct;
            }else {
                showAlertEmptyFields("El porcentaje de perdida no puede ser 0");
            }
        }else {
            showAlertEmptyFields("No puedes dejar el campo de porcentaje de perdida vacio");
        }
        return null;
    }

    @Override
    public BulkProduct getObjectInstance() {
        return new BulkProduct();
    }

    @Override
    public void setObject(BulkProduct bulkProduct) {
        porcentajeField.setText(bulkProduct.getLossPercent().toString());
    }

    @Override
    public BulkProduct findObject(Product object) {
        return findObject( object,"products/bulks", BulkProduct.class);
    }

    @Override
    public void updateList() {

    }

    @Override
    public void setInfo(BulkProduct object) {

    }
}
