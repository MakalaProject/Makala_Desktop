package org.example.customDialogs;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.interfaces.IControllerCreate;
import org.example.model.Client;
import org.example.services.Request;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ClientCreateController implements Initializable, IControllerCreate<Client> {
    @FXML FontAwesomeIconView saveButton;
    @FXML TextField nombresField;
    @FXML TextField apellidosField;
    @FXML TextField telefonoField;
    @FXML TextField correoField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        saveButton.setOnMouseClicked(mouseEvent -> {
            if(nombresField.getText().isEmpty() ||  telefonoField.getText().isEmpty() ){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Campos vacios");
                alert.setHeaderText("No puedes dejar campos 'Nombre' y 'Telefono' sin llenar'");
                alert.setContentText("Por favor completa los datos ");
                Optional<ButtonType> result = alert.showAndWait();
            }else{
                Client returnedClient = (Client) Request.postJ("clients", setInfo(new Client()));
                Node source = (Node)  mouseEvent.getSource();
                Stage stage  = (Stage) source.getScene().getWindow();
                stage.close();
                stage.setUserData(returnedClient);
            }
        });

    }


    @Override
    public Client setInfo(Client client) {
        client.setFirstName(nombresField.getText());
        client.setLastName(apellidosField.getText());
        client.setPhone(telefonoField.getText());
        client.setMail(correoField.getText());
        return client;
    }
}
