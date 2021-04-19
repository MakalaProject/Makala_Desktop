package org.example.controllers.products.subcontrollers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.controllers.elements.controllers.SelectContainerProduct;
import org.example.model.products.BoxProduct;
import org.example.model.products.CraftedProduct;
import org.example.model.products.Product;
import org.example.model.products.StaticProduct;
import org.example.services.Request;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CraftedProductController extends StaticParentProductController<CraftedProduct>{
    @FXML FontAwesomeIconView addInternalProductButton;
    @FXML FontAwesomeIconView addContainerButton;
    @FXML ListView<Product> internalProductsListView;
    CraftedProduct craftedProduct = new CraftedProduct();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url,resourceBundle);
        altoField.setDisable(true);
        anchoField.setDisable(true);
        largoField.setDisable(true);

        addInternalProductButton.setOnMouseClicked(mouseEvent -> {
            ObservableList<Product> internalProducts = FXCollections.observableArrayList(Request.getJ("products/basics/filter-list?productTypes=Comestible,Granel",Product[].class,false));
            Product product = loadDialog(internalProducts);
            if (product != null){
                craftedProduct.setContainerProduct(product);
                //if tipo producto

            }
        });

        addContainerButton.setOnMouseClicked(mouseEvent -> {
            ObservableList<Product> containerProducts = FXCollections.observableArrayList(Request.getJ("products/basics/filter-list?productTypes=Contenedores",Product[].class,false));
            Product product = loadDialog(containerProducts);
            if (product != null){
                craftedProduct.setContainerProduct(product);
                //if tipo producto
                StaticProduct staticProduct = (StaticProduct) Request.find("products/boxes/crafted",product.getIdProduct(), StaticProduct.class);
                craftedProduct.setMeasures(staticProduct.getMeasures());
                altoField.setText(staticProduct.getMeasures());
            }
        });
    }

    public Product loadDialog(ObservableList<Product> productsList){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/select_list_containers.fxml"));
        try {
            SelectContainerProduct dialogController = new SelectContainerProduct();
            fxmlLoader.setController(dialogController);
            Parent parent = fxmlLoader.load();
            dialogController.setProductsList(productsList);
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
            Product product = (Product) stage.getUserData();
            if (product != null){
                return product;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
        return craftedProduct;
    }

    @Override
    public CraftedProduct findObject(Product object) {
        return findObject(object,"products/boxes/crafted", CraftedProduct.class );
    }
}
