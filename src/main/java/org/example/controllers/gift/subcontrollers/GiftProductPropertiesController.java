package org.example.controllers.gift.subcontrollers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.interfaces.IControllerCreate;
import org.example.model.*;
import org.example.model.products.*;
import org.example.services.Request;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class GiftProductPropertiesController implements Initializable, IControllerCreate<GiftProductsToSend> {
    @FXML public Label titleLabel;
    @FXML public TextField cantidadField;
    @FXML public ComboBox<Hole> orificioComboBox;
    @FXML public ComboBox<String> categoriaComboBox;
    @FXML public FontAwesomeIconView updateButton;
    @FXML public FontAwesomeIconView deleteButton;
    @FXML public TextField altoIntField;
    @FXML public TextField anchoIntField;
    @FXML public TextField largoIntField;
    private BoxProduct boxProduct;
    private StaticProduct staticProduct;
    private Gift gift;
    private Boolean create;
    private int originalAmount;
    private GiftProductsToSend giftProductToSend;
    private final ObservableList<String> categoryItems = FXCollections.observableArrayList( "Similar","Fijo", "Libre");
    private final ObservableList<Hole> hole = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cantidadField.focusedProperty().addListener(new FocusVerificationFields(cantidadField, false, 2));
        cantidadField.textProperty().addListener(new ChangedVerificationFields(cantidadField, false, 2));
        categoriaComboBox.setItems(categoryItems);
        categoriaComboBox.getSelectionModel().select(0);
        updateButton.setOnMouseClicked(mouseEvent -> {
            if (!cantidadField.getText().isEmpty() ) {
                if (Integer.parseInt(cantidadField.getText()) > 0 ) {
                    setInfo(giftProductToSend);
                    Node source = (Node)  mouseEvent.getSource();
                    Stage stage  = (Stage) source.getScene().getWindow();
                    if (!volumeValidation()){
                        alertOutOfBounds();
                        return;
                    }
                    giftProductToSend.setProduct(staticProduct);
                    giftProductToSend.setAction(Action.UPDATE);
                    stage.setUserData(giftProductToSend);
                    stage.close();
                }else {
                    checkFields();
                    showAlertEmptyFields("Los valores de los campos no pueden ser 0");
                }
            }else {
                checkFields();
                showAlertEmptyFields("No puedes dejar campos vacios");
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
                giftProductToSend.setAction(Action.DELETE);
                stage.setUserData(giftProductToSend);
            }
            stage.close();
        });
    }

    public void alertOutOfBounds(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Medidas fuera de rango");
        alert.setContentText("El producto que intentas agregar excede el volumen de la division, espacio disponible: " + boxProduct.getAvailableVolume(giftProductToSend.getHoleNumber(), gift.getProductsVolume(giftProductToSend.getHoleNumber(),boxProduct)) + "cm3");
        alert.showAndWait();
    }

    @Override
    public void setProduct(GiftProductsToSend product, boolean isCreate, BoxProduct container, Gift gift){
        this.create = isCreate;
        this.gift = gift;
        boxProduct = container;
        deleteButton.setVisible(!isCreate);
        giftProductToSend = product;
        originalAmount = product.getAmount();
        titleLabel.setText(product.getProduct().getName());
        cantidadField.setText(product.getAmount().toString());
        categoriaComboBox.setValue(product.getCategory());
        altoIntField.setText(boxProduct.getInternalMeasures().getY().toString());
        anchoIntField.setText(boxProduct.getInternalMeasures().getX().toString());
        largoIntField.setText(boxProduct.getInternalMeasures().getZ().toString());
        if(!isCreate){
            orificioComboBox.setDisable(true);
        }
        if(boxProduct.getHolesDimensions().size() == 0){
            orificioComboBox.getItems().clear();
            orificioComboBox.setDisable(true);
        }else{
            orificioComboBox.getItems().setAll(FXCollections.observableArrayList(boxProduct.getHolesDimensions()));
            orificioComboBox.setValue(orificioComboBox.getItems().stream().filter(pro -> pro.getHoleNumber().equals(giftProductToSend.getHoleNumber())).findAny().orElse(null));
        }
    }
    protected void checkFields(){
        if (cantidadField.getText().isEmpty() || Integer.parseInt(cantidadField.getText())==0) {
            cantidadField.setStyle("-fx-background-color: #fea08c; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }else{
            cantidadField.setStyle("-fx-background-color: #E3DAD8; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }
    }


    private boolean volumeValidation(){
        staticProduct = (StaticProduct) Request.find("products/statics", giftProductToSend.getProduct().getIdProduct(), StaticProduct.class);
        if(boxProduct.verifyProductBounds(giftProductToSend.getHoleNumber(), staticProduct)){
            if (!create){
                return boxProduct.getAvailableVolume(giftProductToSend.getHoleNumber(), gift.getProductsVolume(giftProductToSend.getHoleNumber(),boxProduct)).add(staticProduct.getVolume().multiply(new BigDecimal(originalAmount))).compareTo(staticProduct.getVolume().multiply(new BigDecimal(giftProductToSend.getAmount()))) >= 0;
            }
            return boxProduct.getAvailableVolume(giftProductToSend.getHoleNumber(), gift.getProductsVolume(giftProductToSend.getHoleNumber(),boxProduct)).compareTo(staticProduct.getVolume().multiply(new BigDecimal(giftProductToSend.getAmount()))) >= 0;
        }
        return false;
    }

    @Override
    public void setInfo(GiftProductsToSend giftProduct) {
        giftProduct.setCategory(categoriaComboBox.getSelectionModel().getSelectedItem());
        giftProduct.setAmount(Integer.parseInt(cantidadField.getText()));
        giftProduct.setProduct(this.giftProductToSend.getProduct());
        if(boxProduct.getHolesDimensions().size() == 0){
            giftProduct.setHoleNumber(1);
        }else{
            giftProduct.setHoleNumber(orificioComboBox.getSelectionModel().getSelectedItem().getHoleNumber());
        }
    }

}
