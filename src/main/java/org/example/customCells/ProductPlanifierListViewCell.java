package org.example.customCells;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import org.example.interfaces.IConstructor;
import org.example.model.products.Product;

import java.io.IOException;

public class ProductPlanifierListViewCell  extends ListCell<Product> implements IConstructor<ProductListViewCell> {
    @FXML
    Label productName;
    @FXML
    FontAwesomeIconView inGift;
    @FXML Button tooltipButton;

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
                mLLoader = new FXMLLoader(getClass().getResource("/fxml/product_planifier_list_view.fxml"));
                mLLoader.setController(this);
                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            productName.setText(String.valueOf(product.getName()));
            tooltipButton.setTooltip(new Tooltip("Tienes regalos que requieren este producto en stock"));
            inGift.setVisible(product.isHasGifts());
            setText(null);
            setGraphic(anchorPane);
        }

    }

    @Override
    public ProductListViewCell getControllerCell() {
        return new ProductListViewCell();
    }

}