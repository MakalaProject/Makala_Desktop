package org.example.controllers.parent.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
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
    @FXML protected VBox propertiesVBox;
    @FXML protected AnchorPane disableImageAnchorPane;
    @FXML protected AnchorPane disableInfoAnchorPane;
    @FXML protected FontAwesomeIconView deletePicture;
    @FXML protected FontAwesomeIconView nextPicture;
    @FXML protected FontAwesomeIconView previousPicture;
    @FXML protected Label stockLabel;
    @FXML protected Label minLabel;
    @FXML protected Label maxLabel;
    protected Product product;


    protected static final ObservableList<String> typeItems = FXCollections.observableArrayList("Fijo","Contenedores","Granel","Comestible", "Papeles", "Listones","Creados");
    protected static final ObservableList<String> privacyItems = FXCollections.observableArrayList( "Privado","Publico", "Premium");
    protected static final ObservableList<ProductClassDto> classificationItems = FXCollections.observableArrayList(Request.getJ( "classifications/products", ProductClassDto[].class, false));
    protected static final IControllerProducts[] propertiesControllers = {new StaticProductController(), new BoxProductController(), new PaperProductController(), new BulkProductController(), new CraftedProductController()};

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
                String privacy = "Privado";
                if(actualPropertiesController != null) {
                    product = (Product) actualPropertiesController.getObject();
                    if (product != null) {
                        privacy = product.getPrivacy() == null ? "Privado" : product.getPrivacy();
                    }
                }
                if (userClicked && (privacidadComboBox.getValue().equals("Publico") || privacidadComboBox.getValue().equals("Premium")) && privacy.equals("Privado")){
                    userClicked = false;
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Cuidado");
                    alert.setHeaderText("Producto no editable");
                    alert.setContentText("Una vez establecida esta privacidad, no podras editar las propiedades, clasificaci??n o nombre de este producto");
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
        maxField.focusedProperty().addListener(new FocusVerificationFields(maxField, false, 3));
        minField.focusedProperty().addListener(new FocusVerificationFields(minField, false, 3));
        stockField.focusedProperty().addListener(new FocusVerificationFields(stockField, true, 3, 2));
        stockField.textProperty().addListener(new ChangedVerificationFields(stockField, true, 3, 2));
        precioField.focusedProperty().addListener(new FocusVerificationFields(precioField, true, 4,2));
        maxField.textProperty().addListener(new ChangedVerificationFields(maxField, false, 3));
        minField.textProperty().addListener(new ChangedVerificationFields(minField, false, 3));
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

    @Override
    public FontAwesomeIconView getDeletButton() {
        return deletePicture;
    }

    public void setInfo(Product product){
        product.setName(nombreField.getText());
        product.setRealStock(new BigDecimal(stockField.getText()));
        product.setMin(Integer.parseInt(minField.getText()));
        product.setMax(Integer.parseInt(maxField.getText()));
        product.setPrice(new BigDecimal(!precioField.getText().isEmpty() ? precioField.getText() : "0"));
        product.setProductClassDto(clasificacionComboBox.getSelectionModel().getSelectedItem());
        product.setPrivacy(privacidadComboBox.getSelectionModel().getSelectedItem());
        product.setPictures(new ArrayList<>(pictureList));
    }

    protected void changeType(IControllerProducts controller) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(controller.getResource()));
            fxmlLoader.setController(controller);
            Parent root = fxmlLoader.load();
            propertiesVBox.getChildren().setAll(root);
            AnchorPane.setTopAnchor(root,0D);
            AnchorPane.setBottomAnchor(root,0D);
            AnchorPane.setRightAnchor(root,0D);
            AnchorPane.setLeftAnchor(root,0D);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void checkFields(){

        if (nombreField.getText().isEmpty()) {
            nombreField.setStyle("-fx-background-color: #fea08c; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }else{
            nombreField.setStyle("-fx-background-color: #E3DAD8; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }
        if (minField.getText().isEmpty() || Integer.parseInt(minField.getText())==0) {
            minField.setStyle("-fx-background-color: #fea08c; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }else{
            minField.setStyle("-fx-background-color: #E3DAD8; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }
        if (maxField.getText().isEmpty() || Integer.parseInt(maxField.getText())==0) {
            maxField.setStyle("-fx-background-color: #fea08c; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }else{
            maxField.setStyle("-fx-background-color: #E3DAD8; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }
        if (precioField.getText().isEmpty() || Float.parseFloat(precioField.getText())==0) {
            precioField.setStyle("-fx-background-color: #fea08c; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }else{
            precioField.setStyle("-fx-background-color: #E3DAD8; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }
        if (stockField.getText().isEmpty()) {
            stockField.setStyle("-fx-background-color: #fea08c; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }else{
            stockField.setStyle("-fx-background-color: #E3DAD8; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }
        if (!minField.getText().isEmpty() && !maxField.getText().isEmpty()) {
            if (Integer.parseInt(minField.getText()) >= Integer.parseInt(maxField.getText())) {
                minField.setStyle("-fx-background-color: #fea08c; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
                maxField.setStyle("-fx-background-color: #fea08c; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
            } else {
                minField.setStyle("-fx-background-color: #E3DAD8; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
                maxField.setStyle("-fx-background-color: #E3DAD8; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
            }
        }
    }
}
