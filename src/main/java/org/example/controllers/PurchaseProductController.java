package org.example.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.interfaces.IControllerCreate;
import org.example.model.ChangedVerificationFields;
import org.example.model.FocusVerificationFields;
import org.example.model.ProductExpiration;
import org.example.model.PurchaseProduct;
import org.example.model.products.Action;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class PurchaseProductController implements Initializable, IControllerCreate<PurchaseProduct> {
    @FXML public Label titleLabel;
    @FXML public TextField cantidadField;
    @FXML public DatePicker expiryDatePicker;
    @FXML public Label expiryDateLabel;
    @FXML public Label unidadLabel;
    @FXML public FontAwesomeIconView updateButton;
    @FXML public FontAwesomeIconView deleteButton;
    @FXML public AnchorPane disableAnchorPane;
    PurchaseProduct purchaseProduct = new PurchaseProduct();

    public void setProduct(PurchaseProduct product, boolean isCreate, boolean edit) {
        deleteButton.setVisible(!isCreate);
        if (!edit){
            deleteButton.setVisible(false);
            updateButton.setVisible(false);
            disableAnchorPane.setVisible(true);
        }
        purchaseProduct = product;
        if (product.getProduct().getProductClassDto() != null && !product.getProduct().getProductClassDto().getProductType().equals("Comestible") && !product.getProduct().getProductClassDto().getProductType().equals("Granel")){
            if (product.getProduct().getProductClassDto().getProductType().equals("Granel")){
                unidadLabel.setText("grs");
            }else if(product.getProduct().getProductClassDto().getProductType().equals("Listones")){
                unidadLabel.setText("m");
            }else if(product.getProduct().getProductClassDto().getProductType().equals("Papeles")){
                unidadLabel.setText("m2");
            }
            expiryDatePicker.setVisible(false);
            expiryDateLabel.setVisible(false);
        }else {
            if (product.getPackageP()!= null){
                expiryDatePicker.setValue(product.getPackageP().getExpiryDate());
            }
        }
        cantidadField.setText(product.getAmount().toString());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cantidadField.textProperty().addListener(new ChangedVerificationFields(cantidadField, false, 4));
        cantidadField.focusedProperty().addListener(new FocusVerificationFields(cantidadField, false, 4));

        expiryDatePicker.setDayCellFactory(d ->
        new DateCell() {
            @Override public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isBefore(LocalDate.now()));
            }});
        expiryDatePicker.setValue(LocalDate.now());
        updateButton.setOnMouseClicked(mouseEvent -> {
            if (!cantidadField.getText().isEmpty()) {
                if (Integer.parseInt(cantidadField.getText())>0) {
                    setInfo(purchaseProduct);
                    Node source = (Node) mouseEvent.getSource();
                    Stage stage = (Stage) source.getScene().getWindow();
                    purchaseProduct.setAction(Action.UPDATE);
                    stage.setUserData(purchaseProduct);
                    stage.close();
                }else {
                    showAlertEmptyFields("La cantidad no puede ser 0");
                }
            }else {
                showAlertEmptyFields("No puedes dejar el campo de cantidad vacio");
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
                purchaseProduct.setAction(Action.DELETE);
                stage.setUserData(purchaseProduct);
            }
            stage.close();
        });
    }

    @Override
    public void setInfo(PurchaseProduct purchaseProduct) {
        if (purchaseProduct.getProduct().getProductClassDto().getProductType().equals("Comestible") || purchaseProduct.getProduct().getProductClassDto().getProductType().equals("Granel")){
            ProductExpiration productExpiration = new ProductExpiration(expiryDatePicker.getValue(), purchaseProduct.getAmount());
            purchaseProduct.setPackageP(productExpiration);
        }
        if(purchaseProduct.getProduct().getProductClassDto().getProductType().equals("Listones")){
            purchaseProduct.setAmount(new BigDecimal(cantidadField.getText()).multiply(new BigDecimal("10")));
        }else if(purchaseProduct.getProduct().getProductClassDto().getProductType().equals("Papeles")){
            purchaseProduct.setAmount(new BigDecimal(cantidadField.getText()).multiply(new BigDecimal("100")));
        }else {
            purchaseProduct.setAmount(new BigDecimal(cantidadField.getText()));
        }

    }
}
