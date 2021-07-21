package org.example.controllers.create.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.controllers.parent.controllers.GiftParentController;
import org.example.model.*;
import org.example.model.products.BoxProduct;
import org.example.services.ImageService;
import org.example.services.Request;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GiftCreateController extends GiftParentController {
    @FXML protected ComboBox<String> privacidadComboBox;


    protected final ObservableList<String> createItems = FXCollections.observableArrayList( "Privado");
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url,resourceBundle);
        setChargedContainers();
        if(!containerProducts.isEmpty()) {
            actualGift.setContainer(containerProducts.get(0));
            container = actualGift.getContainer();
            containerName.setText(container.getName());
            containerExtended = (BoxProduct) Request.find("products/boxes", actualGift.getContainer().getIdProduct(), BoxProduct.class);
        }
        privacidadComboBox.getItems().addAll(createItems);
        privacidadComboBox.getSelectionModel().select(0);
        updateButton.setOnMouseClicked(mouseEvent -> {
            if( !nombreField.getText().isEmpty() && !laborCostField.getText().isEmpty()){
                if (Float.parseFloat(laborCostField.getText())>0) {
                    Gift gift = new Gift();
                    setExtendedInternalProducts(gift);
                    setInfo(gift);
                    if(gift.getDecorations().size()>0 && gift.getStaticProducts().size()>0) {
                        Gift newGift = null;
                        try {
                            gift.setPictures(new ArrayList<>());
                            newGift = (Gift) Request.postJ(gift.getRoute(), gift);
                            gift.setIdGift(newGift.getIdGift());
                            gift = newGift;
                            ArrayList<Picture> pictures = new ArrayList<>();
                            files = ImageService.uploadImages(files);
                            if (files != null) {
                                for (String s : files) {
                                    pictures.add(new Picture(s));
                                }
                            }
                            newGift.setPictures(new ArrayList<>());
                            newGift.setPictures(pictures);
                            gift.setPapers(null);
                            gift.setDecorations(null);
                            gift.setStaticProducts(null);
                            gift = (Gift) Request.putJ(gift.getRoute(), gift);
                            newGift = gift;
                        } catch (Exception e) {
                            ImageService.deleteImages(files);
                            duplyElementAlert(gift.getIdentifier());
                            return;
                        }
                        Node source = (Node) mouseEvent.getSource();
                        Stage stage = (Stage) source.getScene().getWindow();
                        files = new ArrayList<>();
                        deleteFiles = new ArrayList<>();
                        imageIndex = 0;
                        if (newGift != null) {
                            gift.setIdGift(newGift.getIdGift());
                            newGift.setPrice(gift.getPrice());
                            stage.setUserData(newGift);
                        }
                        stage.close();
                    }else {
                        showAlertEmptyFields("No puedes dejar un regalo sin listones o productos");
                    }
                }else {
                    showAlertEmptyFields("El precio de elaboración no puede ser 0");
                }
            }else{
                checkFields();
                showAlertEmptyFields("No puedes dejar campos indispensables vacios");
            }
            checkFields();
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
    protected void checkFields(){

        if (nombreField.getText().isEmpty()) {
            nombreField.setStyle("-fx-background-color: #fea08c; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }else{
            nombreField.setStyle("-fx-background-color: #E3DAD8; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }

        if (laborCostField.getText().isEmpty() || Float.parseFloat(laborCostField.getText())==0) {
            laborCostField.setStyle("-fx-background-color: #fea08c; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }else{
            laborCostField.setStyle("-fx-background-color: #E3DAD8; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }
    }
    @Override
    public void setInfo(Gift gift){
        super.setInfo(gift);
        gift.setPrivacy(privacidadComboBox.getSelectionModel().getSelectedItem());
    }
}
