package org.example.controllers.client.subcontrollers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.example.interfaces.IControllerCreate;
import org.example.model.Catalog;
import org.example.model.Comment;
import org.example.model.GiftEditable;
import org.example.model.Order;

import java.net.URL;
import java.util.ResourceBundle;

public class OrderPropertiesController implements Initializable, IControllerCreate<Catalog> {
    @FXML TextField statusField;
    @FXML TextField precioField;
    @FXML TextField totalField;
    @FXML TextField fechaEstimadaField;
    @FXML TextField fechaAvanceField;
    @FXML TextField fechaEnvioField;
    @FXML TextField fechaPagoField;
    @FXML TextField fechaCompraField;
    @FXML TextField estadoField;
    @FXML TextField ciudadField;
    @FXML TextField direccionField;
    @FXML ListView<GiftEditable> giftListView;
    @FXML ListView<Comment> commentsListView;

    Order order;

    private ObservableList<GiftEditable> giftsEditableObservableList = FXCollections.observableArrayList();
    private ObservableList<Comment> commentsEditableObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setOrder(Order order){
        this.order =order;
    }

    @Override
    public void setInfo(Catalog object) {

    }

    public void putFields(){

    }
}
