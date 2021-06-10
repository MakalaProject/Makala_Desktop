package org.example.controllers.parent.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.controllers.elements.controllers.SelectListGifts;
import org.example.customCells.CatalogGiftListViewCell;
import org.example.customCells.CatalogListViewCell;
import org.example.model.Catalog;
import org.example.model.CatalogClassification;
import org.example.model.Gift;
import org.example.services.Request;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CatalogParentController implements Initializable {

    @FXML protected ListView<Gift> giftListView;
    @FXML protected FontAwesomeIconView giftButton;
    @FXML protected FontAwesomeIconView updateButton;
    @FXML protected FontAwesomeIconView imageButton;
    @FXML protected FontAwesomeIconView deletePicture;
    @FXML protected TextField nombreField;
    @FXML protected ImageView catalogImage;
    @FXML protected ComboBox<CatalogClassification> clasificacionComboBox;


    protected String imageFile;
    protected ObservableList<CatalogClassification> catalogClassifications = FXCollections.observableArrayList(Request.getJ("classifications/catalogs", CatalogClassification[].class,false));
    protected ObservableList<Gift> giftObservableList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clasificacionComboBox.getItems().setAll(catalogClassifications);
        clasificacionComboBox.getSelectionModel().select(0);

        giftButton.setOnMouseClicked(mouseEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/select_list_generic.fxml"));
            try {
                SelectListGifts dialogController = new SelectListGifts();
                fxmlLoader.setController(dialogController);
                Parent parent = fxmlLoader.load();
                Catalog sendCatalog = new Catalog();
                sendCatalog.setGifts(new ArrayList<>(giftObservableList));
                dialogController.setGiftsList(sendCatalog);
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
                Catalog catalog = (Catalog) stage.getUserData();
                if (catalog!= null) {
                    giftObservableList.setAll(new ArrayList<>(catalog.getGifts()));
                    showGiftsList(giftObservableList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        //---------------------------------------------------------------IMAGE BUTTONS --------------------------------------------------------------------
        imageButton.setOnMouseClicked(mouseEvent -> {
            imageButton.requestFocus();
            Stage s = (Stage)((Node)(mouseEvent.getSource())).getScene().getWindow();
            uploadImage(s);
        });
        
        deletePicture.setOnMouseClicked(mouseEvent -> {
            deletePicture.requestFocus();
            removePicutre();
        });
    }

    protected void removePicutre() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar imagen");
        alert.setContentText("¿Seguro quieres eliminarla?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            catalogImage.setImage(new Image(getClass().getResource("/images/catalog.png").toString()));
            imageFile = null;
        }
    }


    protected void showGiftsList(ObservableList<Gift> list){
        giftListView.setItems(FXCollections.observableList(list.stream().filter(l -> !l.isToDelete()).collect(Collectors.toList())));
        giftListView.setPrefHeight(giftListView.getItems().size() * 35 + 2);
        giftListView.setCellFactory(cellList -> new CatalogGiftListViewCell());
    }

    protected void uploadImage(Stage s){
        FileChooser fileChooser = new FileChooser();
        //Set extension filter
        FileChooser.ExtensionFilter extFilterJPG
                = new FileChooser.ExtensionFilter("JPG files (*.JPG)", "*.JPG");
        FileChooser.ExtensionFilter extFilterjpg
                = new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter extFilterPNG
                = new FileChooser.ExtensionFilter("PNG files (*.PNG)", "*.PNG");
        FileChooser.ExtensionFilter extFilterpng
                = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        fileChooser.getExtensionFilters()
                .addAll(extFilterJPG, extFilterjpg, extFilterPNG, extFilterpng);
        //Show open file dialog
        File file = fileChooser.showOpenDialog(s);
        if (file != null){
            Image img = new Image(file.toURI().toString());
            imageFile = file.getPath();
            catalogImage.setImage(img);
        }
    }
}
