package org.example.controllers.parent.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.controllers.PurchaseProductController;
import org.example.controllers.elements.controllers.SelectContainerProduct;
import org.example.controllers.elements.controllers.SelectListProviders;
import org.example.interfaces.IControllerCreate;
import org.example.interfaces.ListToChangeTools;
import org.example.model.*;
import org.example.model.products.Action;
import org.example.model.products.Product;
import org.example.services.Request;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PurchaseParentController implements Initializable, IControllerCreate<Purchase> {
    protected @FXML DatePicker orderDatePicker;
    protected @FXML DatePicker payDatePicker;
    protected @FXML DatePicker receivedDatePicker;
    protected @FXML AnchorPane fieldsAnchorPane;
    protected @FXML Label providerName;
    protected @FXML Label employeeLabel;
    protected @FXML TextField priceField;
    protected @FXML ListView<PurchaseProduct> productListView;
    protected @FXML FontAwesomeIconView updateButton;
    protected @FXML FontAwesomeIconView providerButton;
    protected @FXML FontAwesomeIconView productosButton;
    protected  @FXML TextArea commentTextArea;
    protected @FXML ComboBox<String> payMethodComboBox;
    protected ObservableList<String> payMethods = FXCollections.observableArrayList("Efectivo", "Transferencia");
    protected Purchase actualPurchase;
    protected Provider provider;
    protected boolean editProduct;
    protected Employee actualEmployee;
    protected Purchase purchase = new Purchase();
    protected ArrayList<PurchaseProduct> purchaseProducts = new ArrayList<>();
    protected ArrayList<PurchaseProduct> originalPurchaseProducts = new ArrayList<>();
    protected ObservableList<Provider> providers = FXCollections.observableArrayList(Request.getJ("users/providers",Provider[].class,true));


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        priceField.textProperty().addListener(new ChangedVerificationFields(priceField, true, 4,2));
        priceField.focusedProperty().addListener(new FocusVerificationFields(priceField, true, 4,2));
        orderDatePicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                receivedDatePicker.setDayCellFactory(d ->
                        new DateCell() {
                            @Override public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);
                                setDisable(item.isBefore(orderDatePicker.getValue()) || item.isAfter(LocalDate.now()));
                            }});
                if (receivedDatePicker.getValue() != null && receivedDatePicker.getValue().compareTo(orderDatePicker.getValue())<0){
                    receivedDatePicker.setValue(orderDatePicker.getValue());
                }
                payDatePicker.setDayCellFactory(d ->
                        new DateCell() {
                            @Override public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);
                                setDisable(item.isBefore(orderDatePicker.getValue()));
                            }});
                if (payDatePicker.getValue() != null && payDatePicker.getValue().compareTo(orderDatePicker.getValue())<0){
                    payDatePicker.setValue(orderDatePicker.getValue());
                }
            }
        });
        actualPurchase = new Purchase();
        payMethodComboBox.getItems().setAll(payMethods);
        payMethodComboBox.getSelectionModel().select(0);
        orderDatePicker.setValue(LocalDate.now());
        productListView.setOnMouseClicked(mouseEvent -> {
            Product product = (Product)Request.find("products", productListView.getSelectionModel().getSelectedItem().getProduct().getIdProduct(), Product.class);
            PurchaseProduct productP = productListView.getSelectionModel().getSelectedItem();
            productP.getProduct().setProductClassDto(product.getProductClassDto());
            propertiesPurchasesProducts(false, productP, editProduct);
        });
        productosButton.setOnMouseClicked(mouseEvent -> {
            ArrayList<Product> purchaseP = new ArrayList<>();
            for(PurchaseProduct p : purchaseProducts){
                purchaseP.add(new Product(p.getProduct().getIdProduct()));
            }
            Product product = loadDialog(FXCollections.observableArrayList(provider.getProducts()), FXCollections.observableArrayList(purchaseP));
            if (product != null) {
                propertiesPurchasesProducts(true, new PurchaseProduct(product, new BigDecimal(0)), editProduct);
            }
        });
        providerButton.setOnMouseClicked(mouseEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/select_list_containers.fxml"));
            try {
                SelectListProviders dialogController = new SelectListProviders();
                fxmlLoader.setController(dialogController);
                Parent parent = fxmlLoader.load();
                dialogController.setProvider(provider, providers);
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
                Provider provider = (Provider) stage.getUserData();
                if (provider != null) {
                    providers.add(this.provider);
                    this.provider = provider;
                    setProviderData();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void setProviderData(){
        actualPurchase.setIdProvider(provider.getIdUser());
        providerName.setText(provider.getFirstName());
    }
    public void setProductsList(){
        productListView.prefHeightProperty().bind(Bindings.size(FXCollections.observableList(purchaseProducts) ).multiply(23.7));
        productListView.getItems().setAll(purchaseProducts);
    }

    public void verifyProducts(){
        providerButton.setDisable(actualPurchase.getProducts().size() > 0);
    }

    protected void checkFields() {
        if (priceField.getText().isEmpty()) {
            priceField.setStyle("-fx-background-color: #fea08c; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        } else {
            priceField.setStyle("-fx-background-color: #E3DAD8; -fx-border-color: #E3DAD8  #E3DAD8 white  #E3DAD8; -fx-border-width: 2;");
        }
    }

    public void setActualEmployee(Employee actualEmployee) {
        this.actualEmployee = actualEmployee;
    }

    @Override
    public void setInfo(Purchase purchase) {
        purchase.setId(actualPurchase.getId());
        purchase.setReceivedDate(receivedDatePicker.getValue());
        purchase.setPayDate(payDatePicker.getValue());
        purchase.setOrderDate(orderDatePicker.getValue());
        new ListToChangeTools<PurchaseProduct,Integer>().setToDeleteItems(originalPurchaseProducts, purchaseProducts);
        purchase.setProducts(purchaseProducts);
        purchase.setPrice( new BigDecimal(!priceField.getText().isEmpty() ? priceField.getText() : "0"));
        purchase.setEmployee(actualEmployee);
        purchase.setPayMethod(payMethodComboBox.getSelectionModel().getSelectedItem());
        purchase.setIdProvider(provider.getIdUser());
        if (actualPurchase.getComment()!=null) {
            purchase.setComment(actualPurchase.getComment());
            purchase.getComment().setComment(commentTextArea.getText());
            if(!purchase.getComment().getComment().equals(actualPurchase.getComment().getComment())){
                purchase.getComment().setDate(LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).substring(0,19), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            }
        }else if(!commentTextArea.getText().isEmpty()){
            purchase.setComment(new Comment(commentTextArea.getText()));
            String string = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            purchase.getComment().setDate(LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).substring(0,19), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
    }
        protected void propertiesPurchasesProducts(boolean isCreate, PurchaseProduct product, boolean edit){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/purchase_product.fxml"));
        try {
            Parent parent = fxmlLoader.load();
            PurchaseProductController dialogController = fxmlLoader.getController();
            dialogController.setProduct(product, isCreate, edit);
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
            PurchaseProduct purchaseProduct = (PurchaseProduct) stage.getUserData();
            if (purchaseProduct != null) {
                if (isCreate){
                    purchaseProducts.add(purchaseProduct);
                }else {
                    if (purchaseProduct.getAction() == Action.DELETE){
                        purchaseProducts.remove(purchaseProduct);
                    }else {
                        purchaseProducts.set(actualPurchase.getProducts().indexOf(productListView.getSelectionModel().getSelectedItem()), purchaseProduct);
                    }
                }
                setProductsList();
                verifyProducts();
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
}
