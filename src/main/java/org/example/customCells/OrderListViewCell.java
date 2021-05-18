package org.example.customCells;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import org.example.model.Gift;
import org.example.model.Order;

import java.io.IOException;

public class OrderListViewCell extends ListCell<Order> {
    @FXML
    Label orderName;

    @FXML
    private AnchorPane anchorPane;


    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(Order order, boolean empty) {
        super.updateItem(order, empty);

        if(empty || order == null) {

            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/fxml/order_list_view.fxml"));
                mLLoader.setController(this);
                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            orderName.setText(String.valueOf(order));
            setText(null);
            setGraphic(anchorPane);

        }

    }
}
