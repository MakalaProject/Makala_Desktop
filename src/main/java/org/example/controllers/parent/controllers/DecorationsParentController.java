package org.example.controllers.parent.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.model.Bow;
import org.example.model.CatalogClassification;
import org.example.model.Gift;
import org.example.services.Request;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class DecorationsParentController implements Initializable {
    @FXML
    protected ListView<Bow> decorationListView;
    @FXML protected FontAwesomeIconView updateButton;
    @FXML protected FontAwesomeIconView imageButton;
    @FXML protected FontAwesomeIconView deletePicture;
    @FXML protected TextField nameField;
    @FXML protected TextArea descriptionTextArea;
    @FXML protected TextField priceField;
    @FXML protected ImageView decorationImage;

    protected String imageFile;
    protected ObservableList<Bow> decorationObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
            decorationImage.setImage(new Image(getClass().getResource("/images/catalog.png").toString()));
            imageFile = null;
        }
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
            decorationImage.setImage(img);
        }
    }
    protected void checkFields(){
        if (nameField.getText().isEmpty()) {
            nameField.setStyle("-fx-background-color: #fea08c; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }else{
            nameField.setStyle("-fx-background-color: #E3DAD8; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }
        if (descriptionTextArea.getText().isEmpty()) {
            descriptionTextArea.setStyle("-fx-background-color: #fea08c; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }else{
            descriptionTextArea.setStyle("-fx-background-color: #E3DAD8; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }
        if (priceField.getText().isEmpty() || Integer.parseInt(priceField.getText()) == 0) {
            priceField.setStyle("-fx-background-color: #fea08c; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }else{
            priceField.setStyle("-fx-background-color: #E3DAD8; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }
    }

}
