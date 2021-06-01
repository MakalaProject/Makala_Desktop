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
import org.example.model.PaperProductToSend;
import org.example.model.products.Action;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class GiftPaperPropertiesController implements Initializable, IControllerCreate<PaperProductToSend> {
    @FXML public Label titleLabel;
    @FXML public TextField largoField;
    @FXML public TextField anchoField;
    @FXML public TextField cantidadField;
    @FXML public FontAwesomeIconView updateButton;
    @FXML public FontAwesomeIconView deleteButton;
    private PaperProductToSend paperProductToSend = new PaperProductToSend();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        largoField.focusedProperty().addListener(new FocusVerificationFields(largoField, true, 2,2));
        largoField.textProperty().addListener(new ChangedVerificationFields(largoField, true, 2,2));
        anchoField.focusedProperty().addListener(new FocusVerificationFields(anchoField, true, 2,2));
        anchoField.textProperty().addListener(new ChangedVerificationFields(anchoField, true, 2,2));
        cantidadField.focusedProperty().addListener(new FocusVerificationFields(cantidadField, false, 2));
        cantidadField.textProperty().addListener(new ChangedVerificationFields(cantidadField, false, 2));

        updateButton.setOnMouseClicked(mouseEvent -> {
            setInfo(paperProductToSend);
            if(paperProductToSend.getAmount() > 0) {
                Node source = (Node) mouseEvent.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
                paperProductToSend.setAction(Action.UPDATE);
                stage.setUserData(paperProductToSend);
            }else{
                showAlertEmptyFields("Cantidad vacia");
            }
        });

        deleteButton.setOnMouseClicked(mouseEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Eliminar producto");
            alert.setContentText("¿Seguro quieres eliminarlo?");
            Optional<ButtonType> result = alert.showAndWait();
            Node source = (Node)  mouseEvent.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();
            if (result.get() == ButtonType.OK){
                paperProductToSend.setAction(Action.DELETE);
                stage.setUserData(paperProductToSend);
            }
            stage.close();
        });
    }

    @Override
    public void setProduct(PaperProductToSend product, boolean isCreate){
        deleteButton.setVisible(!isCreate);
        paperProductToSend = product;
        titleLabel.setText(product.getProduct().getName());
        cantidadField.setText(product.getAmount().toString());
        anchoField.setText(product.getWidthCm().toString());
        largoField.setText(product.getHeightCm().toString());
    }

    @Override
    public void setInfo(PaperProductToSend paperProductToSend) {
        paperProductToSend.setAmount(Integer.parseInt(cantidadField.getText()));
        paperProductToSend.setWidthCm(new BigDecimal(anchoField.getText()));
        paperProductToSend.setHeightCm(new BigDecimal(largoField.getText()));
    }
}
