package org.example.customDialogs;

import org.example.interfaces.IControllerCreate;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.example.model.RegexVerificationFields;
import org.example.services.Request;
import org.example.interfaces.IControllerProducts;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.example.model.products.Product;
import org.example.model.products.ProductClassDto;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class ProductCreateController implements Initializable, IControllerCreate<Product> {
    @FXML
    TextField nombreField;
    @FXML
    TextField minField;
    @FXML
    TextField maxField;
    @FXML
    FontAwesomeIconView saveButton;
    @FXML
    TextField precioField;
    @FXML
    ImageView productImage;
    @FXML
    ComboBox<String> privacidadComboBox;
    @FXML
    ComboBox<ProductClassDto> clasificacionComboBox;
    @FXML
    ComboBox<String> tipoComboBox;
    @FXML AnchorPane propertiesAnchorPane;

    private static final ObservableList<String> typeItems = FXCollections.observableArrayList("Fijo","Granel","Comestible","Creado","Caja");
    private static final ObservableList<String> privacyItems = FXCollections.observableArrayList("Publico", "Privado");
    private static final ObservableList<ProductClassDto> classificationItems = FXCollections.observableArrayList(Request.getJ(Request.REST_URL + "classifications/products", ProductClassDto[].class, false));
    private IControllerProducts[] controllers;
    private IControllerProducts actualPropertiesController;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        privacidadComboBox.getItems().addAll(privacyItems);
        tipoComboBox.getItems().addAll(typeItems);
        clasificacionComboBox.itemsProperty().setValue(classificationItems);
        clasificacionComboBox.getSelectionModel().select(0);
        tipoComboBox.getSelectionModel().select(0);
        privacidadComboBox.getSelectionModel().select(0);
        nombreField.setText("Nuevo Producto");

        //Verifications with regex
        maxField.textProperty().addListener(new RegexVerificationFields(maxField, true, 3));
        minField.textProperty().addListener(new RegexVerificationFields(minField, true, 3));
        precioField.textProperty().addListener(new RegexVerificationFields(precioField, true, 4,2));

        tipoComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                for (IControllerProducts controller : controllers) {
                    if (controller.getIdentifier().equals(t1)){
                        actualPropertiesController = controller;
                        changeType(controller);
                    }
                }
            }
        });

        saveButton.setOnMouseClicked(mouseEvent -> {
            if( !nombreField.getText().isEmpty()){
                Product product = (Product) actualPropertiesController.getObject();
                if (product != null){
                    Product newProduct = (Product) Request.postJ(product.getRoute(), setInfo(product));
                    product.setIdProduct(newProduct.getIdProduct());
                    Node source = (Node)  mouseEvent.getSource();
                    Stage stage  = (Stage) source.getScene().getWindow();
                    stage.close();
                    stage.setUserData(product);
                }
            }else{
                showAlertEmptyFields("No puedes dejar campos indispensables vacios");
            }
        });
    }

    public void setControllersArray(IControllerProducts[] controllersArray){
        controllers = controllersArray;
    }

    @Override
    public Product setInfo(Product product){
        product.setName(nombreField.getText());
        product.setPrice(new BigDecimal(precioField.getText()));
        product.setMin(Integer.parseInt(minField.getText()));
        product.setMax(Integer.parseInt(maxField.getText()));
        product.setPrivacy(privacidadComboBox.getSelectionModel().getSelectedItem());
        product.setProductType(tipoComboBox.getSelectionModel().getSelectedItem());
        product.setProductClassDto(clasificacionComboBox.getSelectionModel().getSelectedItem());
        return product;
    }

    private void changeType(IControllerProducts controller) {
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
