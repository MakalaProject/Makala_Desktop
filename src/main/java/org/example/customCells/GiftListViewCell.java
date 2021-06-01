package org.example.customCells;

import org.example.model.Gift;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class GiftListViewCell extends ListCell<Gift> {
    @FXML
    Label productName;

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
                mLLoader = new FXMLLoader(getClass().getResource("/fxml/gift_list_view.fxml"));
                mLLoader.setController(this);
                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            productName.setText(String.valueOf(gift.getName()));
            setText(null);
            setGraphic(anchorPane);

        }

    }
}
