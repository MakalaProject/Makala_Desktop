package org.example.controllers.create.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.controllers.parent.controllers.GiftParentController;
import org.example.model.*;
import org.example.model.products.BoxProduct;
import org.example.model.products.StaticProduct;
import org.example.services.ImageService;
import org.example.services.Request;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GiftCreateController extends GiftParentController {
    @FXML protected ComboBox<String> privacidadComboBox;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url,resourceBundle);
        actualGift.setContainer(containerProducts.get(0));
        container = actualGift.getContainer();
        containerName.setText(container.getName());
        containerExtended = (BoxProduct)Request.find("products/boxes",actualGift.getContainer().getIdProduct(),BoxProduct.class);
        privacidadComboBox.getItems().addAll(privacyItems);
        privacidadComboBox.getSelectionModel().select(0);
        updateButton.setOnMouseClicked(mouseEvent -> {
            if( !nombreField.getText().isEmpty()){
                Gift gift = new Gift();
                setExtendedInternalProducts(gift);
                setInfo(gift);
                    Gift newGift = null;
                    try {
                        gift.setPictures(new ArrayList<>());
                        newGift = (Gift) Request.postJ(gift.getRoute(), gift);
                        gift.setIdGift(newGift.getIdGift());
                        gift = newGift;
                        ArrayList<Picture> pictures = new ArrayList<>();
                        files = ImageService.uploadImages(files);
                        if(files != null){
                            for(String s: files){
                                pictures.add(new Picture(s));
                            }
                        }
                        newGift.setPictures(new ArrayList<>());
                        newGift.setPictures(pictures);
                        gift = (Gift)Request.putJ(gift.getRoute(), gift);
                        newGift.setPictures(gift.getPictures());
                    } catch (Exception e) {
                        ImageService.deleteImages(files);
                        duplyElementAlert(gift.getIdentifier());
                        return;
                    }
                    Node source = (Node)  mouseEvent.getSource();
                    Stage stage  = (Stage) source.getScene().getWindow();
                    files = new ArrayList<>();
                    deleteFiles = new ArrayList<>();
                    imageIndex = 0;
                    if(newGift != null) {
                        gift.setIdGift(newGift.getIdGift());
                        newGift.setPrice(gift.getPrice());
                        stage.setUserData(newGift);
                    }
                    stage.close();
            }else{
                showAlertEmptyFields("No puedes dejar campos indispensables vacios");
            }
        });


        privacidadComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (userClicked && (privacidadComboBox.getValue().equals("Publico") || privacidadComboBox.getValue().equals("Premium"))){
                    userClicked = false;
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Cuidado");
                    alert.setHeaderText("Regalo no editable");
                    alert.setContentText("Una vez establecido este regalo no podras cambiarlo después");
                    alert.showAndWait();
                }
            }
        });

        privacidadComboBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                userClicked = true;
            }
        });

    }

    @Override
    public void setInfo(Gift gift){
        super.setInfo(gift);
        gift.setPrivacy(privacidadComboBox.getSelectionModel().getSelectedItem());
    }
}
