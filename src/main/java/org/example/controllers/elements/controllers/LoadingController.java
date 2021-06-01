package org.example.controllers.elements.controllers;

import animatefx.animation.Bounce;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class LoadingController implements Initializable {
    @FXML Circle circle1, circle2, circle3;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Bounce(circle1).setCycleDuration(4).setCycleCount(50).setDelay(Duration.valueOf("300ms")).play();
        new Bounce(circle2).setCycleDuration(4).setCycleCount(50).setDelay(Duration.valueOf("600ms")).play();
        new Bounce(circle3).setCycleDuration(4).setCycleCount(50).setDelay(Duration.valueOf("900ms")).play();
    }
}
