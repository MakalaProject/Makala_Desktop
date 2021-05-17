package org.example.customCells;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import org.example.interfaces.IConstructor;
import org.example.model.Manual;

import java.io.IOException;

public class ManualListViewCell extends ListCell<Manual> implements IConstructor<ManualListViewCell> {
    @FXML
    Label manualName;

    @FXML
    private AnchorPane anchorPane;


    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(Manual product, boolean empty) {
        super.updateItem(product, empty);
        if(empty || product == null) {

            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/fxml/manual_step.fxml"));
                mLLoader.setController(this);
                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            manualName.setText(String.valueOf(product.getName()));
            setText(null);
            setGraphic(anchorPane);
        }

    }

    @Override
    public ManualListViewCell getControllerCell() {
        return new ManualListViewCell();
    }
}
