package org.example.controllers.list.controllers;

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
import org.example.customCells.CatalogListViewCell;
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
import org.example.services.ImageService;
import org.example.services.Request;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
    @FXML ImageView catalogImage;
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/select_checklist_generic.fxml"));
            try {
                SelectListGifts dialogController = new SelectListGifts();
                fxmlLoader.setController(dialogController);
                Parent parent = fxmlLoader.load();
                Catalog sendCatalog = new Catalog();
                sendCatalog.setGifts(new ArrayList<>(giftObservableList));
                dialogController.setGiftsList(sendCatalog);
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
                Catalog catalog = (Catalog) stage.getUserData();
                if (catalog!= null) {
                    giftObservableList.setAll(catalog.getGifts());
                    giftListView.getItems().setAll(giftObservableList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    @Override
    public void update() {
        if(!nombreField.getText().isEmpty()){
            Catalog catalog = new Catalog();
            setInfo(catalog);
            try {
                Gift returnedCatalog = (Gift) Request.putJ(catalog.getRoute(), catalog);
                ArrayList<String> pictures = new ArrayList<>();
                pictures.add(catalog.getPath());
                List<String> images = ImageService.uploadImages(pictures);
                catalog.setPath(images.get(0));
                returnedCatalog = (Gift) Request.putJ(catalog.getRoute(), catalog);
            } catch (Exception e) {
                e.printStackTrace();
            }
            actualCatalog = catalog;
            catalogObservableList.set(index, actualCatalog);
            listView.getSelectionModel().select(actualCatalog);
            listView.scrollTo(catalog);
            actualCatalog.setSelectedGifts();
            giftListView.getItems().setAll(actualCatalog.getGifts());
        }else {
            showAlertEmptyFields("Tienes un campo indispensable vacio");
        }
        editSwitch.setSelected(false);
        editView(fieldsAnchorPane, editSwitch, updateButton);
    }


    @Override
    public boolean existChanges() {
        if(actualCatalog == null){
            return false;
        }
        Catalog catalog = new Catalog();
        setInfo(catalog);
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
        //catalogImage.setImage(new Image(actualCatalog.getPath()));
        clasificacionComboBox.getSelectionModel().select(actualCatalog.getCatalogClassification());
        giftObservableList.setAll(actualCatalog.getGifts());
        giftListView.getItems().setAll(giftObservableList);
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
        catalog.setGifts(giftObservableList);
        catalog.setPath(actualCatalog.getPath());
        catalog.setIdCatalog(actualCatalog.getIdCatalog());
    }

    private void uploadImage(Stage s){
        FileChooser fileChooser = new FileChooser();
        //Set extension filter
        FileChooser.ExtensionFilter extFilterJPG
                = new FileChooser.ExtensionFilter("JPG files (*.JPG)", "*.JPG");
        FileChooser.ExtensionFilter extFilterjpg
                = new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter extFilterPNG
                = new FileChooser.ExtensionFilter("PNG files (*.PNG)", "*.PNG");
        FileChooser.ExtensionFilter extFilterpng
                = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        fileChooser.getExtensionFilters()
                .addAll(extFilterJPG, extFilterjpg, extFilterPNG, extFilterpng);
        //Show open file dialog
        File file = fileChooser.showOpenDialog(s);
        if (file != null){
            Image img = new Image(file.toURI().toString());
            catalogImage.setImage(img);
        }
    }
}
