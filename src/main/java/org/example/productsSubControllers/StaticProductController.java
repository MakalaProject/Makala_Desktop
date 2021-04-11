package org.example.productsSubControllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import org.example.interfaces.IControllerProducts;
import org.example.model.products.Product;
import org.example.model.products.StaticProduct;
import org.example.services.Request;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class StaticProductController implements Initializable, IControllerProducts<StaticProduct> {
    @FXML public TextField altoField;
    @FXML public TextField anchoField;
    @FXML public TextField largoField;
    @FXML public ColorPicker color;

    public StaticProductController(){
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @Override
    public String[] getIdentifier() {
        return new String[]{"Fijo"};
    }

    @Override
    public String getResource() {
        return "/fxml/static_product_properties.fxml";
    }

    @Override
    public StaticProduct getObjectByFields() {
        if(!altoField.getText().isEmpty() || !anchoField.getText().isEmpty() || !largoField.getText().isEmpty()) {
            return getObject();
        }
        return null;
    }


    @Override
    public StaticProduct findObject(Product product) {
        StaticProduct staticProduct = (StaticProduct) Request.find("products/statics", product.getIdProduct(), StaticProduct.class);
        setObject(staticProduct);
        return staticProduct;
    }


    @Override
    public StaticProduct getObject() {
        StaticProduct staticProduct = new StaticProduct();
        staticProduct.getMeasures().setX(new BigDecimal(anchoField.getText()));
        staticProduct.getMeasures().setY(new BigDecimal(altoField.getText()));
        staticProduct.getMeasures().setZ(new BigDecimal(largoField.getText()));
        return staticProduct;
    }

    @Override
    public void setObject(StaticProduct staticProduct) {
        anchoField.setText(String.valueOf(staticProduct.getMeasures().getX()));
        altoField.setText(String.valueOf(staticProduct.getMeasures().getY()));
        largoField.setText(String.valueOf(staticProduct.getMeasures().getZ()));

    }
}
