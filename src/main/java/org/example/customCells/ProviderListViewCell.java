package org.example.customCells;

import org.example.model.Provider;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ProviderListViewCell extends ListCell<Provider> {
    @FXML
    Label fullName;


    @FXML
    private AnchorPane anchorPane;


    private FXMLLoader mLLoader;


    @Override
    protected void updateItem(Provider provider, boolean empty) {
        super.updateItem(provider, empty);

        if(empty || provider == null) {
            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/fxml/employee_list_view.fxml"));
                mLLoader.setController(this);
                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            fullName.setText(String.valueOf(provider.getFirstName()) +" "+ String.valueOf(provider.getLastName()));
            setText(null);
            setGraphic(anchorPane);
        }

    }
}
