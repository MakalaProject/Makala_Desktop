package org.example.controllers.list.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.ToggleSwitch;
import org.example.controllers.parent.controllers.PurchaseParentController;
import org.example.customCells.GiftListViewCell;
import org.example.customCells.PurchaseListViewCell;
import org.example.exceptions.ProductDeleteException;
import org.example.interfaces.IControllerCreate;
import org.example.interfaces.IListController;
import org.example.interfaces.ListToChangeTools;
import org.example.model.*;
import org.example.services.Request;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;

public class PurchaseController extends PurchaseParentController implements IListController<Purchase>, IControllerCreate<Purchase>{
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
        purchaseObservableList.setAll(Request.getJ("purchases", Purchase[].class, true));
        purchaseObservableList.sort(Comparator.comparing(Purchase::getOrderDate).reversed());
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
                purchaseObservableList.sort(Comparator.comparing(Purchase::getOrderDate).reversed());
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
        checkFields();
        if (actualPurchase.getProducts().size() > 0 && !priceField.getText().isEmpty()) {
            if (Float.parseFloat(priceField.getText()) > 0) {
                setInfo(purchase);
                if ((purchase.getPayDate() != null && purchase.getReceivedDate() != null) || (purchase.getPayDate() == null && purchase.getReceivedDate() == null) || (purchase.getPayDate() != null && purchase.getReceivedDate() == null)) {
                    if (purchase.getReceivedDate() != null) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Confirmar compra");
                        alert.setHeaderText("Se confirmará la compra y se agregaran los productos al almacen");
                        alert.setContentText("¿Seguro quieres confirmar?");
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.OK) {
                            purchase.setEmployee(actualEmployee);
                            createPurchase();
                        } else {
                            purchase.setReceivedDate(null);
                            receivedDatePicker.setValue(null);
                        }
                    }else{
                        createPurchase();
                    }
                } else {
                    showAlertEmptyFields("No puedes dejar la fecha de pago vacia si ya estableciste fecha de entrega");
                }
            }else{
                showAlertEmptyFields("El precio de la compra no puede ser 0");
            }
        } else {
            showAlertEmptyFields("No puedes dejar la compra sin productos o sin precio");
        }
    }

    private void createPurchase(){
        try {
            Purchase purchaseR = (Purchase) Request.putJ(actualPurchase.getRoute(), purchase);
            purchaseObservableList.set(index, purchase);
            actualPurchase = purchase;
            actualPurchase.setSelectedProducts();
            showList(purchaseObservableList,listView, PurchaseListViewCell.class);
            listView.setItems(purchaseObservableList);
            listView.getSelectionModel().select(actualPurchase);
            listView.scrollTo(actualPurchase);
            updateView();
        } catch (ProductDeleteException e) {
            e.printStackTrace();
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
        orderDatePicker.setDisable(true);
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
                        setDisable(item.isAfter(LocalDate.now()));
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
        if (actualPurchase.getEmployee()!= null){
            employeeLabel.setText(actualPurchase.getEmployee().getFirstName());
        }
        setActualEmployee(actualPurchase.getEmployee());
        setProviderData();
        setProductsList();
        verifyProducts();
        priceField.setText(actualPurchase.getPrice().toString());
        payMethodComboBox.getSelectionModel().select(actualPurchase.getPayMethod());
        if (actualPurchase.getComment() != null){
            commentTextArea.setText(actualPurchase.getComment().getComment());
        }else{
            commentTextArea.clear();
        }
    }

    @Override
    public void updateView() {
        actualPurchase = listView.getSelectionModel().getSelectedItem();
        index = purchaseObservableList.indexOf(listView.getSelectionModel().getSelectedItem());
        editSwitch.setSelected(false);
        editView(fieldsAnchorPane, editSwitch, updateButton);
        if (actualPurchase.getReceivedDate() != null){
            orderDatePicker.setDisable(false);
            editSwitch.setSelected(true);
            editProduct = false;
            disableAnchorPane.setVisible(true);
            editSwitch.setVisible(false);
            updateButton.setVisible(false);
        }else{
            editProduct = true;
            disableAnchorPane.setVisible(false);
            editSwitch.setVisible(true);
            updateButton.setVisible(true);
        }
        purchaseProducts = new ArrayList<>(actualPurchase.getProducts());
        originalPurchaseProducts = new ArrayList<>(actualPurchase.getProducts());
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
