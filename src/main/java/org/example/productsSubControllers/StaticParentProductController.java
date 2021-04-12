package org.example.productsSubControllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.example.interfaces.IControllerProducts;
import org.example.model.products.Product;
import org.example.model.products.StaticProduct;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
public abstract class StaticParentProductController <D extends StaticProduct> implements Initializable, IControllerProducts<D> {
    @FXML public TextField altoField;
    @FXML public TextField anchoField;
    @FXML public TextField largoField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public String[] getIdentifier() {
        return new String[]{"Fijo", "Comestible"};
    }

    @Override
    public String getResource() {
        return "/fxml/static_product_properties.fxml";
    }

    @Override
    public D getObjectByFields() {
        if(!altoField.getText().isEmpty() || !anchoField.getText().isEmpty() || !largoField.getText().isEmpty()) {
            return getObject();
        }
        return null;
    }

    @Override
    public abstract  D findObject(Product object);

    public D getObject(Class<?> classType) {
        D object = null;
        try {
            object = (D) classType.newInstance();
            object.getMeasures().setX(new BigDecimal(anchoField.getText()));
            object.getMeasures().setY(new BigDecimal(altoField.getText()));
            object.getMeasures().setZ(new BigDecimal(largoField.getText()));
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return object;
    }

    @Override
    public void setObject(D staticProduct) {
        anchoField.setText(String.valueOf(staticProduct.getMeasures().getX()));
        altoField.setText(String.valueOf(staticProduct.getMeasures().getY()));
        largoField.setText(String.valueOf(staticProduct.getMeasures().getZ()));
    }
}
