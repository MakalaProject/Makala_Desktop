package org.example.controllers.products.subcontrollers;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.example.interfaces.IControllerCreate;
import org.example.interfaces.IControllerProducts;
import org.example.model.ChangedVerificationFields;
import org.example.model.FocusVerificationFields;
import org.example.model.products.Product;
import org.example.model.products.StaticProduct;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
public abstract class StaticParentProductController <D extends StaticProduct> implements Initializable, IControllerProducts<D>, IControllerCreate<StaticProduct> {
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
        return new String[]{"Fijo","Recipientes", "Comestible"};
    }

    @Override
    public String getResource() {
        return "/fxml/static_product_properties.fxml";
    }


    @Override
    public abstract  D findObject(Product object);

    public D getObject(Class<?> classType) {
        checkFields();
        if(!altoField.getText().isEmpty() && !anchoField.getText().isEmpty() && !largoField.getText().isEmpty()) {
            if (Float.parseFloat(altoField.getText()) > 0 && Float.parseFloat(anchoField.getText()) > 0 && Float.parseFloat(largoField.getText()) > 0) {
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
            } else {
                showAlertEmptyFields("Las medidas no pueden ser 0");
            }
        }else {
            showAlertEmptyFields("No puedes dejar los campos de las medidas vacios");
        }
        return null;
    }
    @Override
    public void checkFields() {
        if (altoField.getText().isEmpty() || Float.parseFloat(altoField.getText())==0){
            altoField.setStyle("-fx-background-color: #fea08c; -fx-border-color: white white black white; -fx-border-width: 2;");
        }else{
            altoField.setStyle("-fx-background-color: white; -fx-border-color: white white black white; -fx-border-width: 2;");
        }
        if (anchoField.getText().isEmpty() || Float.parseFloat(anchoField.getText())==0){
            anchoField.setStyle("-fx-background-color: #fea08c; -fx-border-color: white white black white; -fx-border-width: 2;");
        }else{
            anchoField.setStyle("-fx-background-color: white; -fx-border-color: white white black white; -fx-border-width: 2;");
        }
        if (largoField.getText().isEmpty() || Float.parseFloat(largoField.getText())==0){
            largoField.setStyle("-fx-background-color: #fea08c; -fx-border-color: white white black white; -fx-border-width: 2;");
        }else{
            largoField.setStyle("-fx-background-color: white; -fx-border-color: white white black white; -fx-border-width: 2;");
        }
    }


    @Override
    public void setObject(D staticProduct) {
        anchoField.setText(String.valueOf(staticProduct.getMeasures().getX()));
        altoField.setText(String.valueOf(staticProduct.getMeasures().getY()));
        largoField.setText(String.valueOf(staticProduct.getMeasures().getZ()));
    }

    @Override
    public void updateList() {

    }

}
