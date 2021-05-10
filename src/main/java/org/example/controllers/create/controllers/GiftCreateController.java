package org.example.controllers.create.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url,resourceBundle);
        actualGift.setContainer(containerProducts.get(0));
        container = actualGift.getContainer();
        containerName.setText(container.getName());
        containerExtended = (BoxProduct)Request.find("products/boxes",actualGift.getContainer().getIdProduct(),BoxProduct.class);
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
                        newGift = gift;
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
                        stage.setUserData(newGift);
                    }
                    stage.close();
            }else{
                showAlertEmptyFields("No puedes dejar campos indispensables vacios");
            }
        });
    }
}
