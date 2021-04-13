package org.example.customCells;

import org.example.model.products.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ProductListViewCell extends ListCell<Product> implements IConstructor<ProductListViewCell>{
    @FXML
    Label productName;

    @FXML
    ImageView productImage;

    @FXML
    private AnchorPane anchorPane;


    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(Product product, boolean empty) {
        super.updateItem(product, empty);

        if(empty || product == null) {

            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/fxml/product_list_view.fxml"));
                mLLoader.setController(this);
                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            productName.setText(String.valueOf(product.getName()));


            setText(null);
            setGraphic(anchorPane);

        }

    }

    @Override
    public ProductListViewCell getControllerCell() {
        return new ProductListViewCell();
    }

}
