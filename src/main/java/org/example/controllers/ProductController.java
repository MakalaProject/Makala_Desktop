package org.example.controllers;

import javafx.scene.Node;
import javafx.stage.FileChooser;
import org.example.interfaces.IControllerCreate;
import org.example.interfaces.IListController;
import org.example.model.products.Product;
import org.example.model.products.ProductClassDto;
import org.example.productsSubControllers.PaperProductController;
import org.example.services.*;
import org.example.productsSubControllers.BulkProductController;
import org.example.interfaces.IControllerProducts;
import org.example.productsSubControllers.RibbonProductController;
import org.example.productsSubControllers.StaticProductController;
import org.example.customCells.ProductListViewCell;
import org.example.customDialogs.ProductCreateController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.model.*;
import org.controlsfx.control.ToggleSwitch;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class ProductController  implements Initializable, IListController<Product>, IControllerCreate<Product> {
    //Resources xml
    @FXML TextField searchField;
    @FXML ComboBox<String> comboBox;
    @FXML ListView<Product> listView;
    @FXML TextField nombreField;
    @FXML TextField minField;
    @FXML TextField maxField;
    @FXML FontAwesomeIconView updateButton;
    @FXML FontAwesomeIconView imageButton;
    @FXML FontAwesomeIconView deleteButton;
    @FXML FontAwesomeIconView addButton;
    @FXML TextField precioField;
    @FXML ImageView productImage;
    @FXML ComboBox<String> privacidadComboBox;
    @FXML ComboBox<ProductClassDto> clasificacionComboBox;
    @FXML ComboBox<String> tipoComboBox;
    @FXML AnchorPane propertiesAnchorPane;
    @FXML AnchorPane fieldsAnchorPane;
    @FXML ToggleSwitch editSwitch;
    @FXML SplitPane principalSplitPane;

    Product actualProduct;

    //Combo box lists
    private static final ObservableList<String> typeItems = FXCollections.observableArrayList("Fijo","Granel","Comestible", "Papeles", "Listones","Creado","Caja");
    private static final ObservableList<String> privacyItems = FXCollections.observableArrayList("Publico", "Privado");
    private static final ObservableList<ProductClassDto> classificationItems = FXCollections.observableArrayList(Request.getJ( "classifications/products", ProductClassDto[].class, false));

    //Products list
    private final ObservableList<Product> productObservableList = FXCollections.observableArrayList();
    //Properties controller
    private static final IControllerProducts[] propertiesControllers = {new StaticProductController(), new RibbonProductController(), new PaperProductController(), new BulkProductController()};
    private IControllerProducts actualPropertiesController;
    private int index;
    private List<File> files = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clasificacionComboBox.itemsProperty().setValue(classificationItems);
        privacidadComboBox.getItems().addAll(privacyItems);
        tipoComboBox.getItems().addAll(typeItems);
        comboBox.getItems().addAll(typeItems);

        //Get all products
        productObservableList.addAll(Request.getJ("products/basics/list", Product[].class, false));
        //Select default classification and show list
        comboBox.getSelectionModel().select(0);

        selectClassification();

        if (!listView.getItems().isEmpty()){
            listView.getSelectionModel().select(0);
            updateView();
        }

        //Verifications with regex
        maxField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if(newValue.length()>3){
                    maxField.setText(oldValue);
                }
                if (!newValue.matches("\\d*")) {
                    maxField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        minField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if(newValue.length()>3){
                    minField.setText(oldValue);
                }
                if (!newValue.matches("\\d*")) {
                    minField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        precioField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if(newValue.length()>7){
                    precioField.setText(oldValue);
                }
                if (!newValue.matches("\\d+(\\.\\d{1,2})?")) {
                    precioField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        //Switch to edit
        editSwitch.setOnMouseClicked(mouseEvent -> {
            editView();
        });

        //Search filter
        FilteredList<Product> filteredProducts = new FilteredList<>(productObservableList, p ->true);
        searchField.textProperty().addListener((observable, oldValue, newValue) ->{
                filteredProducts.setPredicate(product -> {
                    if (newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseText = newValue.toLowerCase();
                    if (product.getName().toLowerCase().contains(lowerCaseText)) {
                        return true;
                    } else if (product.getProductType().toLowerCase().contains(lowerCaseText)) {
                        return true;
                    }/*else if(product.getProductClass().getClassification().toLowerCase().contains(lowerCaseText)){
                    return true;
                }*/ else {
                        return false;
                    }
                });
                if (!filteredProducts.isEmpty()) {
                    showList(FXCollections.observableList(filteredProducts), listView, ProductListViewCell.class);
                }
        } );

        //Select item list
        listView.setOnMouseClicked(mouseEvent -> {
            if (!existChanges()) {
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

        imageButton.setOnMouseClicked(mouseEvent -> {
            Stage s = (Stage)((Node)(mouseEvent.getSource())).getScene().getWindow();
            uploadImage(s);
        });
    }

    //Filter products by classifications
    public void selectClassification(){
        ObservableList<Product> products = productObservableList.stream().filter(p -> p.getProductType().equals(comboBox.getValue()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        //check if list is empty
        if (products.isEmpty()){
            listView.setDisable(true);
            listView.getItems().clear();
            cleanForm();
        }else {
            listView.setDisable(false);
            showList(products, listView, ProductListViewCell.class);
        }
    }

    @Override
    public void delete() {
        Request.deleteJ( actualProduct.getRoute(), actualProduct.getIdProduct());
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
        if(!maxField.getText().isEmpty() || !minField.getText().isEmpty() || !precioField.getText().isEmpty() || !nombreField.getText().isEmpty()){
            Product product = (Product) actualPropertiesController.getObjectByFields();
            if (product != null){
                setInfo(product);
                productObservableList.set(index, product);
                actualProduct = product;
                selectClassification();
                listView.getSelectionModel().select(product);
                listView.scrollTo(product);
                List<String> urls = ImageService.sendImageList(files);
                for(String s: urls){
                    product.getPictures().add(new Picture(s));
                }
                Request.putJ( product.getRoute(), product);
            }else {
                showAlertEmptyFields();
            }
        }else{
            showAlertEmptyFields();
        }
        editSwitch.setSelected(false);
        editView();
    }

    @Override
    public void add() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/product_create.fxml"));
        try {
            Parent parent = fxmlLoader.load();
            ProductCreateController dialogController = fxmlLoader.<ProductCreateController>getController();
            dialogController.setControllersArray(propertiesControllers);
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
            Product product = (Product) stage.getUserData();
            if (product != null) {
                productObservableList.add(product);
                selectClassification();
                listView.getSelectionModel().select(product);
                listView.scrollTo(product);
                updateView();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateView();

    }

    @Override
    public boolean existChanges(){
        if(actualProduct == null){
            return false;
        }
        Product product = (Product) actualPropertiesController.getObject();
        if (!actualProduct.equals(setInfo(product))){
            if(!showAlertUnsavedElement(product.getName(), "Producto")) {
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
        privacidadComboBox.setValue(actualProduct.getPrivacy());
        tipoComboBox.setValue(actualProduct.getProductType());
        clasificacionComboBox.setValue(actualProduct.getProductClassDto());
        Image image;
        /*if(!actualProduct.getPictures().isEmpty()){
            image = new Image(actualProduct.getPictures().get(0).getPath(), 225, 171, false, false);
        }else{
            image = new Image(getClass().getResource("/Images/product.png").toString());
        }
        productImage.setImage(image);*/
    }

    @Override
    public void cleanForm() {
        nombreField.setText("");
        maxField.setText("");
        minField.setText("");
        precioField.setText("");
        propertiesAnchorPane.getChildren().clear();
        //actualPropertiesController.cleanForm
    }

    public Product setInfo(Product product){
        product.setStock(actualProduct.getStock());
        product.setName(nombreField.getText());
        product.setIdProduct(actualProduct.getIdProduct());
        product.setMax(Integer.parseInt(maxField.getText()));
        product.setMin(Integer.parseInt(minField.getText()));
        product.setPrice(new BigDecimal(precioField.getText()));
        product.setProductClassDto(clasificacionComboBox.getSelectionModel().getSelectedItem());
        product.setProductType(actualProduct.getProductType());
        product.setPrivacy(privacidadComboBox.getSelectionModel().getSelectedItem());
        product.setPictures(actualProduct.getPictures());
        return product;
    }

    @Override
    public void editView(){
        if (editSwitch.isSelected()){
            fieldsAnchorPane.setDisable(false);
            propertiesAnchorPane.setDisable(false);
        }else {
            fieldsAnchorPane.setDisable(true);
            propertiesAnchorPane.setDisable(true);
        }
    }

    @Override
    public void updateView() {

        //Disable edit option
        editSwitch.setSelected(false);
        editView();
        //Update the actual product
        actualProduct = listView.getSelectionModel().getSelectedItem();
        index = productObservableList.indexOf(listView.getSelectionModel().getSelectedItem());
        //Put general information
        for (IControllerProducts controller : propertiesControllers) {
            if (Arrays.asList(controller.getIdentifier()).contains(actualProduct.getProductType())){
                changeType(controller);
                actualPropertiesController = controller;
                actualProduct = (Product) controller.findObject(listView.getSelectionModel().getSelectedItem());
                putFields();
                return;
            }
        }

    }

    private void changeType(IControllerProducts controller) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(controller.getResource()));
            fxmlLoader.setController(controller);
            Parent root = fxmlLoader.load();
            propertiesAnchorPane.getChildren().add(root);
            AnchorPane.setTopAnchor(root,0D);
            AnchorPane.setBottomAnchor(root,0D);
            AnchorPane.setRightAnchor(root,0D);
            AnchorPane.setLeftAnchor(root,0D);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadImage(Stage s){
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
            files.add(file);
            productImage.setImage(img);
        }
    }

}

