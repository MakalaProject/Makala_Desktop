package org.example.controllers.gift.subcontrollers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.interfaces.IControllerCreate;
import org.example.model.BowProductToSend;
import org.example.model.ChangedVerificationFields;
import org.example.model.FocusVerificationFields;


import java.math.BigDecimal;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class GiftBowPropertiesController implements Initializable, IControllerCreate<BowProductToSend> {
    @FXML
    public Label titleLabel;
    @FXML public TextField largoField;
    @FXML public TextField cantidadField;
    @FXML public FontAwesomeIconView updateButton;
    @FXML public FontAwesomeIconView deleteButton;
    @FXML public AnchorPane disableAnchorPane;


    private BowProductToSend bowProductToSend = new BowProductToSend();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        largoField.focusedProperty().addListener(new FocusVerificationFields(largoField, true, 2,2));
        largoField.textProperty().addListener(new ChangedVerificationFields(largoField, true, 2,2));
        cantidadField.focusedProperty().addListener(new FocusVerificationFields(cantidadField, false, 2));
        cantidadField.textProperty().addListener(new ChangedVerificationFields(cantidadField, false, 2));

        updateButton.setOnMouseClicked(mouseEvent -> {

        });

        deleteButton.setOnMouseClicked(mouseEvent -> {

        });
    }

    @Override
    public void setProduct(BowProductToSend ribbonProduct, boolean isCreate, boolean editProduct){
        deleteButton.setVisible(!isCreate);
        if (!editProduct){
            updateButton.setVisible(false);
            disableAnchorPane.setVisible(true);
            deleteButton.setVisible(false);
        }

    }

    protected void checkFields(){
        if (largoField.getText().isEmpty() || Float.parseFloat(largoField.getText())==0) {
            largoField.setStyle("-fx-background-color: #fea08c; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }else{
            largoField.setStyle("-fx-background-color: #E3DAD8; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }
        if (cantidadField.getText().isEmpty() || Integer.parseInt(cantidadField.getText())==0) {
            cantidadField.setStyle("-fx-background-color: #fea08c; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }else{
            cantidadField.setStyle("-fx-background-color: #E3DAD8; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }
    }

    @Override
    public void setInfo(BowProductToSend ribbonProductToSend) {

    }
}
