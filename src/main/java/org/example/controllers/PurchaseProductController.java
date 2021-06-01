package org.example.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.interfaces.IControllerCreate;
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
    @FXML public FontAwesomeIconView updateButton;
    @FXML public FontAwesomeIconView deleteButton;
    PurchaseProduct purchaseProduct = new PurchaseProduct();

    public void setProduct(PurchaseProduct product, boolean isCreate) {
        deleteButton.setVisible(!isCreate);
        purchaseProduct = product;
        if (product.getProduct().getProductClassDto() != null && !product.getProduct().getProductClassDto().getProductType().equals("Comestible") && !product.getProduct().getProductClassDto().getProductType().equals("Granel")){
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
        expiryDatePicker.setDayCellFactory(d ->
        new DateCell() {
            @Override public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isBefore(LocalDate.now()));
            }});
        expiryDatePicker.setValue(LocalDate.now());
        updateButton.setOnMouseClicked(mouseEvent -> {
            setInfo(purchaseProduct);
            Node source = (Node)  mouseEvent.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();
            purchaseProduct.setAction(Action.UPDATE);
            stage.setUserData(purchaseProduct);
            stage.close();
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
        purchaseProduct.setAmount(new BigDecimal(cantidadField.getText()));
        if (purchaseProduct.getProduct().getProductClassDto().getProductType().equals("Comestible") || purchaseProduct.getProduct().getProductClassDto().getProductType().equals("Granel")){
            ProductExpiration productExpiration = new ProductExpiration(expiryDatePicker.getValue(), purchaseProduct.getAmount());
            purchaseProduct.setPackageP(productExpiration);
        }

    }
}
