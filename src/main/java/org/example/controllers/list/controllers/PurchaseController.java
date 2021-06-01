package org.example.controllers.list.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DateCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.ToggleSwitch;
import org.example.controllers.parent.controllers.PurchaseParentController;
import org.example.customCells.GiftListViewCell;
import org.example.customCells.PurchaseListViewCell;
import org.example.exceptions.ProductDeleteException;
import org.example.interfaces.IListController;
import org.example.model.Provider;
import org.example.model.Purchase;
import org.example.services.Request;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PurchaseController extends PurchaseParentController implements IListController<Purchase> {
    @FXML FontAwesomeIconView deleteButton;
    @FXML FontAwesomeIconView addButton;
    @FXML TextField searchField;
    @FXML ListView<Purchase> listView;
    @FXML ToggleSwitch editSwitch;
    @FXML AnchorPane disableAnchorPane;
    private final ObservableList<Purchase> purchaseObservableList = FXCollections.observableArrayList();
    private int index;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url,resourceBundle);
        purchaseObservableList.setAll(Request.getJ("/purchases", Purchase[].class, true));
        showList(purchaseObservableList, listView, PurchaseListViewCell.class);
        FilteredList<Purchase> filteredPurchases = new FilteredList<>(purchaseObservableList, p ->true);
        searchField.textProperty().addListener((observable, oldValue, newValue) ->{
            if (!existChanges()) {
                filteredPurchases.setPredicate(purchase -> {
                    if (newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseText = newValue.toLowerCase();
                    if (purchase.getOrderDate().toString().toLowerCase().contains(lowerCaseText)){
                        return true;
                    }else if(purchase.getPayDate().toString().toLowerCase().contains(lowerCaseText)){
                        return true;
                    }
                    return purchase.getOrderDate().toString().toLowerCase().contains(lowerCaseText);
                });
                if (!filteredPurchases.isEmpty()) {
                    showList(FXCollections.observableList(filteredPurchases), listView, PurchaseListViewCell.class);
                }
            }
        } );
        if (listView.getItems().size() > 0 ){
            listView.getSelectionModel().select(0);
            actualPurchase = listView.getItems().get(0);
            updateView();
        }else {
            actualPurchase = null;
        }

        listView.setOnMouseClicked(mouseEvent -> {
            if (listView.getSelectionModel().getSelectedItem() != null && !existChanges()) {
                updateView();
            }
        });

        editSwitch.setOnMouseClicked(mouseEvent -> {
            editView(fieldsAnchorPane, editSwitch, updateButton);
        });


        //------------------------------------------------CRUD BUTTONS--------------------------------------------------------------------

        updateButton.setOnMouseClicked(mouseEvent -> {
            updateButton.requestFocus();
            if (actualPurchase != null)
                update();
        });


        addButton.setOnMouseClicked(mouseEvent -> {
            addButton.requestFocus();
            if (!existChanges()){
                add();
            }
        });

        deleteButton.setOnMouseClicked(mouseEvent -> {
            deleteButton.requestFocus();
            deleteAlert("compra");
        });
    }

    public void add() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/purchase_create.fxml"));
        try {
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
            Purchase object = (Purchase) stage.getUserData();
            if(object != null){
                actualPurchase = object;
                purchaseObservableList.add(object);
                showList(purchaseObservableList,listView, PurchaseListViewCell.class);
                listView.getSelectionModel().select(object);
                listView.scrollTo(object);
                updateView();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete() {
        try {
            Request.deleteJ( actualPurchase.getRoute(), actualPurchase.getId());
        } catch (Exception e) {
            errorAlert(e.getMessage());
            return;
        }
        if (listView.getItems().size() > 1) {
            purchaseObservableList.remove(index);
            listView.getSelectionModel().select(0);
            updateView();
            showList(purchaseObservableList, listView, PurchaseListViewCell.class);
        }else {
            actualPurchase = null;
            purchaseObservableList.remove(index);
            showList(purchaseObservableList, listView, PurchaseListViewCell.class);
            cleanForm();
        }
    }

    @Override
    public void update() {
        if (actualPurchase.getProducts().size() > 0){
            Purchase purchase = new Purchase();
            setInfo(purchase);
            if ((purchase.getPayDate() != null && purchase.getReceivedDate() != null && purchase.getPayDate().compareTo(purchase.getOrderDate()) > -1 && purchase.getReceivedDate().compareTo(purchase.getOrderDate()) > -1) || (purchase.getPayDate() == null && purchase.getReceivedDate() == null) || (purchase.getPayDate() != null && purchase.getReceivedDate() == null))
            {
                try {
                    Purchase purchaseR = (Purchase) Request.putJ(actualPurchase.getRoute(), purchase);
                    purchaseObservableList.set(index, purchase);
                    actualPurchase = purchase;
                    actualPurchase.setSelectedProducts();
                    listView.setItems(purchaseObservableList);
                    listView.getSelectionModel().select(actualPurchase);
                    listView.scrollTo(actualPurchase);
                    updateView();
                } catch (ProductDeleteException e) {
                    e.printStackTrace();
                }
            }else{
                showAlertEmptyFields("Verifica las fechas");
            }

        }else {
            showAlertEmptyFields("No puedes dejar la compra sin productos");
        }
    }


    @Override
    public boolean existChanges() {
        if(actualPurchase == null){
            return false;
        }
        Purchase purchase = new Purchase();
        setInfo(purchase);
        if (!actualPurchase.equals(purchase)){
            if(!showAlertUnsavedElement(purchase.getOrderDate().toString(), purchase.getIdentifier())) {
                listView.getSelectionModel().select(purchase);
            }else{
                updateView();
            }
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void putFields() {
        orderDatePicker.setValue(actualPurchase.getOrderDate());
        orderDatePicker.setDayCellFactory(d ->
                new DateCell() {
                    @Override public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(item.isBefore(actualPurchase.getOrderDate()));
                    }});
        payDatePicker.setDayCellFactory(d ->
                new DateCell() {
                    @Override public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(item.isBefore(actualPurchase.getOrderDate()));
                    }});
        receivedDatePicker.setDayCellFactory(d ->
                new DateCell() {
                    @Override public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(item.isBefore(actualPurchase.getOrderDate()));
                    }});
        if (actualPurchase.getPayDate() != null){
            payDatePicker.setValue(actualPurchase.getPayDate());
        }else{
            payDatePicker.setValue(null);
        }
        if (actualPurchase.getReceivedDate() != null){
            receivedDatePicker.setValue(actualPurchase.getReceivedDate());
        }else{
            receivedDatePicker.setValue(null);
        }
        setProviderData();
        setProductsList();
        verifyProducts();
        priceField.setText(actualPurchase.getPrice().toString());
        payMethodComboBox.getSelectionModel().select(actualPurchase.getPayMethod());
        if (actualPurchase.getComment() != null){
            commentTextArea.setText(actualPurchase.getComment().getComment());
        }else{
            commentTextArea.setText("");
        }
    }

    @Override
    public void updateView() {
        actualPurchase = listView.getSelectionModel().getSelectedItem();
        index = purchaseObservableList.indexOf(listView.getSelectionModel().getSelectedItem());
        editSwitch.setSelected(false);
        if (actualPurchase.getReceivedDate() != null){
            disableAnchorPane.setVisible(true);
            editSwitch.setVisible(false);
            updateButton.setVisible(false);
        }else{
            disableAnchorPane.setVisible(false);
            editSwitch.setVisible(true);
            updateButton.setVisible(true);
        }
        purchaseProducts = new ArrayList<>(actualPurchase.getProducts());
        editView(fieldsAnchorPane, editSwitch, updateButton);
        fieldsAnchorPane.setDisable(false);
        provider = providers.stream().filter(p -> p.getIdUser().equals(actualPurchase.getIdProvider())).findAny().orElse(null);
        putFields();
    }

    @Override
    public void cleanForm() {
        productListView.getItems().clear();
        orderDatePicker.setValue(LocalDate.now());
        payDatePicker.setValue(LocalDate.now());
        receivedDatePicker.setValue(LocalDate.now());
        providerName.setText("");
        priceField.setText("");

    }

}
