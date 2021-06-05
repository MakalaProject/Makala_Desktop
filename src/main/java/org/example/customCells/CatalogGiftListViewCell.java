package org.example.customCells;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import org.example.model.Department;
import org.example.model.Gift;

import java.io.IOException;


public class CatalogGiftListViewCell extends ListCell<Gift> {

    @FXML Label giftLabel;
    @FXML Label stockLabel;
    @FXML
    private AnchorPane anchorPane;


    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(Gift gift, boolean empty) {
        super.updateItem(gift, empty);
        if(empty || gift == null) {
            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/fxml/catalog_gift_listview.fxml"));
                mLLoader.setController(this);
                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            giftLabel.setText(gift.getName());
            stockLabel.setText("Stock: " + gift.getStock());
            if(gift.getStock()== 0){
                anchorPane.setStyle("-fx-background-color: #fea08c;");
            }else{
                anchorPane.setStyle("-fx-background-color: #9dff80;");
            }
            setText(null);
            setGraphic(anchorPane);
        }

    }
}
