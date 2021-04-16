package org.example.controllers.products.subcontrollers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import org.example.interfaces.IControllerProducts;
import org.example.model.products.PaperProduct;
import org.example.model.products.Product;

import java.net.URL;
import java.util.ResourceBundle;

public class PaperProductController implements Initializable, IControllerProducts<PaperProduct> {
    @FXML public ColorPicker colorPicker;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public String[] getIdentifier() {
        return new String[]{"Papeles"};
    }

    @Override
    public String getResource() {
        return "/fxml/paper_product_properties.fxml";
    }

    @Override
    public PaperProduct getObject() {
        PaperProduct paperProduct = new PaperProduct();
        paperProduct.setRgb("#" + colorPicker.getValue().toString().substring(2));
        return paperProduct;
    }

    @Override
    public void setObject(PaperProduct paperProduct) {
        colorPicker.setValue(Color.web(paperProduct.getRgb()));
    }


    @Override
    public PaperProduct findObject(Product object) {
        return findObject( object,"products/papers", PaperProduct.class);
    }
}
