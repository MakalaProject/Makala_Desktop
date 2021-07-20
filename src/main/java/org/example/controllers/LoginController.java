package org.example.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

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
        }else{
            UserLogin user = new UserLogin(userField.getText(), passwordField.getText());
            employee = Request.post(user.getRoute(), user, Employee.class);
            boolean access = false;
            if (employee != null){
                for (Department department:employee.getDepartments()) {
                    access = department.getDepartName().equals("Administracion");
                    if(access){
                        break;
                    }
                }
            }
            if(!access){
                alertLabel.setVisible(true);
                alertLabel.setText("ID o contraseña incorrectas");
            }else{
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
                    stage.getIcons().add(new Image(getClass().getResource("/Images/logo.png").toString()));
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
