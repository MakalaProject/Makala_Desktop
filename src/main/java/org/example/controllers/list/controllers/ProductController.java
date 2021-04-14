package org.example.controllers.list.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.example.controllers.parent.controllers.ProductParentController;
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

    Product actualProduct;
    private final ObservableList<Product> productObservableList = FXCollections.observableArrayList();
    //Properties controller
    private int index;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url,resourceBundle);
        comboBox.getItems().addAll(typeItems);

        clasificacionComboBox.valueProperty().addListener(new ChangeListener<ProductClassDto>() {
            @Override
            public void changed(ObservableValue<? extends ProductClassDto> observableValue, ProductClassDto productClassDto, ProductClassDto t1) {
                tipoComboBox.setValue(t1.getProductType());
            }
        });

        //Get all products
        productObservableList.addAll(Request.getJ("products/basics/list", Product[].class, false));
        //Select default classification and show list
        comboBox.getSelectionModel().select(0);
        selectClassification();
        initialList(listView);

        //Switch to edit
        editSwitch.setOnMouseClicked(mouseEvent -> {
            editView(fieldsAnchorPane, editSwitch, updateButton);
        });

        //Search filter
        FilteredList<Product> filteredProducts = new FilteredList<>(productObservableList, p ->true);
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
                    } else {
                        return false;
                    }
                });
                if (!filteredProducts.isEmpty()) {
                    showList(FXCollections.observableList(filteredProducts), listView, ProductListViewCell.class);
                }
            }
        } );

        //Select item list
        listView.setOnMouseClicked(mouseEvent -> {
            if (!existChanges()) {
                imageIndex = 0;
                updateView();
            }
        });

        //CRUD Buttons
        updateButton.setOnMouseClicked(mouseEvent -> {
            if (actualProduct != null)
            update();
        });

        deleteButton.setOnMouseClicked(mouseEvent -> {
            deleteAlert("producto");
        });

        addButton.setOnMouseClicked(mouseEvent -> {
            add();
        });
    }

    //Filter products by classifications
    public void selectClassification(){
        if (!existChanges()) {
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
            }
        }
    }

    @Override
    public void delete() {
        deleteFiles = new ArrayList<>();
        for(Picture p: actualProduct.getPictures()){
            deleteFiles.add(p.getPath());
        }
        ImageService.deleteImages(deleteFiles);
        Request.deleteJ( actualProduct.getIdentifier().toLowerCase(), actualProduct.getIdProduct());
        if (listView.getItems().size() > 1) {
            productObservableList.remove(index);
            listView.getSelectionModel().select(0);
            selectClassification();
            updateView();
        }else {
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
                    productObservableList.set(index, product);
                    actualProduct = product;
                    selectClassification();
                    listView.getSelectionModel().select(product);
                    listView.scrollTo(product);
                    List<String> urls = ImageService.uploadImages(files);
                    files = urls;
                    for (String s : urls) {
                        product.getPictures().add(new Picture(s));
                    }
                    files = new ArrayList<>();
                    if(deleteFiles.size() != 0){
                        ArrayList<Picture> picturesToRemove = new ArrayList<>(product.getPictures());
                        int counter = 0;
                        for (Picture p : product.getPictures()) {
                            files.add(p.getPath());
                            for(String s: deleteFiles){
                                if(s.equals(picturesToRemove.get(counter).getPath())){
                                    picturesToRemove.remove(counter);
                                    counter--;
                                }
                            }
                            counter++;
                        }
                        new ListToChangeTools<Picture,Integer>().setToDeleteItems(actualProduct.getPictures(), picturesToRemove);
                        product.setPictures(picturesToRemove);
                        ImageService.deleteImages(deleteFiles);
                    }
                    actualProduct = (Product) Request.putJ(product.getRoute(), product);
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
        add("/fxml/product_create.fxml",listView,productObservableList, ProductListViewCell.class);
    }

    @Override
    public boolean existChanges(){
        if(actualProduct == null){
            return false;
        }
        Product product = (Product) actualPropertiesController.getObject();
        setInfo(product);
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
        minField.setText(actualProduct.getMin().toString());
        maxField.setText(actualProduct.getMax().toString());
        stockField.setText(actualProduct.getStock().toString());
        privacidadComboBox.setValue(actualProduct.getPrivacy());
        tipoComboBox.setValue(actualProduct.getProductClassDto().getProductType());
        clasificacionComboBox.setValue(actualProduct.getProductClassDto());
    }

    @Override
    public void cleanForm() {
        nombreField.setText("");
        maxField.setText("");
        minField.setText("");
        precioField.setText("");
        propertiesAnchorPane.getChildren().clear();
    }

    @Override
    public void setInfo(Product product){
        super.setInfo(product);
        product.setStock(actualProduct.getStock());
        product.setIdProduct(actualProduct.getIdProduct());
        product.setPictures(actualProduct.getPictures());
    }

    @Override
    public void updateView() {
        //Disable edit option
        editSwitch.setSelected(false);
        editView(fieldsAnchorPane, editSwitch, updateButton);
        //Update the actual product
        actualProduct = listView.getSelectionModel().getSelectedItem();
        index = productObservableList.indexOf(listView.getSelectionModel().getSelectedItem());
        //Put general information
        for (IControllerProducts controller : propertiesControllers) {
            //Find the controller to the type product
            if (Arrays.asList(controller.getIdentifier()).contains(actualProduct.getProductClassDto().getProductType())){
                changeType(controller);
                actualPropertiesController = controller;
                actualProduct = (Product) controller.findObject(listView.getSelectionModel().getSelectedItem());
                putFields();
                files = new ArrayList<>();
                deleteFiles = new ArrayList<>();
                fillImageList();
                checkIndex();
                return;
            }
        }
    }

    private void fillImageList(){
        if(!actualProduct.getPictures().isEmpty()){
            for(Picture p : actualProduct.getPictures()){
                files.add(p.getPath());
            }
        }
    }
}

