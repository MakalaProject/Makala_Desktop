package org.example.customCells;

import org.example.model.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ClientListViewCell extends ListCell<Client> {
    @FXML
    Label fullName;


    @FXML
    private AnchorPane anchorPane;

    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(Client client, boolean empty) {
        super.updateItem(client, empty);

        if (empty || client == null) {
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

            fullName.setText(String.valueOf(client.getFirstName()) + " " + String.valueOf(client.getLastName()));
            setText(null);
            setGraphic(anchorPane);
        }

    }
}