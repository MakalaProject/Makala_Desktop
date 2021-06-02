package org.example.controllers.list.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.ToggleSwitch;
import org.example.controllers.client.subcontrollers.ComentPropertiesController;
import org.example.controllers.client.subcontrollers.GiftEditionPropertiesController;
import org.example.customCells.OrderListViewCell;
import org.example.exceptions.ProductDeleteException;
import org.example.interfaces.IControllerCreate;
import org.example.interfaces.IListController;
import org.example.model.*;
import org.example.model.products.Action;
import org.example.services.Request;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;

public class OrderController implements Initializable, IControllerCreate<Order>, IListController<Order> {

    @FXML ListView<Order> listView;
    @FXML TextField statusField;
    @FXML TextField envioPrecioField;
    @FXML TextField totalField;
    @FXML TextField fechaEstimadaField;
    @FXML TextField fechaAvanceField;
    @FXML DatePicker envioDatePicker;
    @FXML TextField fechaPagoField;
    @FXML TextField fechaCompraField;
    @FXML TextField estadoField;
    @FXML TextField ciudadField;
    @FXML TextField direccionField;
    @FXML ListView<GiftEditable> giftListView;
    @FXML ListView<Comment> commentsListView;
    @FXML protected FontAwesomeIconView deleteButton;
    @FXML protected FontAwesomeIconView addButton;
    @FXML protected FontAwesomeIconView updateButton;
    @FXML protected AnchorPane fieldsAnchorPane;
    @FXML ToggleSwitch editSwitch;
    int index;
    Order actualOrder;
    private ObservableList<GiftEditable> giftsEditableObservableList = FXCollections.observableArrayList();
    private ObservableList<Comment> commentsEditableObservableList = FXCollections.observableArrayList();
    private ObservableList<Order> orderObservableList = FXCollections.observableArrayList(Request.getJ("orders/basics", Order[].class, false));
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        orderObservableList.sort(Comparator.comparing(Order::getDate).reversed());
        listView.setItems(orderObservableList);
        showList(orderObservableList, listView, OrderListViewCell.class);
        listView.setOnMouseClicked(mouseEvent -> {
            if (listView.getSelectionModel().getSelectedItem() != null && !existChanges()) {
                updateView();
            }
        });

        editSwitch.setOnMouseClicked(mouseEvent -> {
            editView(fieldsAnchorPane, editSwitch, updateButton);
            updateButton.setVisible(actualOrder.getShippingDate() == null);
        });

        updateButton.setOnMouseClicked(mouseEvent -> {
            updateButton.requestFocus();
            update();
        });

        giftListView.setOnMouseClicked(mouseEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/gift_edition.fxml"));
            try {
                GiftEditionPropertiesController dialogController = new GiftEditionPropertiesController();
                fxmlLoader.setController(dialogController);
                Parent parent = fxmlLoader.load();
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                dialogController.setObject(giftListView.getSelectionModel().getSelectedItem());
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

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


    @Override
    public void delete() {

    }

    @Override
    public void update() {
        if (envioDatePicker.getValue() != null){
            Order order = new Order();
            setInfo(order);
            if (order.getShippingDate() != null && order.getShippingDate().compareTo(order.getTotalPaymentDate()) > -1)
            {
                try {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Actualizar fecha de envio");
                    alert.setHeaderText("Estas a punto de actualizar la fecha de envio");
                    alert.setContentText("Una vez que actualices coloques la fecha de envio, no podrás volver a colocar fecha");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK){
                        Order orderR = (Order) Request.putJ(actualOrder.getRoute(), order);
                        orderObservableList.set(index, order);
                        actualOrder = order;
                        listView.setItems(orderObservableList);
                        listView.getSelectionModel().select(actualOrder);
                        listView.scrollTo(actualOrder);
                        updateView();
                    }
                } catch (ProductDeleteException e) {
                    e.printStackTrace();
                }
            }else{
                showAlertEmptyFields("Verifica las fechas");
            }

        }else {
            showAlertEmptyFields("No puedes actualizar la compra sin la fecha de envio establecida");
        }
    }

    @Override
    public boolean existChanges() {
        return false;
    }

    public void putFields(){
        giftsEditableObservableList.setAll(actualOrder.getGifts());
        commentsEditableObservableList.setAll(actualOrder.getComments());
        statusField.setText(actualOrder.getStatus());
        envioPrecioField.setText(actualOrder.getShippingPrice().toString());
        totalField.setText(actualOrder.getTotalPrice().toString());
        fechaEstimadaField.setText(Formatter.FormatDate(actualOrder.getEstimatedShippingDate()));
        if(actualOrder.getAdvanceDate() != null)
            fechaAvanceField.setText(Formatter.FormatDateTime(actualOrder.getAdvanceDate()));
        else
            fechaAvanceField.setText("");

        if(actualOrder.getTotalPaymentDate() != null)
            fechaPagoField.setText(Formatter.FormatDateTime(actualOrder.getTotalPaymentDate()));
        else
            fechaPagoField.setText("");
        fechaCompraField.setText(Formatter.FormatDateTime(actualOrder.getDate()));
        estadoField.setText(actualOrder.getAddress().getCity().getState().getName());
        ciudadField.setText(actualOrder.getAddress().getCity().getName());
        direccionField.setText(actualOrder.getAddress().getAddress());
        envioDatePicker.setDisable(actualOrder.getTotalPaymentDate() == null);
        if (actualOrder.getShippingDate() != null){
            envioDatePicker.setValue(actualOrder.getShippingDate().toLocalDate());
        }else{
            envioDatePicker.setValue(null);
        }
        envioDatePicker.setDayCellFactory(d ->
                new DateCell() {
                    @Override public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        //setDisable(item.isBefore(actualOrder.getShippingDate().toLocalDate()));
                    }});
        showOrderLists();
    }

    @Override
    public void updateView() {
        editSwitch.setSelected(false);
        editView(fieldsAnchorPane, editSwitch, updateButton);
        actualOrder = listView.getSelectionModel().getSelectedItem();
        actualOrder = (Order)Request.find("orders", actualOrder.getIdOrder(), Order.class);
        index = orderObservableList.indexOf(listView.getSelectionModel().getSelectedItem());
        putFields();
    }

    @Override
    public void cleanForm() {

    }

    protected void showOrderLists() {
        giftListView.getItems().setAll(giftsEditableObservableList);
        giftListView.prefHeightProperty().bind(Bindings.size(FXCollections.observableList(actualOrder.getGifts())).multiply(23.7));
        commentsListView.getItems().setAll(commentsEditableObservableList);
        commentsListView.prefHeightProperty().bind(Bindings.size(FXCollections.observableList(actualOrder.getComments())).multiply(23.7));
    }

    @Override
    public void setInfo(Order order){
        order.setIdOrder(actualOrder.getIdOrder());
        order.setAddress(actualOrder.getAddress());
        order.setComments(actualOrder.getComments());
        order.setStatus(actualOrder.getStatus());
        order.setDate(actualOrder.getDate());
        order.setAdvanceDate(actualOrder.getAdvanceDate());
        order.setGifts(actualOrder.getGifts());
        order.setEstimatedShippingDate(actualOrder.getEstimatedShippingDate());
        order.setIdClient(actualOrder.getIdClient());
        order.setTotalPrice(actualOrder.getTotalPrice());
        order.setShippingDate(envioDatePicker.getValue().atStartOfDay());
        order.setShippingPrice(actualOrder.getShippingPrice());
        order.setTotalPaymentDate(actualOrder.getTotalPaymentDate());
    }

}
