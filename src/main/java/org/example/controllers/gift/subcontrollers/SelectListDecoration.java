package org.example.controllers.gift.subcontrollers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import org.controlsfx.control.CheckListView;
import org.example.interfaces.ListToChangeTools;
import org.example.model.Decoration;
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
    CheckListView<Decoration> checkListView;

    private ArrayList<Decoration> decorations;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleLabel.setText("Decoraciones");
        checkListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        saveButton.setOnMouseClicked(mouseEvent -> {
            List<Decoration> decorationList = new ArrayList<>(checkListView.getCheckModel().getCheckedItems());
            new ListToChangeTools<Decoration,Integer>().setToDeleteItems(decorations, decorationList);
            Node source = (Node)  mouseEvent.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();
            stage.setUserData(decorationList);
            stage.close();
        });
    }

    public void setDecoration(ArrayList<Decoration> decorations){
        checkListView.getItems().setAll(FXCollections.observableArrayList(Request.getJ("decorations/basics", Decoration[].class, false)));
        for (Decoration decoration : checkListView.getItems()) {
            for (Decoration decoration2 : decorations) {
                if (decoration.getId().equals(decoration2.getId()) && !decoration2.isToDelete()){
                    checkListView.getCheckModel().check(decoration);
                }
            }
        }
        this.decorations = decorations;
    }
}
