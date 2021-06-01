package org.example.controllers.products.subcontrollers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.example.interfaces.IControllerCreate;
import org.example.interfaces.IControllerProducts;
import org.example.model.ChangedVerificationFields;
import org.example.model.FocusVerificationFields;
import org.example.model.products.Product;
import org.example.model.products.RibbonProduct;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class RibbonProductController implements Initializable, IControllerProducts<RibbonProduct>, IControllerCreate<RibbonProduct> {
    @FXML public TextField anchoField;
    @FXML public ColorPicker colorPicker;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        anchoField.textProperty().addListener(new ChangedVerificationFields(anchoField, true, 1,2));
        anchoField.focusedProperty().addListener(new FocusVerificationFields(anchoField, true, 1,2));
    }

    @Override
    public String[] getIdentifier() {
        return new String[]{"Listones"};
    }

    @Override
    public String getResource() {
        return "/fxml/ribbon_product_properties.fxml";
    }

    @Override
    public RibbonProduct getObject() {
        RibbonProduct ribbonProduct = new RibbonProduct();
        ribbonProduct.setWidthIn(new BigDecimal(anchoField.getText()));
        ribbonProduct.setRgb(colorPicker.getValue().toString().substring(2));
        if (!anchoField.getText().isEmpty()){
            if (Float.parseFloat(anchoField.getText())>0){

            }else {
                showAlertEmptyFields("El ancho no puede ser igual a 0");
            }
        }else {
            showAlertEmptyFields("No puedes dejar el campo de ancho vacio");
        }
        return ribbonProduct;
    }

    @Override
    public RibbonProduct getObjectInstance() {
        return new RibbonProduct();
    }

    @Override
    public void setObject(RibbonProduct ribbonProduct) {
        anchoField.setText(ribbonProduct.getWidthIn().toString());
        colorPicker.setValue(Color.web(ribbonProduct.getRgb()));
    }


    @Override
    public RibbonProduct findObject(Product object) {
        return findObject( object,"products/ribbons", RibbonProduct.class);
    }

    @Override
    public void updateList() {

    }


    @Override
    public void setInfo(RibbonProduct object) {

    }
}
