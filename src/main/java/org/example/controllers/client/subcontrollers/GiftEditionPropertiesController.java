package org.example.controllers.client.subcontrollers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.model.GiftEditable;
import org.example.model.Order;
import org.example.model.ProductEdition;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GiftEditionPropertiesController implements Initializable {

    @FXML ListView<ProductEdition> productsChangeableListView;
    @FXML TextField nombreField;
    @FXML TextField cantidadField;

    private GiftEditable actualGiftEditable;
    private ObservableList<ProductEdition> productsChangeableObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        productsChangeableListView.setOnMouseClicked(mouseEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/product_edition.fxml"));
            try {
                ProducEditionPropertiesController dialogController = new ProducEditionPropertiesController();
                fxmlLoader.setController(dialogController);
                Parent parent = fxmlLoader.load();
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                dialogController.setObject(productsChangeableListView.getSelectionModel().getSelectedItem());
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void setObject(GiftEditable giftEditable){
        actualGiftEditable = giftEditable;
        putFields();
    }

    private void putFields(){
        productsChangeableObservableList.setAll(actualGiftEditable.getProductsEdition());
        nombreField.setText(actualGiftEditable.getGift().getName());
        cantidadField.setText(actualGiftEditable.getAmountGifts().toString());
        showList();
    }

    private void showList() {
        productsChangeableListView.setItems(productsChangeableObservableList);
        productsChangeableListView.prefHeightProperty().bind(Bindings.size(FXCollections.observableList(productsChangeableObservableList)).multiply(23.7));
    }
}
