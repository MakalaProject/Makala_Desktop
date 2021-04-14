package org.example.controllers.create.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
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
import org.example.controllers.elements.controllers.SelectListDepart;
import org.example.controllers.elements.controllers.SelectListProduct;
import org.example.controllers.parent.controllers.UserGenericController;
import org.example.model.Adress.Address;
import org.example.model.Adress.City;
import org.example.model.Employee;
import org.example.model.products.Product;
import org.example.model.products.ProductClassDto;
import org.example.model.Provider;
import org.example.services.ProviderService;
import org.example.services.Request;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProviderCreateController extends UserGenericController<Provider> {
    @FXML TextField tiempoField;
    @FXML TextField tarjetaField;
    @FXML TextField addressField;
    @FXML TextField claveField;
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
    private static final ObservableList<ProductClassDto> productClassObservableList = FXCollections.observableArrayList(Request.getJ("classifications/products", ProductClassDto[].class, false));
    private static final ObservableList<String> typeItems = FXCollections.observableArrayList("Emprendedor","Artesano","Comunidad","Empresa");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tipoComboBox.getItems().addAll(typeItems);
        siRadio.setToggleGroup(returnRadioGroup);
        noRadio.setToggleGroup(returnRadioGroup);
        classificationComboBox.itemsProperty().setValue(productClassObservableList);

        updateButton.setOnMouseClicked(mouseEvent -> {
            if (!nombresField.getText().isEmpty() || !tiempoField.getText().isEmpty() || !telefonoField.getText().isEmpty()){
                Provider provider = new Provider();
                setInfo(provider);
                Provider returnedProvider = ProviderService.InsertProvider(provider);
                Node source = (Node)  mouseEvent.getSource();
                Stage stage  = (Stage) source.getScene().getWindow();
                stage.close();
                stage.setUserData(returnedProvider);
            }else {
                showAlertEmptyFields("No puedes dejar campos indispensables vacios");
            }
        });
        productsButton.setOnMouseClicked(mouseEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/select_list_generic.fxml"));
            try {
                Provider provider = new Provider();
                provider.setProductClassDto(classificationComboBox.getSelectionModel().getSelectedItem());
                Parent parent = fxmlLoader.load();
                SelectListProduct dialogController = fxmlLoader.<SelectListProduct>getController();
                dialogController.setProvider(provider);
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
                Provider providerInstance = (Provider) stage.getUserData();
                if (providerInstance!=null) {
                    provider.setProducts(providerInstance.getProducts());
                    showProductsList(provider);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void showProductsList(Provider provider){
        if (provider.getProducts() != null){
            productsListView.setItems(FXCollections.observableArrayList(provider.getProducts()));
        }

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
