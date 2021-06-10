package org.example.customCells;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import org.example.model.Gift;
import org.example.model.products.Hole;

import java.io.IOException;


public class InternalListViewCell<D> extends ListCell<D> {

    @FXML Label label;
    @FXML
    private AnchorPane anchorPane;


    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(D element, boolean empty) {
        super.updateItem(element, empty);
        if(empty || element == null) {
            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/fxml/internal_listview.fxml"));
                mLLoader.setController(this);
                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            label.setText(element.toString());
            anchorPane.setStyle("-fx-background-color: #f9f7f6");
            setText(null);
            setGraphic(anchorPane);
        }

    }
}
