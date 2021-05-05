package org.example.controllers.list.controllers;

import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.ToggleSwitch;
import org.example.customCells.CatalogListViewCell;
import org.example.customCells.GiftListViewCell;
import org.example.interfaces.IControllerCreate;
import org.example.interfaces.IListController;
import org.example.interfaces.ListToChangeTools;
import org.example.model.Catalog;
import org.example.model.CatalogClassification;
import org.example.model.Gift;
import org.example.model.GiftProductsToSend;
import org.example.services.CatalogService;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.example.services.Request;

import java.net.URL;
import java.util.ResourceBundle;

public class CatalogController implements Initializable, IListController<Catalog>, IControllerCreate<Catalog> {
    @FXML FontAwesomeIconView updateButton;
    @FXML FontAwesomeIconView deleteButton;
    @FXML FontAwesomeIconView addButton;
    @FXML FontAwesomeIconView giftButton;
    @FXML ListView<Catalog> listView;
    @FXML ListView<Gift> giftListView;
    @FXML TextField searchField;
    @FXML TextField nombreField;
    @FXML ComboBox<CatalogClassification> clasificacionComboBox;
    @FXML ToggleSwitch editSwitch;
    @FXML protected AnchorPane fieldsAnchorPane;
    Catalog actualCatalog = new Catalog();
    private ObservableList<Catalog> catalogObservableList = FXCollections.observableArrayList();
    private ObservableList<CatalogClassification> catalogClassifications = FXCollections.observableArrayList(Request.getJ("classifications/catalogs", CatalogClassification[].class,false));
    private ObservableList<Gift> giftObservableList = FXCollections.observableArrayList();
    private int index;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        catalogObservableList.setAll(Request.getJ("catalogs",Catalog[].class, false));
        clasificacionComboBox.getItems().setAll(catalogClassifications);
        clasificacionComboBox.getSelectionModel().select(0);
        showList(catalogObservableList, listView,CatalogListViewCell.class);
        FilteredList<Catalog> filteredCatalog = new FilteredList<>(FXCollections.observableArrayList(catalogObservableList), p ->true);
        //Check if the list is empty to update the view and show its values at the beggining
        if(!catalogObservableList.isEmpty()){
            listView.getSelectionModel().select(0);
            updateView();
        }
        searchField.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredCatalog.setPredicate(catalog -> {
                if (newValue.isEmpty()){
                    return true;
                }
                String lowerCaseText = newValue.toLowerCase();
                if (catalog.getName().toLowerCase().contains(lowerCaseText)){
                    return true;
                }else{
                    return false;
                }
            });
            SortedList<Catalog> sortedCatalogs = new SortedList<>(filteredCatalog);
            catalogObservableList = FXCollections.observableList(sortedCatalogs);
        } );

        listView.setOnMouseClicked(mouseEvent -> {
            if (listView.getSelectionModel().getSelectedItem() != null && !existChanges()) {
                updateView();
            }
        });

        editSwitch.setOnMouseClicked(mouseEvent -> {
            editView(fieldsAnchorPane, editSwitch, updateButton);
        });

        giftButton.setOnMouseClicked(mouseEvent -> {

        });

        //------------------------------------------------CRUD BUTTONS--------------------------------------------------------------------

        updateButton.setOnMouseClicked(mouseEvent -> {
            updateButton.requestFocus();
            if (actualCatalog != null)
                update();
        });


        addButton.setOnMouseClicked(mouseEvent -> {
            addButton.requestFocus();
            if (!existChanges()){
                add();
            }
        });

        deleteButton.setOnMouseClicked(mouseEvent -> {
            deleteButton.requestFocus();
            deleteAlert("catalogo");
        });

    }
    public void add() {

    }

    @Override
    public void delete() {
        Request.deleteJ( "catalogs", actualCatalog.getIdCatalog());
        if (listView.getItems().size() > 1) {
            catalogObservableList.remove(index);
            listView.getSelectionModel().select(0);
            updateView();
            showList(FXCollections.observableList(catalogObservableList), listView, CatalogListViewCell.class);
        }else {
            actualCatalog = null;
            catalogObservableList.remove(index);
            showList(FXCollections.observableList(catalogObservableList), listView, CatalogListViewCell.class);
            cleanForm();
        }
    }

    @Override
    public void update() {

    }


    @Override
    public boolean existChanges() {
        return false;
    }

    @Override
    public void putFields() {
        nombreField.setText(actualCatalog.getName());
        clasificacionComboBox.getSelectionModel().select(actualCatalog.getCatalogClassification());
        giftListView.getItems().setAll(actualCatalog.getGifts());
    }

    @Override
    public void updateView(){
        actualCatalog = listView.getSelectionModel().getSelectedItem();
        index = catalogObservableList.indexOf(listView.getSelectionModel().getSelectedItem());
        editSwitch.setSelected(false);
        editView(fieldsAnchorPane, editSwitch, updateButton);
        putFields();
    }

    @Override
    public void cleanForm() {
        nombreField.setText("");
        giftListView.getItems().clear();
    }
    @Override
    public void setInfo(Catalog catalog) {
        catalog.setName(nombreField.getText());
        catalog.setCatalogClassification(clasificacionComboBox.getValue());
        new ListToChangeTools<Gift,Integer>().setToDeleteItems(giftObservableList, actualCatalog.getGifts());
    }
}
