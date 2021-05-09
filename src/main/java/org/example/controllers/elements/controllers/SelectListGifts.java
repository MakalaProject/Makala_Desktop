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
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class SelectListGifts implements Initializable {
    @FXML
    CheckListView<Gift> giftListView;
    @FXML
    TextField searchField;
    @FXML
    FontAwesomeIconView saveButton;
    private Catalog catalog;
    FilteredList<Gift> filteredGifts;
    private ArrayList<Gift> checkedGifts = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        giftListView.getCheckModel().getCheckedItems().addListener(new ListChangeListener<Gift>() {
            @Override
            public void onChanged(Change<? extends Gift> change) {
                change.next();
                if (change.wasAdded()){
                    checkedGifts.add(change.getAddedSubList().get(0));
                }else {
                    checkedGifts.remove(change.getRemoved().get(0));
                }
            }
        });
        saveButton.setOnMouseClicked(mouseEvent -> {
            new ListToChangeTools<Gift,Integer>().setToDeleteItems(catalog.getGifts(), checkedGifts);
            catalog.setGifts(new ArrayList<>(checkedGifts));
            Node source = (Node)  mouseEvent.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();
            stage.setUserData(catalog);
            stage.close();

        });
        searchField.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredGifts.setPredicate(product -> {
                if (newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseText = newValue.toLowerCase();
                return product.getName().toLowerCase().contains(lowerCaseText);
            });
            if (!filteredGifts.isEmpty()) {
                giftListView.setItems(filteredGifts);
            }
        } );
    }

    public void setGiftsList(Catalog catalog){
        ArrayList<Gift> giftsList = new ArrayList<>();
        giftListView.setItems(FXCollections.observableArrayList(Request.getJ("gifts", Gift[].class, false)));
        if(catalog.getGifts().size() > 0 && catalog.getGifts().get(0) != null) {
            for (Gift g : giftListView.getItems()) {
                for (Gift g1 : catalog.getGifts()) {
                    if (g.getId().equals(g1.getId())) {
                        giftListView.getCheckModel().check(g1);
                        giftsList.add(g1);
                    }
                }
            }
        }
        this.catalog.setGifts(giftsList);
    }
}
