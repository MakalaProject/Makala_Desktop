package org.example.controllers.parent.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.controllers.products.subcontrollers.*;
import org.example.interfaces.IControllerCreate;
import org.example.interfaces.IControllerProducts;
import org.example.interfaces.IPictureController;
import org.example.model.ChangedVerificationFields;
import org.example.model.FocusVerificationFields;
import org.example.model.Picture;
import org.example.model.products.Product;
import org.example.model.products.ProductClassDto;
import org.example.services.Request;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public abstract class ProductParentController implements Initializable, IControllerCreate<Product>, IPictureController{
    @FXML protected TextField nombreField;
    @FXML protected TextField minField;
    @FXML protected TextField maxField;
    @FXML protected TextField stockField;
    @FXML protected FontAwesomeIconView updateButton;
    @FXML protected FontAwesomeIconView imageButton;
    @FXML protected FontAwesomeIconView deleteButton;
    @FXML protected FontAwesomeIconView addButton;
    @FXML protected TextField precioField;
    @FXML protected ImageView productImage;
    @FXML protected ComboBox<String> privacidadComboBox;
    @FXML protected ComboBox<ProductClassDto> clasificacionComboBox;
    @FXML protected ComboBox<String> tipoComboBox;
    @FXML protected AnchorPane propertiesAnchorPane;
    @FXML protected AnchorPane fieldsAnchorPane;
    @FXML protected FontAwesomeIconView deletePicture;
    @FXML protected FontAwesomeIconView nextPicture;
    @FXML protected FontAwesomeIconView previousPicture;

    protected static final ObservableList<String> typeItems = FXCollections.observableArrayList("Fijo","Contenedores","Granel","Comestible", "Papeles", "Listones","Creados");
    protected static final ObservableList<String> privacyItems = FXCollections.observableArrayList( "Privado","Publico", "Premium");
    protected static final ObservableList<ProductClassDto> classificationItems = FXCollections.observableArrayList(Request.getJ( "classifications/products", ProductClassDto[].class, false));
    protected static final IControllerProducts[] propertiesControllers = {new StaticProductController(), new BoxProductController(), new RibbonProductController(), new PaperProductController(), new BulkProductController(), new CraftedProductController()};

    protected IControllerProducts actualPropertiesController;
    protected int imageIndex = 0;
    protected List<String> files = new ArrayList<>();
    protected List<String> deleteFiles = new ArrayList<>();
    protected ArrayList<Picture> pictureList = new ArrayList<>();
    protected boolean userClicked = false;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clasificacionComboBox.itemsProperty().setValue(classificationItems);
        privacidadComboBox.getItems().addAll(privacyItems);
        tipoComboBox.getItems().addAll(typeItems);

        imageButton.setOnMouseClicked(mouseEvent -> {
            imageButton.requestFocus();
            Stage s = (Stage)((Node)(mouseEvent.getSource())).getScene().getWindow();
            files = uploadImage(s);
        });

        previousPicture.setOnMouseClicked(mouseEvent -> {
            previousPicture.requestFocus();
            imageIndex--;
            checkIndex();
        });

        nextPicture.setOnMouseClicked(mouseEvent -> {
            nextPicture.requestFocus();
            imageIndex++;
            checkIndex();
        });

        deletePicture.setOnMouseClicked(mouseEvent -> {
            deletePicture.requestFocus();
            files = deletePicture();
        });

        privacidadComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (userClicked && (privacidadComboBox.getValue().equals("Publico") || privacidadComboBox.getValue().equals("Premium"))){
                    userClicked = false;
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Cuidado");
                    alert.setHeaderText("Producto no editable");
                    alert.setContentText("Una vez establecido este producto no podras cambiarlo después");
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



        //Verifications with regex
        maxField.focusedProperty().addListener(new FocusVerificationFields(maxField, true, 3));
        minField.focusedProperty().addListener(new FocusVerificationFields(minField, true, 3));
        stockField.focusedProperty().addListener(new FocusVerificationFields(stockField, false, 3));
        stockField.textProperty().addListener(new ChangedVerificationFields(stockField, false, 3));
        precioField.focusedProperty().addListener(new FocusVerificationFields(precioField, true, 4,2));
        maxField.textProperty().addListener(new ChangedVerificationFields(maxField, true, 3));
        minField.textProperty().addListener(new ChangedVerificationFields(minField, true, 3));
        precioField.textProperty().addListener(new ChangedVerificationFields(precioField, true, 4,2));
    }

    @Override
    public void decreaseIndex() {
        imageIndex--;
    }

    @Override
    public List<String> getFiles() {
        return files;
    }

    @Override
    public FontAwesomeIconView getNextPicture() {
        return nextPicture;
    }
    @Override
    public FontAwesomeIconView getPreviousPicture() {
        return previousPicture;
    }

    @Override
    public List<String> getDeleteFiles() {
        return deleteFiles;
    }

    @Override
    public ImageView getImage() {
        return productImage;
    }

    @Override
    public int getImageIndex() {
        return imageIndex;
    }

    @Override
    public void updateIndex() {
        imageIndex = files.size() -1;
    }

    @Override
    public List<Picture> getPictures() {
        return pictureList;
    }

    public void setInfo(Product product){
        product.setName(nombreField.getText());
        product.setStock(Integer.parseInt(stockField.getText()));
        product.setMax(Integer.parseInt(maxField.getText()));
        product.setMin(Integer.parseInt(minField.getText()));
        product.setPrice(new BigDecimal(precioField.getText()));
        product.setProductClassDto(clasificacionComboBox.getSelectionModel().getSelectedItem());
        product.setPrivacy(privacidadComboBox.getSelectionModel().getSelectedItem());
        product.setPictures(new ArrayList<>(pictureList));
    }

    protected void changeType(IControllerProducts controller) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(controller.getResource()));
            fxmlLoader.setController(controller);
            Parent root = fxmlLoader.load();
            propertiesAnchorPane.getChildren().add(root);
            AnchorPane.setTopAnchor(root,0D);
            AnchorPane.setBottomAnchor(root,0D);
            AnchorPane.setRightAnchor(root,0D);
            AnchorPane.setLeftAnchor(root,0D);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
