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
    @FXML public AnchorPane disableAnchorPane;


    private RibbonProductToSend ribbonProductToSend = new RibbonProductToSend();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        largoField.focusedProperty().addListener(new FocusVerificationFields(largoField, true, 2,2));
        largoField.textProperty().addListener(new ChangedVerificationFields(largoField, true, 2,2));
        cantidadField.focusedProperty().addListener(new FocusVerificationFields(cantidadField, false, 2));
        cantidadField.textProperty().addListener(new ChangedVerificationFields(cantidadField, false, 2));

        updateButton.setOnMouseClicked(mouseEvent -> {
            if (!cantidadField.getText().isEmpty() && !largoField.getText().isEmpty()) {
                if (Integer.parseInt(cantidadField.getText()) > 0 && Float.parseFloat(largoField.getText())>0) {
                    setInfo(ribbonProductToSend);
                    Node source = (Node) mouseEvent.getSource();
                    Stage stage = (Stage) source.getScene().getWindow();
                    stage.close();
                    ribbonProductToSend.setAction(Action.UPDATE);
                    stage.setUserData(ribbonProductToSend);
                }else {
                    showAlertEmptyFields("Los valores de los campos no pueden ser 0");
                    checkFields();
                }
            }else {
                showAlertEmptyFields("No puedes dejar campos vacios");
                checkFields();
            }
            checkFields();
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
    public void setProduct(RibbonProductToSend ribbonProduct, boolean isCreate, boolean editProduct){
        deleteButton.setVisible(!isCreate);
        if (!editProduct){
            updateButton.setVisible(false);
            disableAnchorPane.setVisible(true);
            deleteButton.setVisible(false);
        }
        ribbonProductToSend = ribbonProduct;
        titleLabel.setText(ribbonProduct.getProduct().getName());
        cantidadField.setText(ribbonProduct.getAmount().toString());
        largoField.setText(ribbonProduct.getLengthCm().toString());
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
    public void setInfo(RibbonProductToSend ribbonProductToSend) {
        ribbonProductToSend.setLengthCm(new BigDecimal(largoField.getText()));
        ribbonProductToSend.setAmount(Integer.parseInt(cantidadField.getText()));
    }
}
