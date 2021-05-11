package org.example.controllers.parent.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PurchaseParentController implements Initializable, IControllerCreate<Purchase> {
    protected @FXML DatePicker orderDatePicker;
    protected @FXML DatePicker payDatePicker;
    protected @FXML DatePicker receivedDatePicker;
    protected @FXML AnchorPane fieldsAnchorPane;
    protected @FXML Label providerName;
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
    protected ArrayList<PurchaseProduct> purchaseProducts = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        actualPurchase = new Purchase();
        payMethodComboBox.getItems().setAll(payMethods);
        payMethodComboBox.getSelectionModel().select(0);
        orderDatePicker.setValue(LocalDate.now());
        productListView.setOnMouseClicked(mouseEvent -> {
            propertiesGiftProducts(false, productListView.getSelectionModel().getSelectedItem());
        });
        productosButton.setOnMouseClicked(mouseEvent -> {
            ArrayList<Product> purchaseProducts = new ArrayList<>();
            for(PurchaseProduct p : actualPurchase.getProducts()){
                purchaseProducts.add(new Product(p.getProduct().getIdProduct()));
            }
            Product product = loadDialog(FXCollections.observableArrayList(Request.getJ("products/basics/filter-list?productTypes="+provider.getProductClassDto().getProductType(), Product[].class, false)), FXCollections.observableArrayList(purchaseProducts));
            if (product != null) {
                propertiesGiftProducts(true, new PurchaseProduct(product, new BigDecimal(0)));
            }
            verifyProducts();
        });
        providerButton.setOnMouseClicked(mouseEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/select_list_containers.fxml"));
            try {
                SelectListProviders dialogController = new SelectListProviders();
                fxmlLoader.setController(dialogController);
                Parent parent = fxmlLoader.load();
                dialogController.setProvider(provider);
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
                Provider provider = (Provider) stage.getUserData();
                if (provider != null) {
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

    public void verifyProducts(){
        providerButton.setDisable(actualPurchase.getProducts().size() > 0);
    }

    @Override
    public void setInfo(Purchase purchase) {
        purchase.setId(actualPurchase.getId());
        purchase.setReceivedDate(receivedDatePicker.getValue());
        purchase.setPayDate(payDatePicker.getValue());
        purchase.setOrderDate(orderDatePicker.getValue());
        new ListToChangeTools<PurchaseProduct,Integer>().setToDeleteItems(purchaseProducts, actualPurchase.getProducts());
        purchase.setProducts(actualPurchase.getProducts());
        purchase.setIdProvider(provider.getIdUser());
        purchase.setComment(actualPurchase.getComment());
        purchase.getComment().setComment(commentTextArea.getText());
    }
        protected void propertiesGiftProducts( boolean isCreate, PurchaseProduct product){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("purchase_product.fxml"));
        try {
            Parent parent = fxmlLoader.load();
            PurchaseProductController dialogController = fxmlLoader.getController();
            dialogController.setProduct(product);
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
            PurchaseProduct purchaseProduct = (PurchaseProduct) stage.getUserData();
            if (purchaseProduct != null) {
                if (isCreate){
                    actualPurchase.getProducts().add(purchaseProduct);
                }else {
                    if (purchaseProduct.getAction() == Action.DELETE){
                        actualPurchase.getProducts().remove(purchaseProduct);
                    }else {
                        actualPurchase.getProducts().set(actualPurchase.getProducts().indexOf(productListView.getSelectionModel().getSelectedItem()), purchaseProduct);
                    }
                }
                productListView.getItems().setAll(actualPurchase.getProducts());
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
