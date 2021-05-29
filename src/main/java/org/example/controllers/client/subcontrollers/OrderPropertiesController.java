package org.example.controllers.client.subcontrollers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.ToggleSwitch;
import org.example.interfaces.IControllerCreate;
import org.example.model.Catalog;
import org.example.model.Comment;
import org.example.model.GiftEditable;
import org.example.model.Order;
import org.example.services.Request;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OrderPropertiesController implements Initializable, IControllerCreate<Catalog> {
    @FXML TextField statusField;
    @FXML TextField envioPrecioField;
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
    @FXML
    ToggleSwitch editSwitch;

    Order order;

    private ObservableList<GiftEditable> giftsEditableObservableList = FXCollections.observableArrayList();
    private ObservableList<Comment> commentsEditableObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        editSwitch.setVisible(false);
        commentsListView.setOnMouseClicked(mouseEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/order_comment.fxml"));
            try {
                ComentPropertiesController dialogController = new ComentPropertiesController();
                fxmlLoader.setController(dialogController);
                Parent parent = fxmlLoader.load();
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                dialogController.setObject(commentsListView.getSelectionModel().getSelectedItem());
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void setOrder(Order order){
        this.order = (Order)Request.find("orders", order.getIdOrder(), Order.class);
        putFields();
    }

    @Override
    public void setInfo(Catalog object) {

    }

    public void putFields(){
        giftsEditableObservableList.setAll(order.getGifts());
        commentsEditableObservableList.setAll(order.getComments());
        statusField.setText(order.getStatus());
        envioPrecioField.setText(order.getShippingPrice().toString());
        totalField.setText(order.getTotalPrice().toString());
        fechaEstimadaField.setText(order.getEstimatedShippingDate().toString());
        if(order.getAdvanceDate() != null)
        fechaAvanceField.setText(order.getAdvanceDate().toString());
        if(order.getShippingDate() != null)
        fechaEnvioField.setText(order.getShippingDate().toString());
        if(order.getTotalPaymentDate() != null)
        fechaPagoField.setText(order.getTotalPaymentDate().toString());
        fechaCompraField.setText(order.getDate().toString());
        estadoField.setText(order.getAddress().getCity().getState().getName());
        ciudadField.setText(order.getAddress().getCity().getName());
        direccionField.setText(order.getAddress().getAddress());
        showOrderLists();

    }

    protected void showOrderLists() {
        giftListView.getItems().setAll(giftsEditableObservableList);
        giftListView.prefHeightProperty().bind(Bindings.size(FXCollections.observableList(order.getGifts())).multiply(23.7));
        commentsListView.getItems().setAll(commentsEditableObservableList);
        commentsListView.prefHeightProperty().bind(Bindings.size(FXCollections.observableList(order.getComments())).multiply(23.7));
    }
}
