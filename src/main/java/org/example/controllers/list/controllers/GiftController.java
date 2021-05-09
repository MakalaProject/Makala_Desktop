package org.example.controllers.list.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.Rating;
import org.controlsfx.control.ToggleSwitch;
import org.example.controllers.elements.controllers.SelectContainerProduct;
import org.example.controllers.parent.controllers.GiftParentController;
import org.example.customCells.GiftListViewCell;
import org.example.interfaces.*;
import org.example.model.*;
import org.example.model.products.*;
import org.example.services.ImageService;
import org.example.services.Request;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class GiftController extends GiftParentController implements IListController<Gift> {

    @FXML protected FontAwesomeIconView deleteButton;
    @FXML protected FontAwesomeIconView addButton;
    @FXML TextField searchField;
    @FXML ListView<Gift> listView;
    @FXML ToggleSwitch editSwitch;
    private final ObservableList<Gift> giftObservableList = FXCollections.observableArrayList();
    protected static final ObservableList<String> publicGift = FXCollections.observableArrayList( "Oculto", "Premium", "Publico");
    private int index;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        giftObservableList.addAll(Request.getJ("/gifts/criteria",Gift[].class, true));
        super.initialize(url, resourceBundle);
        showList(FXCollections.observableList(giftObservableList), listView, GiftListViewCell.class);
        FilteredList<Gift> filteredGifts = new FilteredList<>(giftObservableList, p ->true);
        searchField.textProperty().addListener((observable, oldValue, newValue) ->{
            if (!existChanges()) {
                filteredGifts.setPredicate(gift -> {
                    if (newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseText = newValue.toLowerCase();
                    return gift.getName().toLowerCase().contains(lowerCaseText);
                });
                if (!filteredGifts.isEmpty()) {
                    showList(FXCollections.observableList(filteredGifts), listView, GiftListViewCell.class);
                }
            }
        } );
        if (listView.getItems().size() > 0 ){
            listView.getSelectionModel().select(0);
            actualGift = listView.getItems().get(0);
            updateView();
        }else {
            actualGift = null;
        }

        listView.setOnMouseClicked(mouseEvent -> {
            if (listView.getSelectionModel().getSelectedItem() != null && !existChanges()) {
                imageIndex = 0;
                updateView();
            }
        });

        editSwitch.setOnMouseClicked(mouseEvent -> {
            editView(fieldsAnchorPane, editSwitch, updateButton);
        });


        //------------------------------------------------CRUD BUTTONS--------------------------------------------------------------------

        updateButton.setOnMouseClicked(mouseEvent -> {
            updateButton.requestFocus();
            if (actualGift != null)
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
            deleteAlert("regalo");
        });
    }



    @Override
    public boolean existChanges(){
        /*if(actualGift == null){
            return false;
        }
        Gift gift = new Gift();
        setInfo(gift);
        if (!actualGift.equals(gift)){
            if(!showAlertUnsavedElement(gift.getName(), gift.getIdentifier())) {
                listView.getSelectionModel().select(gift);
            }else{
                updateView();
            }
            return true;
        }else {
            return false;
        }*/
        return false;
    }

    @Override
    public void delete() {
       try {
            Request.deleteJ( "products", actualGift.getIdGift());
        } catch (Exception e) {
            errorAlert(e.getMessage());
            return;
        }
        deleteFiles = new ArrayList<>();
        for(Picture p: actualGift.getPictures()){
            deleteFiles.add(p.getPath());
        }
        ImageService.deleteImages(deleteFiles);

        if (listView.getItems().size() > 1) {
            giftObservableList.remove(index);
            listView.getSelectionModel().select(0);
            updateView();
            showList(FXCollections.observableList(giftObservableList), listView, GiftListViewCell.class);
        }else {
            actualGift = null;
            giftObservableList.remove(index);
            showList(FXCollections.observableList(giftObservableList), listView, GiftListViewCell.class);
            cleanForm();
        }
    }

    @Override
    public void update() {
        if(!nombreField.getText().isEmpty()){
            actualGift.setPrice(new BigDecimal(1));
            Gift gift = new Gift();
            setInfo(gift);
            Gift returnedGift = null;
            try {
                ArrayList<Picture> picturesOriginal = new ArrayList<>(gift.getPictures());
                gift.setPictures(new ArrayList<>());
                Request.putJ(gift.getRoute(), gift);
                gift = (Gift) Request.find(gift.getRoute(), gift.getIdGift(), gift.getClass());
                List<String> urls = ImageService.uploadImages(files);
                files = urls;
                gift.getPictures().removeIf(p -> !p.getPath().contains("http://res.cloudinary.com"));
                for (String s : urls) {
                    gift.getPictures().add(new Picture(s));
                }
                files = new ArrayList<>();
                if(deleteFiles.size() != 0){
                    int counter = 0;
                    ArrayList<Picture> pictures = new ArrayList<>(picturesOriginal);
                    for (Picture p : pictures) {
                        files.add(p.getPath());
                        for(String s: deleteFiles){
                            if(s.equals(picturesOriginal.get(counter).getPath())){
                                picturesOriginal.remove(counter);
                                counter--;
                            }
                        }
                        counter++;
                    }
                    new ListToChangeTools<Picture,Integer>().setToDeleteItems(actualGift.getPictures(), picturesOriginal);
                    gift.setPictures(picturesOriginal);
                    ImageService.deleteImages(deleteFiles);
                }
                returnedGift = (Gift) Request.putJ(gift.getRoute(), gift);
            } catch (Exception e) {
                duplyElementAlert(gift.getIdentifier());
                return;
            }
            gift.setPictures(returnedGift.getPictures());
            actualGift = gift;
            pictureList = new ArrayList<>(gift.getPictures());
            giftObservableList.set(index, actualGift);
            listView.getSelectionModel().select(actualGift);
            listView.scrollTo(gift);
            actualGift.setSelectedProducts();
            showProductsList();
        }else{
            showAlertEmptyFields("Tienes un campo indispensable vacio");
        }
        editSwitch.setSelected(false);
        editView(fieldsAnchorPane, editSwitch, updateButton);
    }

    public void add(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/gift_create.fxml"));
        try {
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
            Gift object = (Gift) stage.getUserData();
            if(object != null){
                actualGift = object;
                giftObservableList.add(object);
                userClicked = false;
                //comboBox.setValue(object.getProductClassDto().getProductType());
                showList(giftObservableList,listView,GiftListViewCell.class);
                listView.getSelectionModel().select(object);
                listView.scrollTo(object);
                updateView();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void putFields() {
        nombreField.setText(actualGift.getName());
        precioField.setText(actualGift.getPrice().toString());
        laborCostField.setText(actualGift.getLaborPrice().toString());
        containerName.setText(actualGift.getContainer().getName());
        papersObservableList.setAll(actualGift.getPapers());
        container = actualGift.getContainer();
        containerExtended = (BoxProduct)Request.find("products/boxes",container.getIdProduct(),BoxProduct.class);
        ribbonsObservableList.setAll(actualGift.getRibbons());
        papersObservableList.setAll(actualGift.getPapers());
        privacidadComboBox.setValue(actualGift.getPrivacy());
        productsObservableList.setAll(actualGift.getStaticProducts());
        giftRating.setRating(actualGift.getRating());
        giftRating.setOpacity(1);
        showProductsList();
    }

    @Override
    public void updateView() {
        actualGift = listView.getSelectionModel().getSelectedItem();
        index = giftObservableList.indexOf(listView.getSelectionModel().getSelectedItem());
        if(actualGift.getContainer() == null) {
            actualGift = (Gift) Request.find(actualGift.getRoute(), actualGift.getIdGift(), Gift.class);
            if (actualGift.getStaticProducts() != null) {
                ArrayList<Integer> idProducts = new ArrayList<>(actualGift.getStaticProducts().stream().map(p -> p.getId()).collect(Collectors.toList()));
                actualGift.setInternalProducts(Request.postArray("products/statics/find-list",idProducts, StaticProduct[].class));
            }
            containerExtended = (BoxProduct)Request.find("products/boxes",actualGift.getContainer().getIdProduct(),BoxProduct.class);
        }
        giftObservableList.set(index, actualGift);
        editSwitch.setSelected(false);
        editView(fieldsAnchorPane, editSwitch, updateButton);
        privacyProduct();
        putFields();
        files = new ArrayList<>();
        deleteFiles = new ArrayList<>();
        pictureList = new ArrayList<>();
        fillImageList();
        checkIndex();
    }

    private void privacyProduct() {
        if (!actualGift.getPrivacy().equals("Privado")){
            userClicked = false;
            nombreField.setDisable(true);
            privacidadComboBox.getItems().clear();
            privacidadComboBox.getItems().addAll(publicGift);
            containerButton.setDisable(true);
            productsAnchorPane.setDisable(true);
        }else {
            userClicked = false;
            nombreField.setDisable(false);
            privacidadComboBox.getItems().clear();
            privacidadComboBox.getItems().addAll(privacyItems);
            containerButton.setDisable(false);
            productsAnchorPane.setDisable(false);
        }
    }

    @Override
    public void cleanForm() {
        nombreField.setText("");
        precioField.setText("");
        laborCostField.setText("");
        containerName.setText("");
        internalPapersListView.getItems().clear();
        internalRibbonsListView.getItems().clear();
        internalProductsListView.getItems().clear();

    }



    private void fillImageList(){
        if(!actualGift.getPictures().isEmpty()){
            for(Picture p : actualGift.getPictures()){
                files.add(p.getPath());
                pictureList = new ArrayList<>(actualGift.getPictures());
            }
        }
    }

}
