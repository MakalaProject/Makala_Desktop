package org.example.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.image.Image;
import org.example.model.*;
import org.example.services.Request;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

    @FXML
    public void login(ActionEvent event){
        if(userField.getText().isBlank() || passwordField.getText().isBlank()){
            alertLabel.setVisible(true);
            alertLabel.setText("Llena todos los campos!!");
        }else{
            UserLogin user = new UserLogin(userField.getText(), passwordField.getText());
            Employee employee = Request.post(user.getRoute(), user, Employee.class);
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
                try {
                    ((Node)(event.getSource())).getScene().getWindow().hide();
                    Parent rootHome =  FXMLLoader.load(getClass().getResource("/fxml/home.fxml"));
                    Scene scene = new Scene(rootHome);
                    Stage stage = new Stage();
                    stage.getIcons().add(new Image(getClass().getResource("/Images/logo.png").toString()));
                    stage.setScene(scene);
                    stage.setMinHeight(700);
                    stage.setMinWidth(1300);
                    stage.show();
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
