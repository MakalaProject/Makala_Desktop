package org.example.controllers.elements.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.Data;
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
import java.util.List;
import java.util.ResourceBundle;

public class SelectListGifts implements Initializable {
    @FXML
    ListView<Gift> giftListView;
    @FXML
    TextField searchField;
    @FXML
    FontAwesomeIconView saveButton;
    private Catalog catalog = new Catalog();
    FilteredList<Gift> filteredGifts;
    private ArrayList<Gift> checkedGifts = new ArrayList<>();
    List<CheckedFullItem> listFullItems = new ArrayList<>();
    boolean validation = false;
    private final ObservableList<Gift> gifts = FXCollections.observableList(Request.getJ("gifts/criteria-basic", Gift[].class, true));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchField.requestFocus();
        giftListView.setCellFactory(CheckBoxListCell.forListView(new Callback<Gift, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(Gift gift) {
                BooleanProperty observable = new SimpleBooleanProperty();
                listFullItems.add(new CheckedFullItem(observable, gift.getIdGift()));
                observable.addListener((obs, wasSelected, isNowSelected) -> {
                    if (isNowSelected) {
                        checkedGifts.add(gift);
                    } else {
                        checkedGifts.remove(gift);
                    }
                });
                return observable;
            }
        }));

        giftListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        giftListView.setItems(gifts);
        filteredGifts = new FilteredList<>(gifts, p ->true);
        saveButton.setOnMouseClicked(mouseEvent -> {
            new ListToChangeTools<Gift,Integer>().setToDeleteItems(catalog.getGifts(), checkedGifts);
            catalog.setGifts(new ArrayList<>(checkedGifts));
            /*Node source = (Node)  mouseEvent.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();
            stage.setUserData(catalog);
            stage.close();*/
        });
        searchField.textProperty().addListener((observable, oldValue, newValue) ->{
            validation = true;
            //updateItems();
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
        if(catalog.getGifts().size() > 0 && catalog.getGifts().get(0) != null) {
            //checkItems(new ArrayList<>(catalog.getGifts()));
        }
        this.catalog.setGifts(new ArrayList<>(catalog.getGifts()));
    }


    private void checkItems(ArrayList<Gift> gifts){
        for (Gift g : giftListView.getItems()) {
            for (Gift g1 : gifts) {
                if (g.getId().equals(g1.getId())) {
                    for(CheckedFullItem c : listFullItems){
                        boolean validation = false;
                        if(c.getIdGift() == g.getId()){
                            validation = true;
                        }
                        if(validation) {
                            c.getBooleanProperty().setValue(true);
                            validation = false;
                        }
                    }
                }
            }
        }
    }

    @Data
    class CheckedFullItem{
        private BooleanProperty booleanProperty;
        private int idGift;
        public CheckedFullItem(BooleanProperty booleanProperty, int idGift){
            this.idGift = idGift;
            this.booleanProperty = booleanProperty;
        }
    }
}
