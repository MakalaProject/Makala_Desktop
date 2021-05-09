package org.example.controllers.elements.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.CheckListView;
import org.controlsfx.control.IndexedCheckModel;
import org.example.interfaces.ListToChangeTools;
import org.example.model.Catalog;
import org.example.model.Department;
import org.example.model.Gift;
import org.example.services.Request;

import java.net.URL;
import java.util.ArrayList;

import java.util.HashSet;
import java.util.ResourceBundle;

public class SelectListGifts implements Initializable {
    @FXML
    CheckListView<Gift> giftListView;
    @FXML
    TextField searchField;
    @FXML
    FontAwesomeIconView saveButton;
    private Catalog catalog = new Catalog();
    FilteredList<Gift> filteredGifts;
    private HashSet<Gift> checkedGifts = new HashSet<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        saveButton.setOnMouseClicked(mouseEvent -> {
            new ListToChangeTools<Gift,Integer>().setToDeleteItems(catalog.getGifts(), checkedGifts);
            catalog.setGifts(new ArrayList<>(checkedGifts));
            Node source = (Node)  mouseEvent.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();
            stage.setUserData(catalog);
            stage.close();

        });
        searchField.textProperty().addListener((observable, oldValue, newValue) ->{
            updateItems();
            filteredGifts.setPredicate(gift -> {
                if (newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseText = newValue.toLowerCase();
                return gift.getName().toLowerCase().contains(lowerCaseText);
            });
            if (!filteredGifts.isEmpty()) {
                giftListView.setItems(filteredGifts);
                checkItems(checkedGifts);
            }
        } );
    }

    public void setGiftsList(Catalog catalog){
        giftListView.setItems(FXCollections.observableArrayList(Request.getJ("gifts/criteria-basic", Gift[].class, true)));
        if(catalog.getGifts().size() > 0 && catalog.getGifts().get(0) != null) {
            checkItems(new HashSet<>(catalog.getGifts()));
        }
        this.catalog.setGifts(new ArrayList<>(catalog.getGifts()));
        filteredGifts = new FilteredList<>(giftListView.getItems(), p ->true);
    }

    private void checkItems(HashSet<Gift> gifts){
        for (Gift g : giftListView.getItems()) {
            for (Gift g1 : gifts) {
                if (g.getId().equals(g1.getId())) {
                    giftListView.getCheckModel().check(g1);
                }
            }
        }
    }
    private void updateItems(){
        for (Gift g : giftListView.getItems()) {
            if (checkedGifts.contains(g)){
                if (!giftListView.getCheckModel().isChecked(g)){
                    checkedGifts.remove(g);
                }
            }else if (giftListView.getCheckModel().isChecked(g)){
                    checkedGifts.add(g);
            }
        }
    }
}
