package org.example.controllers.list.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import org.example.model.PurchaseProduct;

import java.net.URL;
import java.util.ResourceBundle;

public class ExpirationProductsController implements Initializable {
    @FXML
    ListView<PurchaseProduct> expiredListView;
    @FXML
    ListView<PurchaseProduct> urgentListView;
    @FXML
    ListView<PurchaseProduct> warningListView;
    @FXML
    ListView<PurchaseProduct> stableListView;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
