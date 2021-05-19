package org.example.controllers.parent.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.ToggleSwitch;
import org.example.controllers.elements.controllers.SelectListObject;
import org.example.controllers.elements.controllers.SelectListProviders;
import org.example.interfaces.IChangeable;
import org.example.interfaces.IControllerCreate;
import org.example.model.*;
import org.example.model.products.Product;
import org.example.services.Request;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class RebateParentController implements Initializable, IControllerCreate<Rebate> {
    @FXML
    protected Label objectLabel;
    @FXML protected Label objectName;
    @FXML
    protected DatePicker startDatePicker;
    protected @FXML DatePicker endDatePicker;
    protected @FXML AnchorPane fieldsAnchorPane;
    protected @FXML ComboBox<String> tipoComboBox;
    protected @FXML Label rebajaLabel;
    protected @FXML TextField porcentajeField;
    @FXML
    protected FontAwesomeIconView updateButton;
    protected @FXML FontAwesomeIconView objectButton;
    protected Rebate actualRebate;
    protected Gift gift = new Gift();
    protected Product product = new Product();
    protected String identifier = "Producto";
    ObservableList<IChangeable> products = FXCollections.observableArrayList(Request.getJ("products/basics/list",Product[].class,false));
    ObservableList<IChangeable> gifts = FXCollections.observableArrayList(Request.getJ("gifts/criteria-basic",Gift[].class,true));
    ObservableList<String> types = FXCollections.observableArrayList("Existencia", "Temporada");
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tipoComboBox.setItems(types);
        tipoComboBox.setValue("Existencia");
        startDatePicker.setOnAction(actionEvent -> {
            endDatePicker.setDayCellFactory(d ->
                    new DateCell() {
                        @Override public void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);
                            setDisable(item.isBefore(endDatePicker.getValue()));
                        }});
            if (endDatePicker.getValue().compareTo(startDatePicker.getValue()) > 0){
                endDatePicker.setValue(startDatePicker.getValue().plusDays(1));
            }
        });
        objectButton.setOnMouseClicked(mouseEvent -> {
            verifyClassType(actualRebate);
            if (identifier.equals("Producto")){
                products.remove(product);
                Product productR = (Product) loadDialog(products);
                if (productR != null){
                    products.add(product);
                    objectName.setText(productR.getName());
                    products.remove(productR);
                    product = productR;
                }
            }else {
                gifts.remove(gift);
                Gift giftR = (Gift) loadDialog(gifts);
                if (giftR != null){
                    objectName.setText(giftR.getName());
                    gifts.add(gift);
                    gifts.remove(giftR);
                    gift = giftR;
                }
            }
        });
    }

    @Override
    public void setInfo(Rebate rebate) {
        rebate.setStartDate(LocalDateTime.parse(startDatePicker.getValue().atTime(LocalTime.now()).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).substring(0,19), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        rebate.setEndDate(LocalDateTime.parse(endDatePicker.getValue().atTime(LocalTime.now()).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).substring(0,19), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        rebate.setPercent(Integer.parseInt(porcentajeField.getText()));
        rebate.setType(tipoComboBox.getValue());
        if (identifier.equals("Producto")){

        }else {
            ((GiftRebate) rebate).setGift(gift);
        }
    }

    public IChangeable loadDialog( ObservableList<IChangeable> items){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/select_list_containers.fxml"));
        try {
            SelectListObject dialogController = new SelectListObject();
            fxmlLoader.setController(dialogController);
            Parent parent = fxmlLoader.load();
            dialogController.setObject(items);
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
            IChangeable receivedObject = (IChangeable) stage.getUserData();
            return receivedObject;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Rebate getInstance(){
        if (identifier.equals("Producto")){
            ProductRebate productRebate = new ProductRebate();
            productRebate.setProduct(product);
            return productRebate;
        }else {
            GiftRebate productRebate = new GiftRebate();
            productRebate.setGift(gift);
            return productRebate;
        }
    }
    public void verifyClassType(Rebate rebate){
        if (rebate.getClass() == ProductRebate.class){
            identifier = "Producto";
        }else {
            identifier = "Regalo";
        }

    }
}
