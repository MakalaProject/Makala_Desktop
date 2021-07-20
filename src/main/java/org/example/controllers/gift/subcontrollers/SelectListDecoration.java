package org.example.controllers.gift.subcontrollers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import org.controlsfx.control.CheckListView;
import org.controlsfx.control.decoration.Decoration;
import org.example.interfaces.ListToChangeTools;
import org.example.model.Bow;
import org.example.model.Department;
import org.example.model.Employee;
import org.example.services.Request;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SelectListDecoration implements Initializable {
    @FXML
    Label titleLabel;
    @FXML
    FontAwesomeIconView saveButton;
    @FXML
    CheckListView<Bow> checkListView;

    private ArrayList<Bow> decorations;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleLabel.setText("Decoraciones");
        checkListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        saveButton.setOnMouseClicked(mouseEvent -> {
            List<Bow> bowList = new ArrayList<>(checkListView.getCheckModel().getCheckedItems());
            new ListToChangeTools<Bow,Integer>().setToDeleteItems(decorations, bowList);
            Node source = (Node)  mouseEvent.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();
            stage.setUserData(decorations);
            stage.close();
        });
    }

    public void setDecoration(ArrayList<Bow> decorations){
        this.decorations = decorations;
        //checkListView.setItems(FXCollections.observableArrayList(Request.getJ("departments", Department[].class, false)));
        ArrayList<Bow> bows = new ArrayList<>();
        for (Bow bow : checkListView.getItems()) {
            for (Bow bow2 : decorations) {
                if (bow.getId().equals(bow2.getId()) && !bow2.isToDelete()){
                    checkListView.getCheckModel().check(bow);
                    bows.add(bow);
                }
            }
        }


    }
}
