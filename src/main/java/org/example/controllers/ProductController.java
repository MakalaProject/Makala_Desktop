package org.example.controllers;

import javafx.scene.Node;
import javafx.stage.FileChooser;
import org.example.interfaces.IControllerCreate;
import org.example.interfaces.IListController;
import org.example.model.products.Product;
import org.example.model.products.ProductClassDto;
import org.example.productsSubControllers.*;
import org.example.services.*;
import org.example.interfaces.IControllerProducts;
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
    @FXML ToggleSwitch editSwitch;
    @FXML AnchorPane fieldsAnchorPane;
    @FXML FontAwesomeIconView deletePicture;
    @FXML FontAwesomeIconView nextPicture;
    @FXML FontAwesomeIconView previousPicture;

    Product actualProduct;

    //Combo box lists
    private static final ObservableList<String> typeItems = FXCollections.observableArrayList("Fijo","Granel","Comestible", "Papeles", "Listones","Creado","Cajas");
    private static final ObservableList<String> privacyItems = FXCollections.observableArrayList("Publico", "Privado");
    private static final ObservableList<ProductClassDto> classificationItems = FXCollections.observableArrayList(Request.getJ( "classifications/products", ProductClassDto[].class, false));

    //Products list
    private final ObservableList<Product> productObservableList = FXCollections.observableArrayList();
    //Properties controller
    private static final IControllerProducts[] propertiesControllers = {new StaticProductController(), new BoxProductController(), new RibbonProductController(), new PaperProductController(), new BulkProductController()};
    private IControllerProducts actualPropertiesController;
    private int index;
    private int imageIndex = 0;
    private List<String> files = new ArrayList<>();
    private List<String> deleteFiles = new ArrayList<>();

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
        maxField.textProperty().addListener(new RegexVerificationFields(maxField, true, 3));
        minField.textProperty().addListener(new RegexVerificationFields(minField, true, 3));
        precioField.textProperty().addListener(new RegexVerificationFields(precioField, true, 4,2));

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

        previousPicture.setOnMouseClicked(mouseEvent -> {
            imageIndex--;
            checkIndex();
        });

        nextPicture.setOnMouseClicked(mouseEvent -> {
            imageIndex++;
            checkIndex();
        });

        deletePicture.setOnMouseClicked(mouseEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Eliminar imagen");
            alert.setContentText("¿Seguro quieres eliminarla?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                deleteFiles.add(files.get(imageIndex));
                files.remove(imageIndex);
                if(imageIndex!=0){
                    imageIndex--;
                }
                checkIndex();
            }
        });
    }
    private void checkIndex(){
        if(imageIndex == files.size()-1){
            nextPicture.setDisable(true);
            previousPicture.setDisable(false);
        }else if(files.size() == 0){
            nextPicture.setDisable(true);
            previousPicture.setDisable(true);
        }else if(imageIndex == 0){
            nextPicture.setDisable(false);
            previousPicture.setDisable(true);
        }else{
            previousPicture.setDisable(false);
            nextPicture.setDisable(false);
        }
        if(files.get(imageIndex).contains("http://res.cloudinary.com")){
            Image image = new Image(files.get(imageIndex));
            productImage.setImage(image);
        }else{
            File file = new File(files.get(imageIndex));
            Image  image = new Image(file.toURI().toString());
            productImage.setImage(image);
        }

    }
    //Filter products by classifications
    public void selectClassification(){
        if (!existChanges()) {
            ObservableList<Product> products = productObservableList.stream().filter(p -> p.getProductType().equals(comboBox.getValue()))
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
                    for (Picture p : product.getPictures()) {
                        files.add(p.getPath());
                    }
                    ImageService.deleteImages(deleteFiles);
                    actualProduct = (Product) Request.putJ(product.getRoute(), product);
                    Request.putJ(product.getRoute(), product);
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
            if (Arrays.asList(controller.getIdentifier()).contains(actualProduct.getProductType())){
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
            files.add(file.getPath());
            productImage.setImage(img);
            imageIndex++;
            checkIndex();
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

