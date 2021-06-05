package org.example.controllers.elements.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import org.controlsfx.control.CheckListView;
import org.example.interfaces.ListToChangeTools;
import org.example.model.Catalog;
import org.example.model.Department;
import org.example.model.Gift;
import org.example.services.Request;

import java.net.URL;
import java.util.ArrayList;

import java.util.List;
import java.util.ResourceBundle;

public class SelectListGifts implements Initializable {
    @FXML
    CheckListView<Gift> checkListView;
    @FXML
    Label titleLabel;
    @FXML
    FontAwesomeIconView saveButton;
    private Catalog catalog = new Catalog();
    FilteredList<Gift> filteredGifts;
    private final ObservableList<Gift> gifts = FXCollections.observableList(Request.getJ("gifts/criteria-general?size=1000", Gift[].class, true));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        checkListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        checkListView.setItems(gifts);
        filteredGifts = new FilteredList<>(gifts, p ->true);
        saveButton.setOnMouseClicked(mouseEvent -> {
            List<Gift> gifts = new ArrayList<>(checkListView.getCheckModel().getCheckedItems());
            new ListToChangeTools<Gift,Integer>().setToDeleteItems(catalog.getGifts(), gifts);
            catalog.setGifts(new ArrayList<>(gifts));
            Node source = (Node)  mouseEvent.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();
            stage.setUserData(catalog);
            stage.close();
        });
        titleLabel.setText("Regalos");
    }



    public void setGiftsList(Catalog catalog){
        if(catalog.getGifts().size() > 0 && catalog.getGifts().get(0) != null) {
            checkItems(new ArrayList<>(catalog.getGifts()));
        }
        this.catalog.setGifts(new ArrayList<>(catalog.getGifts()));
    }


    private void checkItems(ArrayList<Gift> gifts){
        for (Gift g : checkListView.getItems()) {
            for (Gift g1 : gifts) {
                if (g.getId().equals(g1.getId())) {
                    checkListView.getCheckModel().check(g);
                }
            }
        }
    }


}
