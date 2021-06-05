package org.example.controllers.list.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
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
import org.example.model.ChangedVerificationFields;
import org.example.model.FocusVerificationFields;
import org.example.model.products.Product;
import org.example.model.products.ProductClassDto;
import org.example.model.Provider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.services.Request;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ProviderController extends UserParentController<Provider> {
    @FXML ListView<Product> productsListView;
    @FXML TextField tiempoField;
    @FXML TextField tarjetaField;
    @FXML TextField addressField;
    @FXML TextField emailField;
    @FXML ComboBox<City> ciudadComboBox;
    @FXML TextField codigoPostalField;
    @FXML ComboBox<String> tipoComboBox;
    private static final ObservableList<Product> productsItems = FXCollections.observableArrayList();
    @FXML ComboBox<ProductClassDto> classificationComboBox;
    @FXML RadioButton siRadio;
    @FXML FontAwesomeIconView productsButton;
    @FXML RadioButton noRadio;

    final ToggleGroup returnRadioGroup = new ToggleGroup();
    private static final ObservableList<ProductClassDto> productClassObservableList = FXCollections.observableArrayList(Request.getJ("classifications/products", ProductClassDto[].class, false));
    private static final ObservableList<String> typeItems = FXCollections.observableArrayList("Emprendedor","Artesano","Comunidad","Empresa");
    private static final ObservableList<City> citysList = FXCollections.observableArrayList(Request.getJ("cities?idState=1", City[].class, false));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tiempoField.textProperty().addListener(new ChangedVerificationFields(tiempoField,false,2));
        tiempoField.focusedProperty().addListener(new FocusVerificationFields(tiempoField,false,2));
        tarjetaField.textProperty().addListener(new ChangedVerificationFields(tarjetaField,false,16));
        tarjetaField.focusedProperty().addListener(new FocusVerificationFields(tarjetaField,false,16));
        codigoPostalField.textProperty().addListener(new ChangedVerificationFields(codigoPostalField, false, 5));
        tipoComboBox.getItems().addAll(typeItems);
        siRadio.setToggleGroup(returnRadioGroup);
        noRadio.setToggleGroup(returnRadioGroup);
        ciudadComboBox.getItems().addAll(citysList);
        ciudadComboBox.getSelectionModel().select(0);
        userObservableList.addAll(Request.getJ("users/providers",Provider[].class,true));
        userObservableList.sort(Comparator.comparing(Provider::getFirstName));
        filteredUsers = new FilteredList<>(userObservableList, p ->true);
        super.initialize(url,resourceBundle);
        classificationComboBox.itemsProperty().setValue(productClassObservableList);
        classificationComboBox.valueProperty().addListener(new ChangeListener<ProductClassDto>() {
            @Override
            public void changed(ObservableValue<? extends ProductClassDto> observableValue, ProductClassDto productClassDto, ProductClassDto t1) {
                actualUser.setProductClassDto(t1);
            }
        });

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
                    }else if(employee.getProductClassDto().getClassification().toLowerCase().contains(lowerCaseText)) {
                        return true;
                    }else return employee.getProductClassDto().getProductType().toLowerCase().contains(lowerCaseText);

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
                Provider sendProvider = new Provider();
                sendProvider.setProductClassDto(actualUser.getProductClassDto());
                sendProvider.setProducts(new ArrayList<>(productsItems));
                dialogController.setProvider(sendProvider);
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
                Provider provider = (Provider) stage.getUserData();
                if (provider!= null) {
                    productsItems.setAll(provider.getProducts());
                    showProductsList(productsItems);
                    verifyClassification();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        addButton.setOnMouseClicked(mouseEvent -> {
            if (!existChanges()) {
                add("/fxml/provider_create.fxml", listView, userObservableList, UserListViewCell.class);
            }
        });

    }
    public void showProductsList(ObservableList<Product> products){
        productsListView.setItems(FXCollections.observableList(products.stream().filter(l -> !l.isToDelete()).collect(Collectors.toList())));
        productsListView.prefHeightProperty().bind(Bindings.size(productsListView.getItems()).multiply(25.7));
    }

    public void verifyClassification(){
        if (productsListView.getItems().size() > 0 ){
            classificationComboBox.setDisable(true);
        }else {
            classificationComboBox.setDisable(false);
        }
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
    public void update() {
        checkFields();
        if (!tiempoField.getText().isEmpty()){
            if(telefonoField.getText().length()>9 && Integer.parseInt(tiempoField.getText()) >0) {
                if (productsItems.size()>0) {
                    if ((addressField.getText().isEmpty() && codigoPostalField.getText().isEmpty()) || (!addressField.getText().isEmpty() && !codigoPostalField.getText().isEmpty())) {
                        if (emailField.getText().isEmpty() || (!emailField.getText().isEmpty() && emailField.getText().matches("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b"))) {
                            super.update();
                            actualUser.setSelectedProducts();
                            productsItems.setAll(actualUser.getProducts());
                        } else {
                            showAlertEmptyFields("Verifica el formato de la dirección de correo electrónico");
                        }
                    } else {
                        showAlertEmptyFields("Puedes dejar los campos de dirección vacios pero no incompletos");
                    }
                }else {
                    showAlertEmptyFields("Debes agregar productos");
                }
            }else {
                showAlertEmptyFields("El tiempo de entrega debe ser mayor a 0");
            }
        }else{
            showAlertEmptyFields("No puedes dejar campos marcados con * vacios");
        }
    }

    @Override
    protected Provider instanceObject() {
        return new Provider();
    }


    @Override
    public void putFields() {
        super.putFields();
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
        verifyClassification();
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

    @Override
    public void cleanForm() {
        super.cleanForm();
        tiempoField.setText("");
        tarjetaField.setText("");
        emailField.setText("");
        codigoPostalField.setText("");
        addressField.setText("");
    }

    @Override
    public void setInfo(Provider provider) {
        super.setInfo(provider);
        provider.setProductClassDto(classificationComboBox.getSelectionModel().getSelectedItem());
        provider.setCardInfo(tarjetaField.getText());
        provider.setMail(emailField.getText());
        provider.setProductReturn(siRadio.isSelected());
        provider.setShippingTime(Integer.parseInt(tiempoField.getText()));
        provider.setTypeProvider(tipoComboBox.getSelectionModel().getSelectedItem());
        ArrayList<Product> products = new ArrayList<>(productsItems);
        provider.setProducts(products);
        provider.setIdUser(actualUser.getIdUser());
        if(!addressField.getText().isEmpty() && !codigoPostalField.getText().isEmpty()) {
            Address address = new Address(0, addressField.getText(), Integer.parseInt(codigoPostalField.getText()), ciudadComboBox.getValue());
            provider.setAddress(address);
        }
    }
}
