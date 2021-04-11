package org.example.productsSubControllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.example.interfaces.IControllerProducts;
import org.example.model.products.Product;
import org.example.model.products.RibbonProduct;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class RibbonProductController implements Initializable, IControllerProducts<RibbonProduct> {
    @FXML public TextField anchoField;
    @FXML public ColorPicker colorPicker;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public String[] getIdentifier() {
        return new String[]{"Liston"};
    }

    @Override
    public String getResource() {
        return "/fxml/ribbon_product_properties.fxml";
    }

    @Override
    public RibbonProduct getObject() {
        RibbonProduct ribbonProduct = new RibbonProduct();
        ribbonProduct.setWidthCm(new BigDecimal(anchoField.getWidth()));
        ribbonProduct.setColor(colorPicker.getValue().toString());
        return ribbonProduct;
    }

    @Override
    public void setObject(RibbonProduct ribbonProduct) {
        anchoField.setText(ribbonProduct.getWidthCm().toString());
        colorPicker.setValue(Color.web(ribbonProduct.getColor()));
    }

    @Override
    public RibbonProduct getObjectByFields() {
        if (!anchoField.getText().isEmpty()){
            return getObject();
        }
        return null;
    }

    @Override
    public RibbonProduct findObject(Product Objcet) {
        return null;
    }

}
