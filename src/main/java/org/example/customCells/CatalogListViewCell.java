package org.example.customCells;

import org.example.model.Catalog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class CatalogListViewCell extends ListCell<Catalog> {
    @FXML
    Label productName;

    @FXML
    ImageView productImage;

    @FXML
    private AnchorPane anchorPane;


    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(Catalog catalog, boolean empty) {
        super.updateItem(catalog, empty);

        if(empty || catalog == null) {

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

            productName.setText(String.valueOf(catalog.getName()));
            /*if(!product.getPictures().isEmpty()){
                Image image = new Image(product.getPictures().get(0).getPath(), 100, 100, false, false);
                productImage.setImage(image);
            }*/

            setText(null);
            setGraphic(anchorPane);

        }

    }
}
