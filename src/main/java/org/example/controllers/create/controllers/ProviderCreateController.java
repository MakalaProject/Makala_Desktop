package org.example.controllers.create.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.controllers.elements.controllers.SelectListProduct;
import org.example.controllers.parent.controllers.UserGenericController;
import org.example.customCells.InternalListViewCell;
import org.example.model.Adress.Address;
import org.example.model.Adress.City;
import org.example.model.ChangedVerificationFields;
import org.example.model.FocusVerificationFields;
import org.example.model.products.Product;
import org.example.model.products.ProductClassDto;
import org.example.model.Provider;
import org.example.services.Request;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ProviderCreateController extends UserGenericController<Provider> {
    @FXML TextField tiempoField;
    @FXML TextField tarjetaField;
    @FXML TextField addressField;
    @FXML ComboBox<City> ciudadComboBox;
    @FXML ListView<Product> productsListView;
    @FXML TextField codigoPostalField;
    @FXML TextField emailField;
    @FXML ComboBox<String> tipoComboBox;
    @FXML ComboBox<ProductClassDto> classificationComboBox;
    @FXML RadioButton siRadio;
    @FXML RadioButton noRadio;
    @FXML FontAwesomeIconView productsButton;

    final ToggleGroup returnRadioGroup = new ToggleGroup();
    Provider provider = new Provider();
    ObservableList<Product> products = FXCollections.observableArrayList();
    private static final ObservableList<ProductClassDto> productClassObservableList = FXCollections.observableArrayList(Request.getJ("classifications/products", ProductClassDto[].class, false));
    private static final ObservableList<String> typeItems = FXCollections.observableArrayList("Emprendedor","Artesano","Comunidad","Empresa");
    private static final ObservableList<City> citysList = FXCollections.observableArrayList(Request.getJ("cities?idState=1", City[].class, false));


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url,resourceBundle);
        tiempoField.textProperty().addListener(new ChangedVerificationFields(tiempoField,false,2));
        tiempoField.focusedProperty().addListener(new FocusVerificationFields(tiempoField,false,2));
        tarjetaField.textProperty().addListener(new ChangedVerificationFields(tarjetaField,false,16));
        tarjetaField.focusedProperty().addListener(new FocusVerificationFields(tarjetaField,false,16));
        codigoPostalField.textProperty().addListener(new ChangedVerificationFields(codigoPostalField, false, 5));
        tipoComboBox.getItems().addAll(typeItems);
        siRadio.setToggleGroup(returnRadioGroup);
        noRadio.setToggleGroup(returnRadioGroup);
        classificationComboBox.getItems().addAll(productClassObservableList);
        ciudadComboBox.getItems().addAll(citysList);
        siRadio.setSelected(true);
        classificationComboBox.getSelectionModel().select(0);
        tipoComboBox.getSelectionModel().select(0);
        ciudadComboBox.getSelectionModel().select(0);

        classificationComboBox.valueProperty().addListener(new ChangeListener<ProductClassDto>() {
            @Override
            public void changed(ObservableValue<? extends ProductClassDto> observableValue, ProductClassDto productClassDto, ProductClassDto t1) {
                provider.setProductClassDto(t1);
            }
        });

        updateButton.setOnMouseClicked(mouseEvent -> {
            checkFields();
            if (!nombresField.getText().isEmpty() && !tiempoField.getText().isEmpty() && !telefonoField.getText().isEmpty()){
                if(telefonoField.getText().length()>9 && Integer.parseInt(tiempoField.getText()) >0) {
                    if (products.size()>0) {
                        if ((addressField.getText().isEmpty() && codigoPostalField.getText().isEmpty()) || (!addressField.getText().isEmpty() && !codigoPostalField.getText().isEmpty())) {
                            if (emailField.getText().isEmpty() || (!emailField.getText().isEmpty() && emailField.getText().matches("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b"))) {
                                setInfo(provider);
                                Provider returnedProvider = null;
                                try {
                                    returnedProvider = (Provider) Request.postJ(provider.getRoute(), provider);
                                } catch (Exception e) {
                                    duplyElementAlert(provider.getIdentifier());
                                    return;
                                }
                                Node source = (Node) mouseEvent.getSource();
                                Stage stage = (Stage) source.getScene().getWindow();
                                stage.close();
                                stage.setUserData(returnedProvider);
                            } else {
                                showAlertEmptyFields("Verifica el formato de la dirección de correo electrónico");
                            }
                        } else {
                            showAlertEmptyFields("Puedes dejar los campos de dirección vacios pero no incompletos");
                        }
                    }else{
                        showAlertEmptyFields("Debes agregar productos");
                    }
                }else {
                    showAlertEmptyFields("Los dígitos del teléfono deben ser 10 y el tiempo de entrega mayor a 0");
                }
            }else {
                showAlertEmptyFields("No puedes dejar campos marcados con * vacios");
            }
        });
        productsButton.setOnMouseClicked(mouseEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/select_list_generic.fxml"));
            try {
                provider.setProductClassDto(classificationComboBox.getSelectionModel().getSelectedItem());
                provider.setProducts(products);
                SelectListProduct dialogController = new SelectListProduct();
                fxmlLoader.setController(dialogController);
                Parent parent = fxmlLoader.load();
                dialogController.setProvider(provider);
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
                Provider providerInstance = (Provider) stage.getUserData();
                if (providerInstance!=null ) {
                    providerInstance.setSelectedProducts();
                    products.setAll(providerInstance.getProducts());
                    showProductsList(products);
                    if (providerInstance.getProducts().size() > 0){
                        classificationComboBox.setDisable(true);
                    }else {
                        classificationComboBox.setDisable(false);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void showProductsList(ObservableList<Product> products){
        productsListView.setItems(FXCollections.observableList(products.stream().filter(l -> !l.isToDelete()).collect(Collectors.toList())));
        productsListView.setPrefHeight(productsListView.getItems().size() * 35 + 2);
        productsListView.setCellFactory(cellList -> new InternalListViewCell<>());
    }
    protected void checkFields() {
        super.checkFields();
        if (tiempoField.getText().isEmpty() || Integer.parseInt(tiempoField.getText()) == 0) {
            tiempoField.setStyle("-fx-background-color: #fea08c; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        } else {
            tiempoField.setStyle("-fx-background-color: #E3DAD8; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }
        if ((addressField.getText().isEmpty() && codigoPostalField.getText().isEmpty()) || (!addressField.getText().isEmpty() && !codigoPostalField.getText().isEmpty())) {
            addressField.setStyle("-fx-background-color: #E3DAD8; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
            codigoPostalField.setStyle("-fx-background-color: #E3DAD8; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");

        }else{
            addressField.setStyle("-fx-background-color: #fea08c; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
            codigoPostalField.setStyle("-fx-background-color: #fea08c; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }
    }

    @Override
    public void setInfo(Provider provider) {
        super.setInfo(provider);
        provider.setProducts(products);
        provider.setMail(emailField.getText());
        provider.setShippingTime(Integer.parseInt(tiempoField.getText()));
        provider.setProductClassDto(classificationComboBox.getSelectionModel().getSelectedItem());
        provider.setCardInfo(tarjetaField.getText());
        provider.setTypeProvider(tipoComboBox.getSelectionModel().getSelectedItem());
        provider.setProductReturn(siRadio.isSelected());
        if(!addressField.getText().isEmpty() && !codigoPostalField.getText().isEmpty()) {
            Address address = new Address(0, addressField.getText(), Integer.parseInt(codigoPostalField.getText()), ciudadComboBox.getValue());
            provider.setAddress(address);
        }

    }
}
