package org.example.controllers.elements.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.example.model.Adress.City;
import org.example.model.Provider;
import org.example.model.products.Product;
import org.example.model.products.ProductClassDto;
import org.example.services.Request;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ProviderInfo implements Initializable {
    @FXML
    protected TextField nombresField;
    @FXML protected TextField apellidosField;
    @FXML protected TextField telefonoField;
    Provider actualUser;
    @FXML
    ListView<Product> productsListView;
    @FXML TextField tiempoField;
    @FXML TextField tarjetaField;
    @FXML TextField addressField;
    @FXML TextField emailField;
    @FXML
    ComboBox<City> ciudadComboBox;
    @FXML TextField codigoPostalField;
    @FXML ComboBox<String> tipoComboBox;
    private static final ObservableList<Product> productsItems = FXCollections.observableArrayList();
    @FXML ComboBox<ProductClassDto> classificationComboBox;
    @FXML
    RadioButton siRadio;

    @FXML RadioButton noRadio;

    final ToggleGroup returnRadioGroup = new ToggleGroup();
    private static final ObservableList<String> typeItems = FXCollections.observableArrayList("Emprendedor","Artesano","Comunidad","Empresa");
    private static final ObservableList<City> citysList = FXCollections.observableArrayList(Request.getJ("cities?idState=1", City[].class, false));
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tipoComboBox.getItems().addAll(typeItems);
        siRadio.setToggleGroup(returnRadioGroup);
        noRadio.setToggleGroup(returnRadioGroup);
        ciudadComboBox.getItems().addAll(citysList);
        ciudadComboBox.getSelectionModel().select(0);
    }
    public void setObject(Provider provider){
        actualUser = provider;
        putFields();
    }


    public void putFields() {
        nombresField.setText(actualUser.getFirstName());
        apellidosField.setText(actualUser.getLastName());
        telefonoField.setText(actualUser.getPhone());
        productsItems.clear();
        tiempoField.setText(Integer.toString(actualUser.getShippingTime()));
        emailField.setText(actualUser.getMail());
        tarjetaField.setText(actualUser.getCardInfo());
        tipoComboBox.setValue(actualUser.getTypeProvider());
        classificationComboBox.setValue(actualUser.getProductClassDto());
        if(actualUser.getProducts().size() > 0){
            productsItems.setAll(actualUser.getProducts());
        }
        showProductsList(productsItems);
        if(actualUser.isProductReturn()){
            siRadio.setSelected(true);
            noRadio.setSelected(false);
        }else
        {
            siRadio.setSelected(false);
            noRadio.setSelected(true);
        }
        if (actualUser.getAddress() != null){
            codigoPostalField.setText(actualUser.getAddress().getCp().toString());
            ciudadComboBox.setValue(actualUser.getAddress().getCity());
            addressField.setText(actualUser.getAddress().getAddress());
        }else{
            ciudadComboBox.getSelectionModel().clearSelection();
            codigoPostalField.setText("");
            addressField.setText("");
        }
    }

    public void showProductsList(ObservableList<Product> products){
        productsListView.setItems(FXCollections.observableList(products.stream().filter(l -> !l.isToDelete()).collect(Collectors.toList())));
        productsListView.prefHeightProperty().bind(Bindings.size(productsListView.getItems()).multiply(25.7));
    }
}
