package org.example.controllers.products.subcontrollers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.controllers.elements.controllers.SelectContainerProduct;
import org.example.interfaces.ListToChangeTools;
import org.example.model.products.*;
import org.example.services.Request;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class CraftedProductController extends StaticParentProductController<CraftedProduct> {
    @FXML
    FontAwesomeIconView addInternalProductButton;
    @FXML
    FontAwesomeIconView addContainerButton;
    @FXML
    ListView<InsideProduct> internalProductsListView;
    @FXML
    Label containerName;
    CraftedProduct craftedProduct = new CraftedProduct();
    private final ObservableList<InsideProduct> insideProductList = FXCollections.observableArrayList();
    private final Set<InsideProduct> originalInsideProductList = new HashSet<>();
    private Product productContainer;
    private final ObservableList<Product> internalProducts = FXCollections.observableArrayList();
    private final ObservableList<Product> containerProducts = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        altoField.setDisable(true);
        anchoField.setDisable(true);
        largoField.setDisable(true);
        internalProducts.setAll(Request.getJ("products/basics/filter-list?productTypes=Comestible,Granel", Product[].class, false));
        containerProducts.setAll(Request.getJ("products/basics/filter-list?productTypes=Contenedores", Product[].class, false));
        addInternalProductButton.setOnMouseClicked(mouseEvent -> {
            ArrayList<Product> products = new ArrayList<>();
            for(InsideProduct i: craftedProduct.getProductsInside()){
                products.add(new Product(i.getIdProduct()));
            }
            ObservableList<Product> listToRemove = FXCollections.observableArrayList(products);
            Product product = loadDialog(internalProducts, listToRemove);
            if ( product != null) {
                InsideProduct insideProduct  = new InsideProduct(product.getIdProduct(), product.getName(), new BigDecimal(0),false);
                propertiesInternalProduct(true, insideProduct);
            }
        });

        addContainerButton.setOnMouseClicked(mouseEvent -> {
            Product product = loadDialog(containerProducts, FXCollections.observableArrayList(craftedProduct.getProductContainer()));
            if (product != null) {
                if (productContainer!=null){
                    containerProducts.add(productContainer);
                }
                productContainer = product;
                containerProducts.remove(product);
                craftedProduct.setProductContainer(product);
                getContainer(product);
            }
        });
        internalProductsListView.setOnMouseClicked(mouseEvent -> {
            propertiesInternalProduct(false, internalProductsListView.getSelectionModel().getSelectedItem());
        });
    }

    public void getContainer(Product product){
        StaticProduct staticProduct = (StaticProduct) Request.find("products/statics", product.getIdProduct(), StaticProduct.class);
        craftedProduct.setMeasures(staticProduct.getMeasures());
        anchoField.setText(String.valueOf(staticProduct.getMeasures().getX()));
        altoField.setText(String.valueOf(staticProduct.getMeasures().getY()));
        largoField.setText(String.valueOf(staticProduct.getMeasures().getZ()));
        containerName.setText(staticProduct.getName());
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

    public void propertiesInternalProduct(boolean isCreate, InsideProduct insideProduct){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/inside_product_properties.fxml"));
        try {
            Parent parent = fxmlLoader.load();
            InsideProductController dialogController = fxmlLoader.<InsideProductController>getController();
            dialogController.setInternalProduct(insideProduct);
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
            InternalProductPropertiesToSend internalProductPropertiesToSend = (InternalProductPropertiesToSend) stage.getUserData();
            if (internalProductPropertiesToSend != null) {
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setObject(CraftedProduct craftedProduct){
        this.craftedProduct = craftedProduct;
        this.productContainer = craftedProduct.getProductContainer();
        if (productContainer == null){
            productContainer = containerProducts.get(0);
            getContainer(productContainer);
            containerProducts.remove(0);
        }
        clearController();
        containerName.setText(craftedProduct.getProductContainer().getName());
        if (craftedProduct.getProductsInside() != null){
            for (InsideProduct insideProduct : craftedProduct.getProductsInside()) {
                originalInsideProductList.add(new InsideProduct(insideProduct.getIdProduct(), insideProduct.getName(), insideProduct.getAmount(), false));
            }
        }
        insideProductList.setAll(craftedProduct.getProductsInside());
        showList();
        super.setObject(craftedProduct);
    }

    public void showList(){
        internalProductsListView.setItems(insideProductList);
    }

    public void clearController(){
        insideProductList.clear();
        originalInsideProductList.clear();
    }

    @Override
    public String[] getIdentifier() {
        return new String[]{"Creados"};
    }

    @Override
    public String getResource() {
        return "/fxml/crafted_product_properties.fxml";
    }

    @Override
    public CraftedProduct getObject() {
        craftedProduct = super.getObject(CraftedProduct.class);
        new ListToChangeTools<InsideProduct,Integer>().setToDeleteItems(originalInsideProductList, insideProductList);
        craftedProduct.setProductsInside(insideProductList);
        craftedProduct.setProductContainer(productContainer);
        return craftedProduct;
    }

    @Override
    public CraftedProduct findObject(Product object) {
        return findObject(object,"products/crafted", CraftedProduct.class );
    }
}