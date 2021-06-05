package org.example.controllers.list.controllers;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.ToggleSwitch;
import org.example.controllers.elements.controllers.SelectListDepart;
import org.example.controllers.elements.controllers.SelectListGifts;
import org.example.controllers.parent.controllers.CatalogParentController;
import org.example.customCells.CatalogListViewCell;
import org.example.customCells.GiftListViewCell;
import org.example.interfaces.IControllerCreate;
import org.example.interfaces.IListController;
import org.example.interfaces.ListToChangeTools;
import org.example.model.*;
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
import org.example.model.products.Product;
import org.example.services.ImageService;
import org.example.services.Request;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CatalogController extends CatalogParentController implements Initializable, IListController<Catalog>, IControllerCreate<Catalog> {

    @FXML FontAwesomeIconView deleteButton;
    @FXML FontAwesomeIconView addButton;
    @FXML ListView<Catalog> listView;
    @FXML TextField searchField;
    @FXML ToggleSwitch editSwitch;
    @FXML protected AnchorPane fieldsAnchorPane;
    Catalog actualCatalog = new Catalog();
    private ObservableList<Catalog> catalogObservableList = FXCollections.observableArrayList();
    FilteredList<Catalog> filteredCatalog;
    private int index;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        catalogObservableList.setAll(Request.getJ("catalogs",Catalog[].class, false));
        showList(catalogObservableList, listView,CatalogListViewCell.class);
        filteredCatalog = new FilteredList<>(catalogObservableList, p ->true);
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
            showList(FXCollections.observableArrayList(filteredCatalog), listView, CatalogListViewCell.class);
        } );

        listView.setOnMouseClicked(mouseEvent -> {
            if (listView.getSelectionModel().getSelectedItem() != null && !existChanges()) {
                updateView();
            }
        });

        editSwitch.setOnMouseClicked(mouseEvent -> {
            editView(fieldsAnchorPane, editSwitch, updateButton);
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/catalog_create.fxml"));
        try {
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
            Catalog object = (Catalog) stage.getUserData();
            if(object != null){
                actualCatalog = object;
                catalogObservableList.add(object);
                showList(catalogObservableList, listView, CatalogListViewCell.class);
                listView.getSelectionModel().select(object);
                listView.scrollTo(object);
                updateView();
            }
            if(actualCatalog != null) {
                updateView();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete() {
        try {
            Request.deleteJ( "catalogs", actualCatalog.getIdCatalog());

        } catch (Exception e) {
            errorAlert(e.getMessage());
            return;
        }
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

    protected void checkFields(){
        if (nombreField.getText().isEmpty()) {
            nombreField.setStyle("-fx-background-color: #fea08c; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }else{
            nombreField.setStyle("-fx-background-color: #E3DAD8; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }
    }

    @Override
    public void update() {
        if(!nombreField.getText().isEmpty()){
            Catalog catalog = new Catalog();
            setInfo(catalog);
            try {
                Catalog returnedCatalog = (Catalog) Request.putJ(catalog.getRoute(), catalog);
                if(imageFile != null && !imageFile.equals("") &&!imageFile.contains("http://res.cloudinary.com")) {
                    ArrayList<String> pictures = new ArrayList<>();
                    pictures.add(catalog.getPath());
                    List<String> images = ImageService.uploadImages(pictures);
                    catalog.setPath(images.get(0));
                    imageFile = catalog.getPath();
                    returnedCatalog = (Catalog) Request.putJ(catalog.getRoute(), catalog);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            actualCatalog = catalog;
            actualCatalog.setSelectedGifts();
            catalogObservableList.set(index, actualCatalog);
            listView.getSelectionModel().select(actualCatalog);
            listView.scrollTo(catalog);
            giftListView.getItems().setAll(actualCatalog.getGifts());
            editSwitch.setSelected(false);
            editView(fieldsAnchorPane, editSwitch, updateButton);
        }else {
            checkFields();
            showAlertEmptyFields("Tienes un campo indispensable vacio");
        }
        checkFields();
    }


    @Override
    public boolean existChanges() {
        if(actualCatalog == null){
            return false;
        }
        Catalog catalog = new Catalog();
        setInfo(catalog);
        catalog.setSelectedGifts();
        catalog.sortList();
        actualCatalog.sortList();
        if (!actualCatalog.equals(catalog)){
            if(!showAlertUnsavedElement(catalog.getName(), catalog.getIdentifier())) {
                listView.getSelectionModel().select(catalog);
            }else{
                updateView();
            }
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void putFields() {
        nombreField.setText(actualCatalog.getName());
        imageFile = actualCatalog.getPath();
        if(actualCatalog.getPath() != null && !actualCatalog.getPath().equals("")){
            catalogImage.setImage(new Image(actualCatalog.getPath()));
        }else{
            catalogImage.setImage(new Image(getClass().getResource("/images/catalog.png").toString()));
        }
        clasificacionComboBox.getSelectionModel().select(actualCatalog.getCatalogClassification());
        giftObservableList.setAll(actualCatalog.getGifts());
        giftListView.getItems().setAll(giftObservableList);
    }

    @Override
    public void updateView(){
        actualCatalog = listView.getSelectionModel().getSelectedItem();
        index = catalogObservableList.indexOf(listView.getSelectionModel().getSelectedItem());
        editSwitch.setSelected(false);
        showGiftsList(giftObservableList);
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
        catalog.setGifts(new ArrayList<>(giftObservableList));
        catalog.setPath(imageFile);
        catalog.setIdCatalog(actualCatalog.getIdCatalog());
    }

}
