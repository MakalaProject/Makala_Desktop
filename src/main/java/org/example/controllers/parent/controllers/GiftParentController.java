package org.example.controllers.parent.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.Rating;
import org.example.controllers.elements.controllers.SelectContainerProduct;
import org.example.controllers.gift.subcontrollers.SelectListDecoration;
import org.example.customCells.InternalListViewCell;
import org.example.interfaces.IControllerCreate;
import org.example.interfaces.IPictureController;
import org.example.interfaces.ListToChangeTools;
import org.example.model.*;
import org.example.model.products.*;
import org.example.services.Request;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GiftParentController implements Initializable, IPictureController, IControllerCreate<Gift> {

    @FXML
    protected TextField nombreField;
    @FXML protected FontAwesomeIconView updateButton;
    @FXML protected FontAwesomeIconView imageButton;
    @FXML protected TextField precioField;
    @FXML protected TextField flowersField;
    @FXML protected TextField applicationsField;
    @FXML protected TextField laborCostField;
    @FXML protected ImageView giftImage;
    @FXML protected AnchorPane fieldsAnchorPane;
    @FXML protected AnchorPane productsAnchorPane;
    @FXML protected FontAwesomeIconView deletePicture;
    @FXML protected FontAwesomeIconView nextPicture;
    @FXML protected FontAwesomeIconView productsButton;
    @FXML protected FontAwesomeIconView containerButton;
    @FXML protected FontAwesomeIconView papersButton;
    @FXML protected FontAwesomeIconView ribbonsButton;
    @FXML protected FontAwesomeIconView previousPicture;
    @FXML protected Label containerName;
    @FXML protected Rating giftRating;
    protected Product container;
    protected BoxProduct containerExtended;
    @FXML protected ListView<GiftProductsToSend> internalProductsListView;
    @FXML protected ListView<PaperProductToSend> internalPapersListView;
    @FXML protected ListView<Decoration> internalBowsListView;
    protected Gift actualGift;
    public boolean chargedRibbons, chargedTextiles, chargedProducts, chargedContainers;
    protected boolean editProduct = true;

    protected final ObservableList<PaperProductToSend> papersObservableList = FXCollections.observableArrayList();
    protected final ObservableList<GiftProductsToSend> productsObservableList = FXCollections.observableArrayList();
    protected final ObservableList<Decoration> decorationsObservableList = FXCollections.observableArrayList();

    protected final ObservableList<PaperProductToSend> actualPapersObservableList = FXCollections.observableArrayList();
    protected final ObservableList<Decoration> actualDecorationObservableList = FXCollections.observableArrayList();
    protected final ObservableList<GiftProductsToSend> actualProductsObservableList = FXCollections.observableArrayList();

    protected final String resourceProducts = "/fxml/gift_product_properties.fxml";

    protected int imageIndex = 0;
    protected List<String> files = new ArrayList<>();
    protected List<String> deleteFiles = new ArrayList<>();
    protected ArrayList<Picture> pictureList = new ArrayList<>();
    protected final ObservableList<Product> papersProducts = FXCollections.observableArrayList();
    protected final ObservableList<String> privacyItems = FXCollections.observableArrayList( "Privado","Público", "Premium");
    protected final ObservableList<Product> containerProducts = FXCollections.observableArrayList();
    protected final ObservableList<Decoration> decorationProducts = FXCollections.observableArrayList();
    protected final ObservableList<Product> internalProducts = FXCollections.observableArrayList();

    protected boolean userClicked = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        precioField.focusedProperty().addListener(new FocusVerificationFields(precioField, true, 4,2));
        precioField.textProperty().addListener(new ChangedVerificationFields(precioField, true, 4,2));
        actualGift = new Gift();


        internalProductsListView.setOnMouseClicked(mouseEvent -> {
            propertiesGiftProducts(resourceProducts,false, internalProductsListView.getSelectionModel().getSelectedItem(), actualProductsObservableList, internalProductsListView, editProduct);
            checkInternalProducts();
        });

        //------------------------------------------------PRODUCTS BUTTONS --------------------------------------------------------------------

        containerButton.setOnMouseClicked(mouseEvent -> {
            setChargedContainers();
            Product product = loadDialog(containerProducts, FXCollections.observableArrayList(actualGift.getContainer()));
            if (product != null) {
                actualGift.setContainer(product);
                container = product;
                containerExtended = (BoxProduct)Request.find("products/boxes",container.getIdProduct(),BoxProduct.class);
                containerName.setText(product.getName());
            }
        });

        productsButton.setOnMouseClicked(mouseEvent -> {
            setChargedProducts();
            Product product = loadDialog(internalProducts, FXCollections.observableArrayList());
            if (product != null) {
                StaticProduct staticProduct = new StaticProduct(product.getIdProduct(), product.getName());
                propertiesGiftProducts(resourceProducts,true, new GiftProductsToSend(staticProduct), actualProductsObservableList,internalProductsListView, editProduct);
            }
            checkInternalProducts();
        });

        papersButton.setOnMouseClicked(mouseEvent -> {
            setChargedTextiles();
            Product product = loadDialog(papersProducts, FXCollections.observableArrayList());
            if (product != null) {
                actualPapersObservableList.clear();
                if (containerExtended.getHolesDimensions().size() > 0){
                    ArrayList<PaperProductToSend> paperProductsToSend = new ArrayList<>();
                    for (Hole h:containerExtended.getHolesDimensions()){
                        PaperProductToSend paper = new PaperProductToSend();
                        paper.setAmount(1);
                        paper.setHeightCm(h.getHoleDimensions().getY().add(new BigDecimal(5)));
                        paper.setWidthCm(h.getHoleDimensions().getX().add(new BigDecimal(5)));
                        paper.setProduct(product);
                        paperProductsToSend.add(paper);
                    }
                    actualPapersObservableList.setAll(paperProductsToSend);
                }else {
                    PaperProductToSend paperProductToSend = new PaperProductToSend();
                    paperProductToSend.setAmount(1);
                    paperProductToSend.setHeightCm(containerExtended.getMeasures().getY().add(new BigDecimal(5)));
                    paperProductToSend.setWidthCm(containerExtended.getMeasures().getX().add(new BigDecimal(5)));
                    paperProductToSend.setProduct(product);
                    actualPapersObservableList.add(paperProductToSend);
                }
                showProductsList();
            }
        });

        ribbonsButton.setOnMouseClicked(mouseEvent -> {
            setChargedRibbons();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/select_list_generic.fxml"));
            try {
                SelectListDecoration dialogController = new SelectListDecoration();
                fxmlLoader.setController(dialogController);
                Parent parent = fxmlLoader.load();
                ArrayList<Decoration> decorationList = new ArrayList<>(actualDecorationObservableList);
                dialogController.setDecoration(decorationList);
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
                ArrayList<Decoration> returnedDecorations = (ArrayList<Decoration>) stage.getUserData();
                if (returnedDecorations!= null) {
                    actualDecorationObservableList.setAll(returnedDecorations);
                    showProductsList();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
    protected <T extends GiftProductsParent> void propertiesGiftProducts(String resource, boolean isCreate, GiftProductsParent insideProduct, List<T> giftProducts, ListView<? extends GiftProductsParent> listViewProducts, boolean editProduct){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resource));
        try {
            Parent parent = fxmlLoader.load();
            IControllerCreate<GiftProductsParent> dialogController = fxmlLoader.getController();
            if (resource.equals(resourceProducts)){
                dialogController.setProduct(insideProduct, isCreate, containerExtended, actualGift, editProduct);
            }else {
                dialogController.setProduct(insideProduct, isCreate, editProduct);
            }
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
            GiftProductsParent giftProductsToSend = (GiftProductsParent) stage.getUserData();
            if (giftProductsToSend != null) {
                if (isCreate){
                    giftProducts.add((T)giftProductsToSend);
                }else {
                    if (giftProductsToSend.getAction() == Action.DELETE){
                        giftProducts.remove((T)giftProductsToSend);
                    }else {
                        giftProducts.set(giftProducts.indexOf(listViewProducts.getSelectionModel().getSelectedItem()), (T)giftProductsToSend);
                    }
                }
                showProductsList();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setChargedRibbons(){
        if(!chargedRibbons){
            chargedRibbons = true;
            decorationProducts.setAll(Request.getJ("decorations/basics", Decoration[].class, false));
        }
    }

    private void setChargedTextiles(){
        if(!chargedTextiles){
            chargedTextiles = true;
            papersProducts.setAll(Request.getJ("products/basics/filter-list?privacy=publico&productTypes=Telas", Product[].class, false));
        }
    }

    private void setChargedProducts(){
        if (!chargedProducts){
            chargedProducts = true;
            internalProducts.setAll(Request.getJ("products/basics/filter-list?privacy=publico&productTypes=Fijo,Creados,Comestible", Product[].class, false));
        }
    }
    protected void setChargedContainers(){
        if(!chargedContainers){
            chargedContainers = true;
            containerProducts.setAll(Request.getJ("products/basics/filter-list?privacy=publico&idClass=21", Product[].class, false));
        }
    }

    protected Product loadDialog(ObservableList<Product> productsList, ObservableList<Product> productListToDelete) {
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
    protected void showProductsList() {

        internalPapersListView.getItems().clear();
        internalProductsListView.getItems().clear();
        internalBowsListView.getItems().clear();

        internalPapersListView.getItems().setAll(actualPapersObservableList);
        internalPapersListView.setPrefHeight(actualPapersObservableList.size() * 35 + 2);
        internalPapersListView.setCellFactory(listCell -> new InternalListViewCell<>());

        ObservableList<Decoration> dec = FXCollections.observableList(actualDecorationObservableList.stream().filter(l -> !l.isToDelete()).collect(Collectors.toList()));
        internalBowsListView.getItems().setAll(dec);
        internalBowsListView.setPrefHeight(dec.size() * 35 + 2);
        internalBowsListView.setCellFactory(listCell -> new InternalListViewCell<>());

        internalProductsListView.getItems().setAll(actualProductsObservableList);
        internalProductsListView.setPrefHeight(actualProductsObservableList.size() * 35 + 2);
        internalProductsListView.setCellFactory(listCell -> new InternalListViewCell<>());
    }
    protected void checkInternalProducts(){
        if (actualGift.getStaticProducts().size() > 0 ){
            containerButton.setDisable(true);
        }else {
            containerButton.setDisable(false);
        }
    }

    protected void setExtendedInternalProducts(Gift gift){
        ArrayList<Integer> idProducts = new ArrayList<>(actualGift.getStaticProducts().stream().map(p -> p.getProduct().getIdProduct()).collect(Collectors.toList()));
        //ArrayList<Integer> idRibbons = new ArrayList<>(actualGift.getRibbons().stream().map(p -> p.getProduct().getIdProduct()).collect(Collectors.toList()));
        ArrayList<Integer> idPapers = new ArrayList<>(actualGift.getPapers().stream().map(p -> p.getProduct().getIdProduct()).collect(Collectors.toList()));
        actualGift.setInternalProducts(Request.postArray("products/statics/find-list",idProducts, StaticProduct[].class));
        //actualGift.setInternalRibbons(Request.postArray("products/basics/find-list",idRibbons, RibbonProduct[].class));
        actualGift.setInternalPapers(Request.postArray("products/basics/find-list",idPapers, PaperProduct[].class));
        containerExtended = (BoxProduct)Request.find("products/boxes",actualGift.getContainer().getIdProduct(),BoxProduct.class);
        gift.setContainer(containerExtended);
    }

    @Override
    public void setInfo(Gift gift) {
        gift.setName(nombreField.getText());
        new ListToChangeTools<GiftProductsToSend,Integer>().setToDeleteItems(productsObservableList, actualProductsObservableList);
        new ListToChangeTools<PaperProductToSend,Integer>().setToDeleteItems(papersObservableList, actualPapersObservableList);
        new ListToChangeTools<Decoration,Integer>().setToDeleteItems(decorationsObservableList, actualDecorationObservableList);
        gift.setStaticProducts(actualProductsObservableList);
        gift.setPapers(actualPapersObservableList);
        gift.setDecorations(actualDecorationObservableList);
        gift.setPictures(pictureList);
        gift.setContainer(containerExtended);
        gift.setApplications(new BigDecimal(applicationsField.getText()));
        gift.setFlowers(new BigDecimal(flowersField.getText()));
        gift.setLaborPrice(new BigDecimal(!laborCostField.getText().isEmpty() ? laborCostField.getText() : "0"));
        gift.setIdGift(actualGift.getIdGift());
        gift.setPrice(actualGift.getPrice());
        gift.setRating(actualGift.getRating());
        gift.sortList();
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
        return giftImage;
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

    @Override
    public String getDefaultPicture(){
        return getClass().getResource("/images/gift.png").toString();
    }

    @Override
    public FontAwesomeIconView getDeletButton() {
        return deletePicture;
    }
}
