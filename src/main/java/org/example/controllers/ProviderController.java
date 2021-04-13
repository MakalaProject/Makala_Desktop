package org.example.controllers;

import org.example.customCells.ProviderListViewCell;
import org.example.customDialogs.ProviderCreateController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.model.Adress.Address;
import org.example.model.Adress.City;
import org.example.model.products.Product;
import org.example.model.products.ProductClassDto;
import org.example.model.Provider;
import org.example.services.ProviderService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.example.services.Request;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProviderController extends UserParentController<Provider> {
    @FXML ListView<Product> productsList;
    @FXML TextField tiempoField;
    @FXML TextField tarjetaField;
    @FXML TextField claveField;
    @FXML TextField emailField;
    @FXML ComboBox<City> ciudadComboBox;
    @FXML TextField codigoPostalField;
    @FXML ComboBox<String> tipoComboBox;
    @FXML ComboBox<ProductClassDto> classificationComboBox;
    @FXML RadioButton siRadio;
    @FXML RadioButton noRadio;

    final ToggleGroup returnRadioGroup = new ToggleGroup();
    private static final ObservableList<ProductClassDto> productClassObservableList = FXCollections.observableArrayList(Request.getJ("classifications/products", ProductClassDto[].class, false));
    private static final ObservableList<String> typeItems = FXCollections.observableArrayList("Emprendedor","Artesano","Comunidad","Empresa");
    private static final ObservableList<City> citysList = FXCollections.observableArrayList(Request.getJ("classifications/products", City[].class, false));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tipoComboBox.getItems().addAll(typeItems);
        siRadio.setToggleGroup(returnRadioGroup);
        noRadio.setToggleGroup(returnRadioGroup);
        ciudadComboBox.getItems().addAll(citysList);
        userObservableList.addAll(Request.getJ("users/providers",Provider[].class,true));
        super.initialize(url,resourceBundle);

        classificationComboBox.itemsProperty().setValue(productClassObservableList);

        searchField.textProperty().addListener((observable, oldValue, newValue) ->{
            if (!existChanges()){
                filteredUsers.setPredicate(employee -> {

                    if (newValue.isEmpty()){
                        return true;
                    }
                    String lowerCaseText = newValue.toLowerCase();
                    if (employee.getFirstName().toLowerCase().contains(lowerCaseText)){
                        return true;
                    }else if(employee.getLastName().toLowerCase().contains(lowerCaseText)){
                        return true;
                    }else if(employee.getPhone().toLowerCase().contains(lowerCaseText)){
                        return true;
                    }else{
                        return false;
                    }

                });
                if (!filteredUsers.isEmpty()) {
                    showList(filteredUsers, listView, ProviderListViewCell.class);
                }
            }
        } );


        addButton.setOnMouseClicked(mouseEvent -> {
            add("/fxml/provider_create.fxml",listView,userObservableList,ProviderListViewCell.class);
        });

    }

    @Override
    protected void showList() {
        showList(userObservableList,listView,ProviderListViewCell.class);
    }

    @Override
    public void update() {
        if ( !tiempoField.getText().isEmpty() ) {
            super.update();
        }else{
            showAlertEmptyFields("No puedes dejar campos campos necesarios sin llenar'");
        }
    }

    @Override
    protected Provider instanceObject() {
        return new Provider();
    }


    @Override
    public void putFields() {
        super.putFields();
        tiempoField.setText(Integer.toString(actualUser.getShippingTime()));
        emailField.setText(actualUser.getMail());
        tarjetaField.setText(actualUser.getCardNumber());
        tipoComboBox.setValue(actualUser.getTypeProvider());
        classificationComboBox.setValue(actualUser.getProductClassDto());
        claveField.setText(actualUser.getClabe());
        if (actualUser.getAddress() != null){
            codigoPostalField.setText(actualUser.getAddress().getCp().toString());
            ciudadComboBox.setValue(actualUser.getAddress().getCity());
            //address
        }
    }


    @Override
    public void cleanForm() {
        super.cleanForm();
        tiempoField.setText("");
        tarjetaField.setText("");
        emailField.setText("");
        claveField.setText("");
        codigoPostalField.setText("");
        //address

    }

    @Override
    public void setInfo(Provider provider) {
        provider.setProductClassDto(classificationComboBox.getSelectionModel().getSelectedItem());
        provider.setCardNumber(tarjetaField.getText());
        provider.setClabe(claveField.getText());
        provider.setTypeProvider(tipoComboBox.getSelectionModel().getSelectedItem());
        provider.setProductReturn(siRadio.isSelected());
        Address address = new Address(0, "",Integer.parseInt(codigoPostalField.getText()),ciudadComboBox.getValue());
        provider.setAddress(address);
    }
}
