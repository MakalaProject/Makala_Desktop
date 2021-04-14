package org.example.controllers.list.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.controllers.elements.controllers.SelectListProduct;
import org.example.controllers.parent.controllers.UserParentController;
import org.example.customCells.UserListViewCell;
import org.example.model.Adress.Address;
import org.example.model.Adress.City;
import org.example.model.products.Product;
import org.example.model.products.ProductClassDto;
import org.example.model.Provider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.services.Request;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProviderController extends UserParentController<Provider> {
    @FXML ListView<Product> productsListView;
    @FXML TextField tiempoField;
    @FXML TextField tarjetaField;
    @FXML TextField addressField;
    @FXML TextField claveField;
    @FXML TextField emailField;
    @FXML ComboBox<City> ciudadComboBox;
    @FXML TextField codigoPostalField;
    @FXML ComboBox<String> tipoComboBox;
    @FXML ComboBox<ProductClassDto> classificationComboBox;
    @FXML RadioButton siRadio;
    @FXML FontAwesomeIconView productsButton;
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
                    showList(filteredUsers, listView, UserListViewCell.class);
                }
            }
        } );

        productsButton.setOnMouseClicked(mouseEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/select_list_generic.fxml"));
            try {
                SelectListProduct dialogController = new SelectListProduct();
                fxmlLoader.setController(dialogController);
                Parent parent = fxmlLoader.load();
                dialogController.setProvider(actualUser);
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
                Provider provider = (Provider) stage.getUserData();
                if (provider!=null) {
                    actualUser.setProducts(provider.getProducts());
                    Request.putJ(provider.getRoute(), actualUser);
                    provider.setSelectedProducts();
                    userObservableList.set(userObservableList.indexOf(actualUser), provider);
                    listView.getSelectionModel().select(provider);
                    listView.scrollTo(provider);
                    updateView();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        addButton.setOnMouseClicked(mouseEvent -> {
            add("/fxml/provider_create.fxml",listView,userObservableList, UserListViewCell.class);
        });

    }
    public void showProductsList(Provider provider){
        if (provider.getProducts() != null){
            productsListView.setItems(FXCollections.observableArrayList(provider.getProducts()));
        }

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
            addressField.setText(actualUser.getAddress().getAddress());
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
        addressField.setText("");

    }

    @Override
    public void setInfo(Provider provider) {
        super.setInfo(provider);
        provider.setProductClassDto(classificationComboBox.getSelectionModel().getSelectedItem());
        provider.setCardNumber(tarjetaField.getText());
        provider.setClabe(claveField.getText());
        provider.setTypeProvider(tipoComboBox.getSelectionModel().getSelectedItem());
        provider.setProductReturn(siRadio.isSelected());
        Address address = new Address(0, addressField.getText(),Integer.parseInt(codigoPostalField.getText()),ciudadComboBox.getValue());
        provider.setAddress(address);
    }
}
