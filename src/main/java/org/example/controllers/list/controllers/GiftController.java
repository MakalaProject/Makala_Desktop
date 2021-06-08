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
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class GiftController extends GiftParentController implements IListController<Gift> {

    @FXML protected FontAwesomeIconView deleteButton;
    @FXML protected FontAwesomeIconView addButton;
    @FXML protected ComboBox<String> privacidadComboBox;
    @FXML TextField searchField;
    @FXML ListView<Gift> listView;
    @FXML ToggleSwitch editSwitch;
    @FXML protected FontAwesomeIconView manualButton;
    private final ObservableList<Gift> giftObservableList = FXCollections.observableArrayList();
    private final ObservableList<Gift> actualList = FXCollections.observableArrayList();
    protected static final ObservableList<String> publicGift = FXCollections.observableArrayList( "Oculto", "Premium", "Publico");
    private int index;
    private ArrayList<Step> stepList = new ArrayList<>();
    ManualController dialogController;
    FilteredList<Gift> filteredGifts;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        giftObservableList.addAll(Request.getJ("/gifts/criteria-basic?size=10000",Gift[].class, true));
        actualList.setAll(giftObservableList);
        super.initialize(url, resourceBundle);
        privacidadComboBox.getItems().addAll(privacyItems);
        privacidadComboBox.getSelectionModel().select(0);
        showList(FXCollections.observableList(giftObservableList), listView, GiftListViewCell.class);
        filteredGifts = new FilteredList<>(actualList, p ->true);
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

        manualButton.setOnMouseClicked(mouseEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/manual.fxml"));
            try {
                dialogController = new ManualController();
                fxmlLoader.setController(dialogController);
                Parent parent = fxmlLoader.load();
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                dialogController.setObject(actualGift, stepList);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
                ArrayList<Step> object = (ArrayList<Step>) stage.getUserData();
                if(object != null){
                    stepList = object;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        privacidadComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                ArrayList<Step> steps = new ArrayList<>();
                for(Step e : stepList){
                    if(!e.isToDelete()){
                        steps.add(e);
                    }
                }
                if(userClicked && steps.isEmpty() && (privacidadComboBox.getValue().equals("Publico") || privacidadComboBox.getValue().equals("Premium"))){
                    userClicked = false;
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setHeaderText("Regalo no publicable");
                    alert.setContentText("No puedes publicar un regalo sin un manual creado con anterioridad");
                    alert.showAndWait();
                    privacidadComboBox.setValue(s);
                }
                if (userClicked && (privacidadComboBox.getValue().equals("Publico") || privacidadComboBox.getValue().equals("Premium"))){
                    userClicked = false;
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Cuidado");
                    alert.setHeaderText("Regalo no editable");
                    alert.setContentText("Una vez establecido este regalo no podras cambiarlo después");
                    alert.showAndWait();
                }
            }
        });

        privacidadComboBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                userClicked = true;
            }
        });
    }

    @Override
    public boolean existChanges(){
        if(actualGift == null){
            return false;
        }
        Gift gift = new Gift();
        setInfo(gift);
        if (!actualGift.equals(gift)){
            if(!showAlertUnsavedElement(actualGift.getName(), gift.getIdentifier())) {
                listView.getSelectionModel().select(gift);
            }else{
                updateView();
            }
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void delete() {
       try {
            Request.deleteJ( "gifts", actualGift.getIdGift());
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
        if(!nombreField.getText().isEmpty() && !laborCostField.getText().isEmpty()){
            if (Float.parseFloat(laborCostField.getText())>0) {
                if(actualGift.getRibbons().size()>0 && actualGift.getStaticProducts().size()>0) {
                    int listIndex = listView.getSelectionModel().getSelectedIndex();
                    Gift gift = new Gift();
                    setInfo(gift);
                    Gift returnedGift = null;
                    try {
                        ArrayList<Picture> picturesOriginal = new ArrayList<>(gift.getPictures());
                        gift.setPictures(new ArrayList<>());
                        if(!actualGift.getPrivacy().equals("Privado")){
                            gift.setStaticProducts(null);
                            gift.setRibbons(null);
                            gift.setPapers(null);
                        }
                        Request.putJ(gift.getRoute(), gift);
                        gift = (Gift) Request.find(gift.getRoute(), gift.getIdGift(), gift.getClass());
                        if(gift.getSteps() != null) {
                            stepList = gift.getSteps();
                            uploadStepImages();
                        }
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
                        if(!gift.getPrivacy().equals("Privado")){
                            gift.setStaticProducts(null);
                            gift.setRibbons(null);
                            gift.setPapers(null);
                        }
                        returnedGift = (Gift) Request.putJ(gift.getRoute(), gift);
                    } catch (Exception e) {
                        duplyElementAlert(gift.getIdentifier());
                        return;
                    }
                    gift.setPictures(returnedGift.getPictures());
                    actualGift = returnedGift;
                    setExtendedInternalProducts(actualGift);
                    pictureList = new ArrayList<>(gift.getPictures());
                    Gift g = listView.getItems().get(listIndex);
                    actualList.set(actualList.indexOf(g), actualGift);
                    if(listView.getItems().get(listIndex) != actualGift) {
                        listView.getItems().set(listIndex, actualGift);
                    }
                    listView.scrollTo(actualGift);
                    giftObservableList.set(index, actualGift);
                    putFields();
                    privacyProduct();
                    editSwitch.setSelected(false);
                    editView(fieldsAnchorPane, editSwitch, updateButton);
                    listView.getSelectionModel().select(listIndex);
                    updateView();
                }else {
                    showAlertEmptyFields("No puedes dejar un regalo sin listones o productos");
                }
            }else {
                showAlertEmptyFields("El precio de elaboración no puede ser 0");
            }
        }else {
            checkFields();
            showAlertEmptyFields("Tienes un campo indispensable vacio");
        }
        checkFields();
    }
    protected void uploadStepImages(){
        ArrayList<Step> newSteps = new ArrayList<>(stepList);
        for(Step s : stepList){
            if(s.getPath()!= null) {
                if (s.getPath().contains("http://res.cloudinary.com")) {
                    continue;
                }
                List<String> file = new ArrayList<>();
                file.add(s.getPath());
                List<String> returnedFiles = ImageService.uploadImages(file);
                s.setPath(returnedFiles.get(0));
            }
        }
    }

    protected void checkFields(){
        if (nombreField.getText().isEmpty()) {
            nombreField.setStyle("-fx-background-color: #fea08c; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }else{
            nombreField.setStyle("-fx-background-color: #E3DAD8; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }
        if (laborCostField.getText().isEmpty() || Float.parseFloat(laborCostField.getText())==0) {
            laborCostField.setStyle("-fx-background-color: #fea08c; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }else{
            laborCostField.setStyle("-fx-background-color: #E3DAD8; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }
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
        actualGift.sortList();
        nombreField.setText(actualGift.getName());
        precioField.setText(actualGift.getPrice().setScale(2, RoundingMode.HALF_EVEN).toString());
        laborCostField.setText(actualGift.getLaborPrice().toString());
        containerName.setText(actualGift.getContainer().getName());
        papersObservableList.setAll(actualGift.getPapers());
        container = actualGift.getContainer();
        privacidadComboBox.setValue(actualGift.getPrivacy());
        stepList = new ArrayList<>(actualGift.getSteps());
        giftRating.setRating(actualGift.getRating());
        giftRating.setOpacity(1);
        ribbonsObservableList.setAll(actualGift.getRibbons());
        papersObservableList.setAll(actualGift.getPapers());
        productsObservableList.setAll(actualGift.getStaticProducts());
        actualProductsObservableList.setAll(actualGift.getStaticProducts());
        actualPapersObservableList.setAll(actualGift.getPapers());
        actualRibbonsObservableList.setAll(actualGift.getRibbons());
        showProductsList();
        checkInternalProducts();
    }

    @Override
    public void updateView() {
        actualGift = listView.getSelectionModel().getSelectedItem();
        index = giftObservableList.indexOf(actualGift);
        if(actualGift.getContainer() == null) {
            actualGift = (Gift) Request.find(actualGift.getRoute(), actualGift.getIdGift(), Gift.class);
            setExtendedInternalProducts(actualGift);
            int listIndex = listView.getSelectionModel().getSelectedIndex();
            actualList.set(actualList.indexOf(listView.getSelectionModel().getSelectedItem()), actualGift);
            if(listView.getItems().get(listIndex) != actualGift) {
                listView.getItems().set(listIndex, actualGift);
            }
            giftObservableList.set(index, actualGift);
        }
        editSwitch.setSelected(false);
        editView(fieldsAnchorPane, editSwitch, updateButton);
        privacyProduct();
        putFields();
        actualGift.setContainer(containerExtended);
        files = new ArrayList<>();
        deleteFiles = new ArrayList<>();
        pictureList = new ArrayList<>();
        fillImageList();
        checkIndex();
    }

    private void privacyProduct() {
        userClicked = false;
        if (!actualGift.getPrivacy().equals("Privado")){
            nombreField.setDisable(true);
            privacidadComboBox.getItems().clear();
            privacidadComboBox.getItems().addAll(publicGift);
            containerButton.setDisable(true);
            productsAnchorPane.setDisable(true);
        }else {
            nombreField.setDisable(false);
            privacidadComboBox.getItems().clear();
            privacidadComboBox.getItems().addAll(privacyItems);
            containerButton.setDisable(false);
            productsAnchorPane.setDisable(false);
        }
        privacidadComboBox.getSelectionModel().select(actualGift.getPrivacy());
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

    @Override
    public void setInfo(Gift gift) {
        super.setInfo(gift);
        gift.setPrivacy(privacidadComboBox.getSelectionModel().getSelectedItem());
        gift.setSteps(stepList);
    }

}
