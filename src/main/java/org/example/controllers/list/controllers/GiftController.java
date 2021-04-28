package org.example.controllers.list.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.ToggleSwitch;
import org.example.controllers.elements.controllers.SelectContainerProduct;
import org.example.controllers.gift.subcontrollers.GiftPaperPropertiesController;
import org.example.controllers.gift.subcontrollers.GiftProductPropertiesController;
import org.example.controllers.products.subcontrollers.InsideProductController;
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
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;


public class GiftController implements IListController<Gift>, Initializable, IPictureController, IControllerCreate<Gift> {

    @FXML
    protected TextField nombreField;
    @FXML protected FontAwesomeIconView updateButton;
    @FXML protected FontAwesomeIconView imageButton;
    @FXML protected FontAwesomeIconView deleteButton;
    @FXML protected FontAwesomeIconView addButton;
    @FXML protected TextField precioField;
    @FXML protected TextField laborCostField;
    @FXML protected ImageView productImage;
    @FXML protected AnchorPane fieldsAnchorPane;
    @FXML protected FontAwesomeIconView deletePicture;
    @FXML protected FontAwesomeIconView nextPicture;
    @FXML protected FontAwesomeIconView productsButton;
    @FXML protected FontAwesomeIconView containerButton;
    @FXML protected FontAwesomeIconView papersButton;
    @FXML protected FontAwesomeIconView ribbonsButton;
    @FXML protected FontAwesomeIconView previousPicture;
    @FXML protected Label containerName;
    @FXML Product container;
    @FXML TextField searchField;
    @FXML ListView<Gift> listView;
    @FXML ListView<StaticProduct> internalProductsListView;
    @FXML ListView<PaperProduct> internalPapersListView;
    @FXML ListView<RibbonProduct> internalRibbonsListView;
    @FXML ToggleSwitch editSwitch;
    Gift actualGift;
    private final ObservableList<Gift> giftObservableList = FXCollections.observableArrayList();
    private final ObservableList<PaperProduct> papersObservableList = FXCollections.observableArrayList();
    private final ObservableList<RibbonProduct> ribbonsObservableList = FXCollections.observableArrayList();
    private final ObservableList<StaticProduct> productsObservableList = FXCollections.observableArrayList();


    protected int imageIndex = 0;
    protected List<String> files = new ArrayList<>();
    protected List<String> deleteFiles = new ArrayList<>();
    protected ArrayList<Picture> pictureList = new ArrayList<>();
    private final ObservableList<Product> papersProducts = FXCollections.observableArrayList();
    private final ObservableList<Product> containerProducts = FXCollections.observableArrayList();
    private final ObservableList<Product> ribbonsProducts = FXCollections.observableArrayList();
    private final ObservableList<Product> internalProducts = FXCollections.observableArrayList();


    private int index;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        giftObservableList.addAll((Gift) Request.getJ("/gifts/criteria",Gift[].class, true));
        precioField.focusedProperty().addListener(new FocusVerificationFields(precioField, true, 4,2));
        precioField.textProperty().addListener(new ChangedVerificationFields(precioField, true, 4,2));

        papersProducts.setAll(Request.getJ("products/basics/filter-list?privacy=publico&productTypes=Papeles", Product[].class, false));
        containerProducts.setAll(Request.getJ("products/basics/filter-list?privacy=publico&idClass=25", Product[].class, false));
        ribbonsProducts.setAll(Request.getJ("products/basics/filter-list?privacy=publico&productTypes=Listones", Product[].class, false));
        internalProducts.setAll(Request.getJ("products/basics/filter-list?privacy=publico&productTypes=Fijo", Product[].class, false));

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

        listView.setOnMouseClicked(mouseEvent -> {
            if (listView.getSelectionModel().getSelectedItem() != null && !existChanges()) {
                imageIndex = 0;
                updateView();
            }
        });

        //------------------------------------------------PRODUCTS BUTTONS --------------------------------------------------------------------

        containerButton.setOnMouseClicked(mouseEvent -> {
            Product product = loadDialog(containerProducts, FXCollections.observableArrayList(actualGift.getContainer()));
            if (product != null) {
                actualGift.setContainer(product);
                container = product;
                containerName.setText(product.getName());
            }
        });

        papersButton.setOnMouseClicked(mouseEvent -> {
            Product product = loadDialog(containerProducts, FXCollections.observableArrayList(actualGift.getPapers()));
            if (product != null) {


            }
        });

        ribbonsButton.setOnMouseClicked(mouseEvent -> {

        });

