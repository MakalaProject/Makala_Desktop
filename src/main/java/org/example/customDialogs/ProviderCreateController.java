package org.example.customDialogs;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.interfaces.IControllerCreate;
import org.example.model.products.ProductClassDto;
import org.example.model.Provider;
import org.example.services.ProviderService;
import org.example.services.Request;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProviderCreateController implements Initializable, IControllerCreate<Provider> {
    @FXML TextField nombresField;
    @FXML FontAwesomeIconView saveButton;
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

    final ToggleGroup returnRadioGroup = new ToggleGroup();
    private static final ObservableList<ProductClassDto> productClassObservableList = FXCollections.observableArrayList(Request.getJ("classifications/products", ProductClassDto[].class, false));
    private static final ObservableList<String> typeItems = FXCollections.observableArrayList("Emprendedor","Artesano","Comunidad","Empresa");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tipoComboBox.getItems().addAll(typeItems);
        siRadio.setToggleGroup(returnRadioGroup);
        noRadio.setToggleGroup(returnRadioGroup);
        classificationComboBox.itemsProperty().setValue(productClassObservableList);

        saveButton.setOnMouseClicked(mouseEvent -> {
            if (nombresField.getText().isEmpty() || tiempoField.getText().isEmpty() || telefonoField.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Campos vacios");
                alert.setHeaderText("No puedes dejar campos 'Nombre', 'Telefono' y 'Tiempo' sin llenar'");
                alert.setContentText("Por favor completa los datos ");
                Optional<ButtonType> result = alert.showAndWait();
            }else{
                Provider provider = new Provider();
                provider.setFirstName(nombresField.getText());
                provider.setProductClass(classificationComboBox.getSelectionModel().getSelectedItem());
                provider.setLastName(apellidosField.getText());
                provider.setPhone(telefonoField.getText());
                provider.setCardNumber(tarjetaField.getText());
                provider.setClabe(claveField.getText());
                provider.setType(tipoComboBox.getSelectionModel().getSelectedItem());
                boolean isReturn = siRadio.isSelected();
                provider.setProductReturn(isReturn);
                Provider returnedProvider = ProviderService.InsertProvider(provider);
                Node source = (Node)  mouseEvent.getSource();
                Stage stage  = (Stage) source.getScene().getWindow();
                stage.close();
                stage.setUserData(returnedProvider);
            }
        });
    }

    @Override
    public Provider setInfo(Provider object) {
        return null;
    }
}
