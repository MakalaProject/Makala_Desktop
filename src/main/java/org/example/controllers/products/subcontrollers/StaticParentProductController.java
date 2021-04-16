package org.example.controllers.products.subcontrollers;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.example.interfaces.IControllerProducts;
import org.example.model.ChangedVerificationFields;
import org.example.model.FocusVerificationFields;
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

        altoField.textProperty().addListener(new ChangedVerificationFields(altoField, true, 2,2));
        largoField.textProperty().addListener(new ChangedVerificationFields(largoField, true, 2,2));
        anchoField.textProperty().addListener(new ChangedVerificationFields(anchoField, true, 2,2));
        altoField.focusedProperty().addListener(new FocusVerificationFields(altoField, true, 2,2));
        largoField.focusedProperty().addListener(new FocusVerificationFields(largoField, true, 2,2));
        anchoField.focusedProperty().addListener(new FocusVerificationFields(anchoField, true, 2,2));

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
