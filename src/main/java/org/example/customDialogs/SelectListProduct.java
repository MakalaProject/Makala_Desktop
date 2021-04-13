package org.example.customDialogs;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.controlsfx.control.CheckListView;
import org.example.interfaces.ListToChangeTools;
import org.example.model.Provider;
import org.example.model.products.Product;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SelectListProduct implements Initializable {
    @FXML
    FontAwesomeIconView saveButton;
    @FXML
    CheckListView<Product> checkListView;
    private Provider provider;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        saveButton.setOnMouseClicked(mouseEvent -> {
            List<Product> productList = checkListView.getCheckModel().getCheckedItems();
            new ListToChangeTools<Product,Integer>().setToDeleteItems(provider.getProducts(), productList);
            provider.setProducts(productList);
            Node source = (Node)  mouseEvent.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();
            stage.close();
            stage.setUserData(provider);
        });
    }

    public void setProvider(Provider provider){
        this.provider = provider;
        if (provider.getProducts() != null){
            for (Product product : checkListView.getItems()) {
                for (Product product1 : provider.getProducts()) {
                    if (product.getIdProduct() == product1.getIdProduct())
                        checkListView.getCheckModel().check(product);
                }
            }
        }

    }
}
