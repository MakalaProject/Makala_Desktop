package org.example.controllers.products.subcontrollers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.interfaces.IControllerCreate;
import org.example.model.ChangedVerificationFields;
import org.example.model.FocusVerificationFields;
import org.example.model.products.*;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class InsideProductController implements Initializable, IControllerCreate<InsideProduct> {
    @FXML public Label titleLabel;
    @FXML public TextField cantidadField;
    @FXML public FontAwesomeIconView updateButton;
    @FXML public FontAwesomeIconView deleteButton;
    private InsideProduct insideProduct = new InsideProduct();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cantidadField.textProperty().addListener(new ChangedVerificationFields(cantidadField, true, 3,2));
        cantidadField.focusedProperty().addListener(new FocusVerificationFields(cantidadField, true, 3,2));
        updateButton.setOnMouseClicked(mouseEvent -> {
            Node source = (Node)  mouseEvent.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();
            stage.close();
            setInfo(insideProduct);
            stage.setUserData(new InternalProductPropertiesToSend(insideProduct, Action.UPDATE));
        });

        deleteButton.setOnMouseClicked(mouseEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Eliminar producto");
            alert.setContentText("¿Seguro quieres eliminarlo?");
            Optional<ButtonType> result = alert.showAndWait();
            Node source = (Node)  mouseEvent.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();
            if (result.get() == ButtonType.OK){
                stage.setUserData(new InternalProductPropertiesToSend(insideProduct, Action.UPDATE));
            }
            stage.close();
        });
    }


    public void setInternalProduct(InsideProduct insideProduct){
        this.insideProduct =  new InsideProduct(insideProduct.getIdProduct(), insideProduct.getName(), new BigDecimal(0), false);
        titleLabel.setText(insideProduct.getName());
        cantidadField.setText(insideProduct.getAmount().toString());
    }

    @Override
    public void setInfo(InsideProduct insideProduct) {
        insideProduct.setAmount(new BigDecimal(cantidadField.getText()));
    }


}
