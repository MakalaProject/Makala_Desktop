package org.example.customCells;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import org.example.interfaces.IConstructor;
import org.example.model.products.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ProductListViewCell extends ListCell<Product> implements IConstructor<ProductListViewCell> {
    @FXML
    Label productName;

    @FXML
    FontAwesomeIconView productIcon;

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
            if (product.getProductClassDto().getProductType().equals("Fijo")){
                productIcon.setIcon(FontAwesomeIcon.GLASS);
            }else if(product.getProductClassDto().getProductType().equals("Contenedores")){
                productIcon.setIcon(FontAwesomeIcon.INBOX);
            }else if(product.getProductClassDto().getProductType().equals("Granel")){
                productIcon.setIcon(FontAwesomeIcon.LEAF);
            }else if(product.getProductClassDto().getProductType().equals("Papeles")){
                productIcon.setIcon(FontAwesomeIcon.STICKY_NOTE);
            }else if(product.getProductClassDto().getProductType().equals("Listones")){
                productIcon.setIcon(FontAwesomeIcon.CUT);
            }else if(product.getProductClassDto().getProductType().equals("Creados")){
                productIcon.setIcon(FontAwesomeIcon.HAND_STOP_ALT);
            }else if(product.getProductClassDto().getProductType().equals("Comestible")){
                productIcon.setIcon(FontAwesomeIcon.APPLE);
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
