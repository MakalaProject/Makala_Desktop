package org.example.controllers.list.controllers;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.controllers.parent.controllers.ProductParentController;
import org.example.exceptions.ProductDeleteException;
import org.example.interfaces.*;
import org.example.model.products.Product;
import org.example.model.products.ProductClassDto;
import org.example.services.*;
import org.example.customCells.ProductListViewCell;
import javafx.scene.layout.AnchorPane;
import org.example.model.*;
import org.controlsfx.control.ToggleSwitch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class ProductController extends ProductParentController implements IListController<Product> {
    //Resources xml
    @FXML TextField searchField;
    @FXML ComboBox<String> comboBox;
    @FXML ListView<Product> listView;
    @FXML AnchorPane propertiesAnchorPane;
    @FXML ToggleSwitch editSwitch;
    FilteredList<Product> filteredProducts;
    Product actualProduct;
    private final ObservableList<Product> productObservableList = FXCollections.observableArrayList();
    protected static final ObservableList<String> publicProduct = FXCollections.observableArrayList( "Oculto", "Premium", "Publico");
    //Properties controller
    private int index;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url,resourceBundle);
        comboBox.getItems().addAll(typeItems);

        //Get all products
        productObservableList.addAll(Request.getJ("products/basics/list", Product[].class, false));
        //Select default classification and show list
        comboBox.getSelectionModel().select(0);
        //Switch to edit
        editSwitch.setOnMouseClicked(mouseEvent -> {
            editView(fieldsAnchorPane, editSwitch, updateButton);
        });
        selectClassification();
        filteredProducts = new FilteredList<>(listView.getItems(), p ->true);
        //Search filter
        searchField.textProperty().addListener((observable, oldValue, newValue) ->{
            if (!existChanges()) {
                filteredProducts.setPredicate(product -> {
                    if (newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseText = newValue.toLowerCase();
                    if (product.getName().toLowerCase().contains(lowerCaseText)) {
                        return true;
                    } else if (product.getProductClassDto().getProductType().toLowerCase().contains(lowerCaseText)) {
                        return true;
                    } else if(product.getProductClassDto().getClassification().toLowerCase().contains(lowerCaseText)){
                        return true;
                    }else{
                        return false;
                    }
                });
                if (!filteredProducts.isEmpty()) {
                    ObservableList<Product> sended = FXCollections.observableArrayList();
                    sended.addAll(filteredProducts);
                    showList(sended, listView, ProductListViewCell.class);
                }
                System.out.println("Vacia");
            }
        } );
        if(!listView.getItems().isEmpty()){
            listView.getSelectionModel().select(0);
            updateView();
        }
        //Select item list
        listView.setOnMouseClicked(mouseEvent -> {
            if(actualPropertiesController.indispensableChanges()){
                listView.getSelectionModel().select(productObservableList.get(index));
                return;
            }
            if (listView.getSelectionModel().getSelectedItem() != null && !existChanges()) {
                imageIndex = 0;
                updateView();
            }
        });

        //CRUD Buttons
        updateButton.setOnMouseClicked(mouseEvent -> {
            updateButton.requestFocus();
            if (actualProduct != null)
            update();
        });

        deleteButton.setOnMouseClicked(mouseEvent -> {
            deleteButton.requestFocus();
            deleteAlert("producto");
        });

        addButton.setOnMouseClicked(mouseEvent -> {
            addButton.requestFocus();
            if (!existChanges()){
                add();
            }
        });

        comboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (userClicked){
                    selectClassification();
                    userClicked = false;
                }
            }
        });

        comboBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                userClicked = true;
            }
        });
    }

    //Filter products by classifications
    public void selectClassification(){
        if (!existChanges()) {
            changeList();
        }
    }

    public void changeList(){
        ObservableList<Product> products = productObservableList.stream().filter(p -> p.getProductClassDto().getProductType().equals(comboBox.getValue()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        //check if list is empty
        if (products.isEmpty()) {
            listView.setDisable(true);
            listView.getItems().clear();
            cleanForm();
        } else {
            listView.setDisable(false);
            showList(products, listView, ProductListViewCell.class);
            filteredProducts = new FilteredList<>(listView.getItems(), p ->true);
        }
    }

    @Override
    public void delete() {

        try {
            Request.deleteJ( "products", actualProduct.getIdProduct());
        } catch (Exception e) {
            errorAlert(e.getMessage());
            return;
        }
        deleteFiles = new ArrayList<>();
        for(Picture p: actualProduct.getPictures()){
            deleteFiles.add(p.getPath());
        }
        ImageService.deleteImages(deleteFiles);
        if (listView.getItems().size() > 1) {
            productObservableList.remove(index);
            selectClassification();
            listView.getSelectionModel().select(0);
            updateView();
        }else {
            actualProduct = null;
            productObservableList.remove(index);
            selectClassification();
        }
    }

    @Override
    public void update() {
        if(!nombreField.getText().isEmpty()){
            if (Integer.parseInt(minField.getText()) <= Integer.parseInt(maxField.getText())){
                Product product = (Product) actualPropertiesController.getObject();
                if (product != null) {
                    setInfo(product);
                    Product returnedProduct = null;
                    try {
                        ArrayList<Picture> picturesOriginal = new ArrayList<>(product.getPictures());
                        product.setPictures(new ArrayList<>());
                        Request.putJ(product.getRoute(), product);
                        returnedProduct = (Product) Request.find(product.getRoute(), product.getIdProduct(), product.getClass());
                        List<String> urls = ImageService.uploadImages(files);
                        files = urls;
                        returnedProduct.getPictures().removeIf(p -> !p.getPath().contains("http://res.cloudinary.com"));
                        for (String s : urls) {
                            returnedProduct.getPictures().add(new Picture(s));
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
                            new ListToChangeTools<Picture,Integer>().setToDeleteItems(actualProduct.getPictures(), picturesOriginal);
                            returnedProduct.setPictures(picturesOriginal);
                            ImageService.deleteImages(deleteFiles);
                        }
                        returnedProduct = (Product) Request.putJ(product.getRoute(), returnedProduct);
                    } catch (ProductDeleteException e) {
                        if(e.getStatus() == 423){
                            errorAlert(e.getMessage());
                            updateView();
                            return;
                        }
                        duplyElementAlert(product.getIdentifier());
                        return;
                    }
                    product.setPictures(returnedProduct.getPictures());
                    actualPropertiesController.setObject(returnedProduct);
                    actualProduct = product;
                    pictureList = new ArrayList<>(product.getPictures());
                    comboBox.setValue(actualProduct.getProductClassDto().getProductType());
                    productObservableList.set(index, actualProduct);
                    selectClassification();
                    listView.getSelectionModel().select(actualProduct);
                    listView.scrollTo(product);
                    updateView();
                }
            }else {
                showAlertEmptyFields("El mínimo no puede ser mayor al maximo");
            }
        }else{
            showAlertEmptyFields("Tienes un campo indispensable vacio");
        }
        editSwitch.setSelected(false);
        editView(fieldsAnchorPane, editSwitch, updateButton);
    }

    public void add() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/product_create.fxml"));
        try {
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
            Product object = (Product) stage.getUserData();
            if(object != null){
                actualProduct = object;
                productObservableList.add(object);
                userClicked = false;
                comboBox.setValue(object.getProductClassDto().getProductType());
                changeList();
                listView.getSelectionModel().select(object);
                listView.scrollTo(object);
                updateView();
            }
            /*if(actualProduct != null) {
                actualPropertiesController.setObject(actualProduct);
                updateView();
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean existChanges(){
        if(actualProduct == null){
            return false;
        }
        Product product = (Product) actualPropertiesController.getObject();
        //Box products send null if their internal measures are bigger than external, so, have to check to avoid Exception
        if(product == null){
            listView.getSelectionModel().select(actualProduct);
            return false;
        }
        setInfo(product);
        product.sortList();
        actualProduct.sortList();
        if (!actualProduct.equals(product)){
            if(!showAlertUnsavedElement(product.getName(), product.getIdentifier())) {
                listView.getSelectionModel().select(actualProduct);
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
        nombreField.setText(actualProduct.getName());
        precioField.setText(actualProduct.getPrice().toString());
        minField.setText(actualProduct.formatMin().toString());
        maxField.setText(actualProduct.formatMax().toString());
        stockField.setText(actualProduct.formatStock(stockLabel).toString());
        actualProduct.formatStock(minLabel);
        actualProduct.formatStock(maxLabel);
        privacidadComboBox.setValue(actualProduct.getPrivacy());
        tipoComboBox.setValue(actualProduct.getProductClassDto().getProductType());
        clasificacionComboBox.setValue(actualProduct.getProductClassDto());
    }

    @Override
    public void cleanForm() {
        actualProduct = null;
        productImage.setImage(new Image(getClass().getResource("/Images/product.png").toString()));
        nombreField.setText("");
        maxField.setText("");
        minField.setText("");
        precioField.setText("");
        propertiesAnchorPane.getChildren().clear();
    }

    @Override
    public void setInfo(Product product){
        super.setInfo(product);
        product.setIdProduct(actualProduct.getIdProduct());
    }

    @Override
    public void updateView() {
        actualProduct = listView.getSelectionModel().getSelectedItem();
        if(actualProduct == null){
            return;
        }
        //Disable edit option
        editSwitch.setSelected(false);
        editView(fieldsAnchorPane, editSwitch, updateButton);
        index = productObservableList.indexOf(listView.getSelectionModel().getSelectedItem());
        //Put general information
        for (IControllerProducts controller : propertiesControllers) {
            //Find the controller to the type product
            if (Arrays.asList(controller.getIdentifier()).contains(actualProduct.getProductClassDto().getProductType()) || Arrays.asList(controller.getIdentifier()).contains(actualProduct.getProductClassDto().getClassification())){
                changeType(controller);
                actualPropertiesController = controller;
                if(actualProduct.getPrivacy() == null) {
                    actualProduct = (Product) controller.findObject(listView.getSelectionModel().getSelectedItem());
                    actualProduct.getClassificationsPerType().setAll(Request.getJ("classifications/products/filter-list?productType=" + actualProduct.getProductClassDto().getProductType(), ProductClassDto[].class, false));
                    productObservableList.set(index, actualProduct);
                    int listIndex = listView.getSelectionModel().getSelectedIndex();
                    listView.getItems().set(listIndex, actualProduct);
                }else{
                    actualPropertiesController.setObject(actualProduct);
                }
                clasificacionComboBox.setItems(actualProduct.getClassificationsPerType());
                privacyProduct();
                putFields();
                files = new ArrayList<>();
                deleteFiles = new ArrayList<>();
                pictureList = new ArrayList<>();
                fillImageList();
                checkIndex();
                return;
            }
        }
    }

    private void privacyProduct(){
        if (!actualProduct.getPrivacy().equals("Privado")){
            userClicked = false;
            nombreField.setDisable(true);
            clasificacionComboBox.setDisable(true);
            privacidadComboBox.getItems().clear();
            privacidadComboBox.getItems().addAll(publicProduct);
            propertiesAnchorPane.setDisable(true);
        }else {
            userClicked = false;
            nombreField.setDisable(false);
            clasificacionComboBox.setDisable(false);
            privacidadComboBox.getItems().clear();
            privacidadComboBox.getItems().addAll(privacyItems);
            propertiesAnchorPane.setDisable(false);
        }
    }

    private void fillImageList(){
        if(!actualProduct.getPictures().isEmpty()){
            for(Picture p : actualProduct.getPictures()){
                files.add(p.getPath());
                pictureList = new ArrayList<>(actualProduct.getPictures());
            }
        }
    }
}

