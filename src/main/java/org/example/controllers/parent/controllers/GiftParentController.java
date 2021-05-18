package org.example.controllers.parent.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import org.example.controllers.elements.controllers.SelectContainerProduct;
import org.example.interfaces.IControllerCreate;
import org.example.interfaces.IPictureController;
import org.example.interfaces.ListToChangeTools;
import org.example.model.*;
import org.example.model.products.*;
import org.example.services.Request;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    @FXML protected ListView<RibbonProductToSend> internalRibbonsListView;
    protected Gift actualGift;
    protected final ObservableList<PaperProductToSend> papersObservableList = FXCollections.observableArrayList();
    protected final ObservableList<RibbonProductToSend> ribbonsObservableList = FXCollections.observableArrayList();
    protected final ObservableList<GiftProductsToSend> productsObservableList = FXCollections.observableArrayList();
    protected final String resourcePapers = "/fxml/gift_paper_properties.fxml";
    protected final String resourceRibbons = "/fxml/gift_ribbon_properties.fxml";
    protected final String resourceProducts = "/fxml/gift_product_properties.fxml";

    protected int imageIndex = 0;
    protected List<String> files = new ArrayList<>();
    protected List<String> deleteFiles = new ArrayList<>();
    protected ArrayList<Picture> pictureList = new ArrayList<>();
    protected final ObservableList<Product> papersProducts = FXCollections.observableArrayList();
    protected final ObservableList<String> privacyItems = FXCollections.observableArrayList( "Privado","Publico", "Premium");
    protected final ObservableList<Product> containerProducts = FXCollections.observableArrayList();
    protected final ObservableList<Product> ribbonsProducts = FXCollections.observableArrayList();
    protected final ObservableList<Product> internalProducts = FXCollections.observableArrayList();

    protected boolean userClicked = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        precioField.focusedProperty().addListener(new FocusVerificationFields(precioField, true, 4,2));
        precioField.textProperty().addListener(new ChangedVerificationFields(precioField, true, 4,2));
        actualGift = new Gift();
        papersProducts.setAll(Request.getJ("products/basics/filter-list?privacy=publico&productTypes=Papeles", Product[].class, false));
        containerProducts.setAll(Request.getJ("products/basics/filter-list?privacy=publico&idClass=21", Product[].class, false));
        ribbonsProducts.setAll(Request.getJ("products/basics/filter-list?privacy=publico&productTypes=Listones", Product[].class, false));
        internalProducts.setAll(Request.getJ("products/basics/filter-list?privacy=publico&productTypes=Fijo,Creados,Comestible", Product[].class, false));

        internalPapersListView.setOnMouseClicked(mouseEvent -> {
            propertiesGiftProducts(resourcePapers,false, internalPapersListView.getSelectionModel().getSelectedItem(), actualGift.getPapers(), internalPapersListView);
        });
        internalRibbonsListView.setOnMouseClicked(mouseEvent -> {
            propertiesGiftProducts(resourceRibbons,false, internalRibbonsListView.getSelectionModel().getSelectedItem(), actualGift.getRibbons(), internalRibbonsListView);
        });
        internalProductsListView.setOnMouseClicked(mouseEvent -> {
            propertiesGiftProducts(resourceProducts,false, internalProductsListView.getSelectionModel().getSelectedItem(), actualGift.getStaticProducts(), internalRibbonsListView);
            checkInternalProducts();
        });


        //------------------------------------------------PRODUCTS BUTTONS --------------------------------------------------------------------

        containerButton.setOnMouseClicked(mouseEvent -> {
            Product product = loadDialog(containerProducts, FXCollections.observableArrayList(actualGift.getContainer()));
            if (product != null) {
                actualGift.setContainer(product);
                container = product;
                containerExtended = (BoxProduct)Request.find("products/boxes",container.getIdProduct(),BoxProduct.class);
                containerName.setText(product.getName());
            }
        });

        productsButton.setOnMouseClicked(mouseEvent -> {
            Product product = loadDialog(internalProducts, FXCollections.observableArrayList());
            if (product != null) {
                StaticProduct staticProduct = new StaticProduct(product.getIdProduct(), product.getName());
                propertiesGiftProducts(resourceProducts,true, new GiftProductsToSend(staticProduct), actualGift.getStaticProducts(),internalProductsListView);
            }
            checkInternalProducts();
        });

        papersButton.setOnMouseClicked(mouseEvent -> {
            Product product = loadDialog(papersProducts, FXCollections.observableArrayList());
            if (product != null) {
                propertiesGiftProducts(resourcePapers,true, new PaperProductToSend(product), actualGift.getPapers(),internalPapersListView);
            }
        });

        ribbonsButton.setOnMouseClicked(mouseEvent -> {
            Product product = loadDialog(ribbonsProducts, FXCollections.observableArrayList());
            if (product != null) {
                propertiesGiftProducts(resourceRibbons,true, new RibbonProductToSend(product), actualGift.getRibbons(),internalRibbonsListView);
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
    protected <T extends GiftProductsParent> void propertiesGiftProducts(String resource, boolean isCreate, GiftProductsParent insideProduct, List<T> giftProducts, ListView<? extends GiftProductsParent> listViewProducts){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resource));
        try {
            Parent parent = fxmlLoader.load();
            IControllerCreate<GiftProductsParent> dialogController = fxmlLoader.getController();
            if (resource.equals(resourceProducts)){
                dialogController.setProduct(insideProduct, isCreate, containerExtended, actualGift);
            }else {
                dialogController.setProduct(insideProduct, isCreate);
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
                        giftProducts.removeIf(g -> g.getId().equals(giftProductsToSend.getId()));
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
        internalRibbonsListView.getItems().clear();
        internalPapersListView.getItems().setAll(actualGift.getPapers());
        internalPapersListView.prefHeightProperty().bind(Bindings.size(FXCollections.observableList(actualGift.getPapers()) ).multiply(23.7));
        internalRibbonsListView.getItems().setAll(actualGift.getRibbons());
        internalRibbonsListView.prefHeightProperty().bind(Bindings.size(FXCollections.observableList(actualGift.getRibbons()) ).multiply(23.7));
        internalProductsListView.getItems().setAll(actualGift.getStaticProducts());
        internalProductsListView.prefHeightProperty().bind(Bindings.size(FXCollections.observableList(actualGift.getStaticProducts())).multiply(23.7));
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
        ArrayList<Integer> idRibbons = new ArrayList<>(actualGift.getRibbons().stream().map(p -> p.getProduct().getIdProduct()).collect(Collectors.toList()));
        ArrayList<Integer> idPapers = new ArrayList<>(actualGift.getPapers().stream().map(p -> p.getProduct().getIdProduct()).collect(Collectors.toList()));
        actualGift.setInternalProducts(Request.postArray("products/statics/find-list",idProducts, StaticProduct[].class));
        actualGift.setInternalRibbons(Request.postArray("products/basics/find-list",idRibbons, RibbonProduct[].class));
        actualGift.setInternalPapers(Request.postArray("products/basics/find-list",idPapers, PaperProduct[].class));
        containerExtended = (BoxProduct)Request.find("products/boxes",actualGift.getContainer().getIdProduct(),BoxProduct.class);
        gift.setContainer(containerExtended);
    }

    @Override
    public void setInfo(Gift gift) {
        gift.setName(nombreField.getText());
        new ListToChangeTools<GiftProductsToSend,Integer>().setToDeleteItems(productsObservableList, actualGift.getStaticProducts());
        new ListToChangeTools<PaperProductToSend,Integer>().setToDeleteItems(papersObservableList, actualGift.getPapers());
        new ListToChangeTools<RibbonProductToSend,Integer>().setToDeleteItems(ribbonsObservableList, actualGift.getRibbons());
        gift.setStaticProducts(actualGift.getStaticProducts());
        gift.setPapers(actualGift.getPapers());
        gift.setRibbons(actualGift.getRibbons());
        gift.setPictures(pictureList);
        gift.setContainer(containerExtended);
        gift.setLaborPrice(new BigDecimal(laborCostField.getText()));
        gift.setIdGift(actualGift.getIdGift());
        gift.setPrice(actualGift.getPrice());
        gift.setRating(actualGift.getRating());
        gift.sortList();
    }
    protected BigDecimal obtainPrice(Gift gift){
        BigDecimal price = gift.getProductsTotalPrice().add(actualGift.getLaborPrice());
        Gain gain = (Gain)Request.getJ("gain-factors/find-one?price="+price.intValue(), Gain.class);
        price = price.divide(gain.getFactor(), 2, RoundingMode.HALF_UP);
        price = price.multiply(new BigDecimal(1.16));
        return price.setScale(2, RoundingMode.CEILING);
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
        return getClass().getResource("/Images/gift.png").toString();
    }
}
