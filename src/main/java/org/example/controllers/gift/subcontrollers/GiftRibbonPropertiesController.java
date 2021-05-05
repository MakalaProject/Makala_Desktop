package org.example.controllers.gift.subcontrollers;

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
import org.example.model.RibbonProductToSend;
import org.example.model.products.Action;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class GiftRibbonPropertiesController implements Initializable, IControllerCreate<RibbonProductToSend> {
    @FXML
    public Label titleLabel;
    @FXML public TextField largoField;
    @FXML public TextField cantidadField;
    @FXML public FontAwesomeIconView updateButton;
    @FXML public FontAwesomeIconView deleteButton;


    private RibbonProductToSend ribbonProductToSend = new RibbonProductToSend();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        largoField.focusedProperty().addListener(new FocusVerificationFields(largoField, true, 2,2));
        largoField.textProperty().addListener(new ChangedVerificationFields(largoField, true, 2,2));
        cantidadField.focusedProperty().addListener(new FocusVerificationFields(cantidadField, false, 2));
        cantidadField.textProperty().addListener(new ChangedVerificationFields(cantidadField, false, 2));

        updateButton.setOnMouseClicked(mouseEvent -> {
            Node source = (Node)  mouseEvent.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();
            stage.close();
            setInfo(ribbonProductToSend);
            ribbonProductToSend.setAction(Action.UPDATE);
            stage.setUserData(ribbonProductToSend);
        });

        deleteButton.setOnMouseClicked(mouseEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Eliminar producto");
            alert.setContentText("¿Seguro quieres eliminarlo?");
            Optional<ButtonType> result = alert.showAndWait();
            Node source = (Node)  mouseEvent.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();
            if (result.get() == ButtonType.OK){
                ribbonProductToSend.setAction(Action.DELETE);
                stage.setUserData(ribbonProductToSend);
            }
            stage.close();
        });
    }

    @Override
    public void setProduct(RibbonProductToSend ribbonProduct, boolean isCreate){
        if (!isCreate){
            deleteButton.setVisible(true);
        }else {
            deleteButton.setVisible(false);
        }
        product = ribbonProduct.getProduct();
        titleLabel.setText(product.getName());
        cantidadField.setText(ribbonProduct.getAmount().toString());
        largoField.setText(ribbonProduct.getLengthCm().toString());
    }

    @Override
    public void setInfo(RibbonProductToSend ribbonProductToSend) {
        ribbonProductToSend.setLengthCm(new BigDecimal(largoField.getText()));
        ribbonProductToSend.setAmount(Integer.parseInt(cantidadField.getText()));
    }
}
