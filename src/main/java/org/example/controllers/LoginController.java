package org.example.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.Optional;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import org.example.exceptions.ProductDeleteException;
import org.example.model.*;
import org.example.services.Request;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.services.TokenSingleton;

public class LoginController implements Initializable {

    @FXML
    private ImageView logoImage;
    @FXML
    private TextField userField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Label alertLabel;
    private Employee employee;

    @FXML
    public void login(ActionEvent event){
        if(userField.getText().isBlank() || passwordField.getText().isBlank()){
            alertLabel.setVisible(true);
            alertLabel.setText("Llena todos los campos!!");
        }else {
            UserLogin user = new UserLogin(userField.getText(), passwordField.getText());
            //Logín and get token

            try {
                employee = Request.postLogin(user.getRoute(), user, Employee.class);
            } catch (Exception e) {
                alertLabel.setVisible(true);
                alertLabel.setText("ID o contraseña incorrectas");
                return;
            }
            //Decode the JWT to get the roles
            String[] chunks = TokenSingleton.getSingleton().getToken().split("\\.");
            Base64.Decoder decoder = Base64.getDecoder();
            String payload = new String(decoder.decode(chunks[1]));
            Payload decodedPayload = new Gson().fromJson(payload, Payload.class);
            TokenSingleton.getSingleton().setPayload(decodedPayload);
            boolean access = false;
            //Verify if the payload contains the "ADMIN" role
            for (Authorities a : decodedPayload.getAuthorities()) {
                if (a.getAuthority().equals("ADMIN")) {
                    access = true;
                    break;
                }
            }
            if(!access){
                alertLabel.setVisible(true);
                alertLabel.setText("ID o contraseña incorrectas");
            }else{
                Employee employee = (Employee) Request.find("users/employees", Integer.parseInt( userField.getText()), Employee.class);
                if (employee.isFirstLogin()){
                    Optional<String> result = null;
                    do{
                        TextInputDialog dialog = new TextInputDialog("Contraseña");
                        dialog.setTitle("Cambio de contraseña");
                        dialog.setHeaderText("Primer inicio de sesión");
                        dialog.setContentText("Contraseña:");
                        result = dialog.showAndWait();
                        if (result.isPresent()){
                            employee.setPassword(result.get());
                            employee.setFirstLogin(false);
                            try {
                                Request.putJ(employee.getRoute(), employee);
                            } catch (ProductDeleteException e) {
                                e.printStackTrace();
                            }
                        }
                    }while (result.isEmpty());
                }
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/home.fxml"));
                    Parent rootHome =  fxmlLoader.load();
                    HomeController homeController = fxmlLoader.getController();
                    homeController.setAdministrator(employee);
                    Scene scene = new Scene(rootHome);
                    Stage stage = new Stage();
                    stage.getIcons().add(new Image(getClass().getResource("/images/logo.png").toString()));
                    stage.setScene(scene);
                    stage.setMinHeight(500);
                    stage.setMinWidth(1000);
                    stage.show();
                    ((Node)(event.getSource())).getScene().getWindow().hide();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {









    }


}
