package org.example.controllers.elements.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.example.model.Notification;

import java.net.URL;
import java.util.ResourceBundle;

public class NotificationController implements Initializable {
    @FXML
    TextField semanaField;
    @FXML TextField mesField;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setObject(Notification notification){
        if(!notification.isWeek3Busy()){
            semanaField.setText("Semana relajada");
        }else {
            semanaField.setText("Alta carga de trabajo");
        }
        if(!notification.isNextMonthBusy()){
            mesField.setText("Mes relajado");
        }else{
            mesField.setText("Alta carga de trabajo");
        }
    }
}
