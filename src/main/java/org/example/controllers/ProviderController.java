package org.example.controllers;

import org.example.customCells.ProviderListViewCell;
import org.example.customDialogs.ProviderCreateController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.ToggleSwitch;
import org.example.interfaces.IControllerCreate;
import org.example.interfaces.IListController;
import org.example.model.products.Product;
import org.example.model.products.ProductClassDto;
import org.example.model.Provider;
import org.example.services.ProviderService;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.example.services.Request;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProviderController  implements Initializable, IListController<Provider>, IControllerCreate<Provider> {
    @FXML FontAwesomeIconView  updateButton;
    @FXML FontAwesomeIconView deleteButton;
    @FXML FontAwesomeIconView addButton;
    @FXML ListView<Provider> listView;
    @FXML ListView<Product> productsList;
    @FXML TextField searchField;
    @FXML TextField nombresField;
    @FXML TextField apellidosField;
    @FXML TextField tiempoField;
    @FXML TextField tarjetaField;
    @FXML TextField claveField;
    @FXML TextField telefonoField;
    @FXML TextField emailField;
    @FXML ComboBox<String> tipoComboBox;
    @FXML ComboBox<ProductClassDto> classificationComboBox;
    @FXML RadioButton siRadio;
    @FXML RadioButton noRadio;
    @FXML AnchorPane fieldsAnchorPane;
    @FXML SplitPane principalSplitPane;
    @FXML ToggleSwitch editSwitch;

    final ToggleGroup returnRadioGroup = new ToggleGroup();
    private ObservableList<Provider> providerObservableList;
    private static final ObservableList<ProductClassDto> productClassObservableList = FXCollections.observableArrayList(Request.getJ("classifications/products", ProductClassDto[].class, false));
    private static final ObservableList<String> typeItems = FXCollections.observableArrayList("Emprendedor","Artesano","Comunidad","Empresa");
    Provider actualProvider;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tipoComboBox.getItems().addAll(typeItems);
        siRadio.setToggleGroup(returnRadioGroup);
        noRadio.setToggleGroup(returnRadioGroup);
        providerObservableList = FXCollections.observableList(ProviderService.getProviders());
        classificationComboBox.itemsProperty().setValue(productClassObservableList);
        showListProviders(providerObservableList);
        //Check if the list is empty to update the view and show its values at the beggining
        if(!providerObservableList.isEmpty()){
            listView.getSelectionModel().select(0);
            updateView();
        }
        FilteredList<Provider> filteredProviders = new FilteredList<>(FXCollections.observableArrayList(providerObservableList), p ->true);
        searchField.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredProviders.setPredicate(employee -> {
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
            SortedList<Provider> sortedProviders = new SortedList<>(filteredProviders);
            providerObservableList = FXCollections.observableList(sortedProviders);
            showListProviders(providerObservableList);
        } );

        telefonoField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("^\\\\d{10}$")) {
                    telefonoField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        listView.setOnMouseClicked(mouseEvent -> {
            updateView();
        });

        updateButton.setOnMouseClicked(mouseEvent -> {
            updateProvider(actualProvider);
        });

        editSwitch.setOnMouseClicked(mouseEvent -> {
            //editView();
        });

        principalSplitPane.setOnMouseExited(mouseEvent -> {
            updateView();
        });

        deleteButton.setOnMouseClicked(mouseEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Image image = new Image(getClass().getResource("/Images/delete.png").toString(), 50, 70, false, false);
            ImageView imageView = new ImageView(image);
            alert.setGraphic(imageView);
            alert.setTitle("Eliminar proveedor");
            alert.setHeaderText("Estas a punto de eliminar un proveedor");
            alert.setContentText("¿Seguro quieres eliminarlo?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                ProviderService.DeleteProvider(listView.getSelectionModel().getSelectedItem().getId());
                providerObservableList.remove(listView.getSelectionModel().getSelectedItem());
                showListProviders(providerObservableList);
                updateView();
            }
        });

        addButton.setOnMouseClicked(mouseEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/provider_create.fxml"));
            try {
                Parent parent = fxmlLoader.load();
                ProviderCreateController dialogController = fxmlLoader.<ProviderCreateController>getController();
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                //stage.setMaxHeight(712);
                //stage.setMaxWidth(940);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
                Provider provider = (Provider) stage.getUserData();
                if(provider != null){
                    providerObservableList.add(provider);
                    showListProviders(providerObservableList);
                    listView.getSelectionModel().select(provider);
                    listView.scrollTo(provider);
                    updateView();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    private void updateProvider( Provider provider){
        if (nombresField.getText().isEmpty() || tiempoField.getText().isEmpty() || telefonoField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Campos vacios");
            alert.setHeaderText("No puedes dejar campos 'Nombre', 'Telefono' y 'Tiempo' sin llenar'");
            alert.setContentText("Por favor completa los datos ");
            Optional<ButtonType> result = alert.showAndWait();

        }else{
            provider.setFirstName(nombresField.getText());
            provider.setProductClass(classificationComboBox.getSelectionModel().getSelectedItem());
            provider.setLastName(apellidosField.getText());
            provider.setPhone(telefonoField.getText());
            provider.setCardNumber(tarjetaField.getText());
            provider.setClabe(claveField.getText());
            provider.setType(tipoComboBox.getSelectionModel().getSelectedItem());
            boolean isReturn = siRadio.isSelected();
            provider.setProductReturn(isReturn);
            providerObservableList.set(providerObservableList.indexOf(actualProvider), provider);
            showListProviders(providerObservableList);
            ProviderService.UpdateProvider(provider);
            listView.scrollTo(provider);
            listView.getSelectionModel().select(provider);
            editSwitch.setSelected(false);
            //editView();
        }
    }

    private void showAlertUnsavedProvider(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Image image = new Image(getClass().getResource("/Images/delete.png").toString(), 50, 70, false, false);
        ImageView imageView = new ImageView(image);
        alert.setGraphic(imageView);
        alert.setTitle("Empleado sin guardar");
        alert.setHeaderText("Tienes cambios sin guardar de '" + actualProvider.getFirstName() + "'");
        alert.setContentText("¿Quieres mantener los cambios?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            updateProvider(actualProvider);
        }
    }

    /*@Override
    public void editView(){
        if (editSwitch.isSelected()){
            fieldsAnchorPane.setDisable(false);
        }else {
            fieldsAnchorPane.setDisable(true);
        }
    }*/


    @Override
    public void delete() {

    }

    @Override
    public void update() {

    }

    @Override
    public void add() {

    }

    @Override
    public boolean existChanges() {
        return false;
    }

    @Override
    public void putFields() {

    }

    @Override
    public void updateView(){
        if (!compareProvider()){
            showAlertUnsavedProvider();
        }
        editSwitch.setSelected(false);
        //editView();
        actualProvider = listView.getSelectionModel().getSelectedItem();
        nombresField.setText(actualProvider.getFirstName());
        apellidosField.setText(actualProvider.getLastName());
        telefonoField.setText(actualProvider.getPhone());
        tiempoField.setText(Integer.toString(actualProvider.getShippingTime()));
        emailField.setText(actualProvider.getEmail());
        tarjetaField.setText(actualProvider.getCardNumber());
        tipoComboBox.setValue(actualProvider.getType());
        classificationComboBox.setValue(actualProvider.getProductClass());
        claveField.setText(actualProvider.getClabe());
        classificationComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(ProductClassDto classification) {
                return classification.getClassification();
            }
            @Override
            public ProductClassDto fromString(String string) {
                return classificationComboBox.getItems().stream().filter(classification -> classification.getClassification().equals(string)).findFirst().orElse(null);
            }
        });
        if(actualProvider.isProductReturn()){
            siRadio.setSelected(true);
        }else{
            noRadio.setSelected(true);
        }

    }

    @Override
    public void cleanForm() {

    }

    public void showList(ObservableList List) {

    }

    private boolean compareProvider(){
        if (actualProvider == null){
            return true;
        }
        Provider provider = new Provider();
        provider.setFirstName(nombresField.getText());
        provider.setLastName(apellidosField.getText());
        provider.setProductClass(classificationComboBox.getSelectionModel().getSelectedItem());
        provider.setPhone(telefonoField.getText());
        provider.setCardNumber(tarjetaField.getText());
        provider.setClabe(claveField.getText());
        provider.setShippingTime(Integer.parseInt(tiempoField.getText()));
        provider.setEmail(emailField.getText());
        provider.setType(tipoComboBox.getSelectionModel().getSelectedItem());
        provider.setProductReturn(siRadio.isSelected());
        return provider.CompareProvider(actualProvider);
    }

    private void showListProviders(ObservableList<Provider> providersList){
        listView.setItems(providersList);
        listView.setCellFactory(employeeListView -> new ProviderListViewCell());
    }

    @Override
    public Provider setInfo(Provider object) {
        return null;
    }
}