        productsButton.setOnMouseClicked(mouseEvent -> {

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

        //------------------------------------------------IMAGE BUTTONS--------------------------------------------------------------------

        imageButton.setOnMouseClicked(mouseEvent -> {
            imageButton.requestFocus();
            Stage s = (Stage)((Node)(mouseEvent.getSource())).getScene().getWindow();
            files = uploadImage(s);
        });

        previousPicture.setOnMouseClicked(mouseEvent -> {
            previousPicture.requestFocus();
            imageIndex--;
            checkIndex();
        });

        nextPicture.setOnMouseClicked(mouseEvent -> {
            nextPicture.requestFocus();
            imageIndex++;
            checkIndex();
        });

        deletePicture.setOnMouseClicked(mouseEvent -> {
            deletePicture.requestFocus();
            files = deletePicture();
        });
    }

    public void propertiesPapersProduct(boolean isCreate, Product insideProduct){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/gift_paper_properties.fxml"));
        try {
            GiftPaperPropertiesController dialogController = new GiftPaperPropertiesController();
            fxmlLoader.setController(dialogController);
            Parent parent = fxmlLoader.load();
            dialogController.setPaper(insideProduct);
            if (!isCreate){
                dialogController.deleteButton.setVisible(true);
            }else {
                dialogController.deleteButton.setVisible(false);
            }
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
            PaperProductToSend paperProductToSend = (PaperProductToSend) stage.getUserData();
            /*if (paperProductToSend != null) {
                if (isCreate){
                    insideProductList.add(internalProductPropertiesToSend.getInsideProduct());
                    internalProducts.removeIf(p -> p.getIdProduct().equals(internalProductPropertiesToSend.getInsideProduct().getId()));
                }else {
                    if (internalProductPropertiesToSend.getAction() == Action.DELETE){
                        insideProductList.remove(internalProductPropertiesToSend.getInsideProduct());
                        internalProducts.add(new Product(internalProductPropertiesToSend.getInsideProduct().getIdProduct(),internalProductPropertiesToSend.getInsideProduct().getName()));
                    }else {
                        insideProductList.set(insideProductList.indexOf(internalProductsListView.getSelectionModel().getSelectedItem()), internalProductPropertiesToSend.getInsideProduct());
                    }
                }
                showList();
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Product loadDialog(ObservableList<Product> productsList, ObservableList<Product> productListToDelete) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/select_list_containers.fxml"));
        try {
            SelectContainerProduct dialogController = new SelectContainerProduct();
            fxmlLoader.setController(dialogController);
            Parent parent = fxmlLoader.load();
            dialogController.setProductsList(productsList, productListToDelete);
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
            Product product = (Product) stage.getUserData();
            if (product != null) {
                return product;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void delete() {
        deleteFiles = new ArrayList<>();
        for(Picture p: actualGift.getPictures()){
            deleteFiles.add(p.getPath());
        }
        ImageService.deleteImages(deleteFiles);
        Request.deleteJ( "products", actualGift.getIdGitft());
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
            Gift gift = new Gift();
            setInfo(gift);
            Gift returnedGift = null;
            try {
                ArrayList<Picture> picturesOriginal = new ArrayList<>(gift.getPictures());
                gift.setPictures(new ArrayList<>());
                Request.putJ(gift.getRoute(), gift);
                gift = (Gift) Request.find(gift.getRoute(), gift.getIdGitft(), gift.getClass());
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
            updateView();
        }else{
            showAlertEmptyFields("Tienes un campo indispensable vacio");
        }
        editSwitch.setSelected(false);
        editView(fieldsAnchorPane, editSwitch, updateButton);
    }

    public void add(){

    }


    @Override
    public boolean existChanges() {
        return false;
    }

    @Override
    public void putFields() {
        nombreField.setText(actualGift.getName());
        precioField.setText(actualGift.getPrice().toString());
        laborCostField.setText(actualGift.getLabor().toString());
        containerName.setText(actualGift.getContainer().getName());
        papersObservableList.setAll(actualGift.getPapers());
        ribbonsProducts.setAll(actualGift.getRibbons());
        internalProducts.setAll(actualGift.getStaticProducts());
        container = actualGift.getContainer();
        showProductsList();
    }

    private void showProductsList() {
        internalPapersListView.getItems().setAll(papersObservableList);
        internalRibbonsListView.getItems().setAll(ribbonsObservableList);
        internalProductsListView.getItems().setAll(productsObservableList);
    }

    @Override
    public void updateView() {
        actualGift = listView.getSelectionModel().getSelectedItem();
        //Disable edit option
        editSwitch.setSelected(false);
        editView(fieldsAnchorPane, editSwitch, updateButton);
        index = giftObservableList.indexOf(listView.getSelectionModel().getSelectedItem());
        privacyProduct();
        putFields();
        files = new ArrayList<>();
        deleteFiles = new ArrayList<>();
        pictureList = new ArrayList<>();
        fillImageList();
        checkIndex();
    }

    private void privacyProduct() {


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

    @Override
    public void setInfo(Gift gift) {
        gift.setName(nombreField.getText());
        gift.setPrice(new BigDecimal(precioField.getText()));
        gift.setPapers(papersObservableList);
        gift.setPictures(pictureList);
        gift.setRibbons(ribbonsObservableList);
        gift.setStaticProducts(productsObservableList);
        gift.setContainer(container);
        gift.setIdGitft(actualGift.getIdGitft());
    }



    //--------------------------------------------------------------------------- IMAGE METHODS--------------------------------------------------------------
    @Override
    public void decreaseIndex() {
        imageIndex--;
    }

    @Override
    public List<String> getFiles() {
        return files;
    }

    @Override
    public FontAwesomeIconView getNextPicture() {
        return nextPicture;
    }
    @Override
    public FontAwesomeIconView getPreviousPicture() {
        return previousPicture;
    }

    @Override
    public List<String> getDeleteFiles() {
        return deleteFiles;
    }

    @Override
    public ImageView getImage() {
        return productImage;
    }

    @Override
    public int getImageIndex() {
        return imageIndex;
    }

    @Override
    public void updateIndex() {
        imageIndex = files.size() -1;
    }

    @Override
    public List<Picture> getPictures() {
        return pictureList;
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
