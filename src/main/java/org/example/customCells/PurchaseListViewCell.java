package org.example.customCells;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import org.example.interfaces.IConstructor;
import org.example.model.Purchase;

import java.io.IOException;

public class PurchaseListViewCell extends ListCell<Purchase> implements IConstructor<PurchaseListViewCell> {
    @FXML
    Label purchaseDate;

    @FXML
    private AnchorPane anchorPane;


    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(Purchase purchase, boolean empty) {
        super.updateItem(purchase, empty);

        if(empty || purchase == null) {

            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/fxml/purchase_list_view.fxml"));
                mLLoader.setController(this);
                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            purchaseDate.setText(String.valueOf(purchase.getOrderDate()));
            setText(null);
            setGraphic(anchorPane);
        }

    }

    @Override
    public PurchaseListViewCell getControllerCell() {
        return new PurchaseListViewCell();
    }

}
