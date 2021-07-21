package org.example.customCells;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.example.model.Catalog;
import org.example.model.Decoration;

import java.io.IOException;

public class DecorationListViewCell  extends ListCell<Decoration> {
    @FXML
    Label decorationName;

    @FXML
    private AnchorPane anchorPane;

    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(Decoration catalog, boolean empty) {
        super.updateItem(catalog, empty);

        if(empty || catalog == null) {

            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/fxml/decoration_list_view.fxml"));
                mLLoader.setController(this);
                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            decorationName.setText(String.valueOf(catalog.getName()));

            setText(null);
            setGraphic(anchorPane);

        }

    }
}
