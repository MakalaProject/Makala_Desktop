package org.example.controllers.parent.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.example.model.Step;

import java.net.URL;
import java.util.ResourceBundle;

public class ManualParentController implements Initializable {
    @FXML protected TextField nombreField;
    @FXML protected ComboBox<String> tipoComboBox;
    @FXML protected ListView<Step> stepListView;
    @FXML protected FontAwesomeIconView updateButton;


    protected ObservableList<Step> manualSteps = FXCollections.observableArrayList();
    protected ObservableList<String> typeItems = FXCollections.observableArrayList("Regalo", "Producto", "Actividad");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tipoComboBox.getItems().addAll(typeItems);
        tipoComboBox.setValue("Regalo");
    }

}
