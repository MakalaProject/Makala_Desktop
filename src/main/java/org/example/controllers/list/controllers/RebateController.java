package org.example.controllers.list.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.ToggleSwitch;
import org.example.controllers.parent.controllers.RebateParentController;
import org.example.customCells.RebateListViewCell;
import org.example.exceptions.ProductDeleteException;
import org.example.interfaces.IListController;
import org.example.model.GiftRebate;
import org.example.model.ProductRebate;
import org.example.model.Rebate;
import org.example.services.Request;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class RebateController extends RebateParentController implements IListController<Rebate> {
    @FXML FontAwesomeIconView deleteButton;
    @FXML FontAwesomeIconView addButton;
    @FXML
    TextField searchField;
    @FXML
    ListView<Rebate> listView;
    @FXML ToggleSwitch editSwitch;
    private final ObservableList<Rebate> rebatesObservableList = FXCollections.observableArrayList();
    private int index;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        rebatesObservableList.addAll(Request.getJ("gift-rebates", GiftRebate[].class,true));
        rebatesObservableList.addAll(Request.getJ("product-rebates", ProductRebate[].class, true));
        rebatesObservableList.setAll(rebatesObservableList.stream().filter(r -> r.getEndDate().compareTo(LocalDateTime.now())>-1).collect(Collectors.toList()));
        showList(rebatesObservableList,listView, RebateListViewCell.class);
        FilteredList<Rebate> rebateFilteredList = new FilteredList<>(rebatesObservableList, p ->true);
        searchField.textProperty().addListener((observable, oldValue, newValue) ->{
            if (!existChanges()) {
                rebateFilteredList.setPredicate(purchase -> {
                    if (newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseText = newValue.toLowerCase();
                    if (purchase.getEndDate().toString().toLowerCase().contains(lowerCaseText)){
                        return true;
                    }else if(purchase.getStartDate().toString().toLowerCase().contains(lowerCaseText)){
                        return true;
                    }else if (purchase.getClass() == ProductRebate.class && ((ProductRebate) purchase).getProduct().getName().toLowerCase().contains(lowerCaseText)){
                        return true;
                    }else return purchase.getClass() == GiftRebate.class && ((GiftRebate) purchase).getGift().getName().toLowerCase().contains(lowerCaseText);
                });
                if (!rebateFilteredList.isEmpty()) {
                    showList(FXCollections.observableList(rebateFilteredList), listView, RebateListViewCell.class);
                }
            }
        } );
        if (listView.getItems().size() > 0 ){
            listView.getSelectionModel().select(0);
            actualRebate = listView.getItems().get(0);
            updateView();
        }else {
            actualRebate = null;
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
            if (actualRebate != null)
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
            deleteAlert(" rebaja ");
        });
    }
    public void add() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/rebate_create.fxml"));
        try {
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
            Rebate object = (Rebate) stage.getUserData();
            if(object != null){
                actualRebate = object;
                rebatesObservableList.add(object);
                showList(rebatesObservableList,listView, RebateListViewCell.class);
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
            Request.deleteJ( actualRebate.getRoute(), actualRebate.getId());
        } catch (Exception e) {
            errorAlert(e.getMessage());
            return;
        }
        if (listView.getItems().size() > 1) {
            rebatesObservableList.remove(index);
            listView.getSelectionModel().select(0);
            updateView();
            showList(rebatesObservableList, listView, RebateListViewCell.class);
        }else {
            actualRebate = null;
            rebatesObservableList.remove(index);
            showList(rebatesObservableList, listView, RebateListViewCell.class);
            cleanForm();
        }
    }

    @Override
    public void update() {
        checkFields();
        Rebate rebate = actualRebate;
        if (!porcentajeField.getText().isEmpty()) {
            if (Integer.parseInt(porcentajeField.getText())>0) {
                verifyClassType(rebate);
                setInfo(rebate);
                try {
                    Rebate rebateR = (Rebate) Request.putJ(actualRebate.getRoute(), rebate);
                    rebatesObservableList.set(index, rebate);
                    actualRebate = rebateR;
                    listView.setItems(rebatesObservableList);
                    showList(rebatesObservableList, listView, RebateListViewCell.class);
                    listView.getSelectionModel().select(actualRebate);
                    listView.scrollTo(actualRebate);
                    editSwitch.setSelected(false);
                    editView(fieldsAnchorPane, editSwitch, updateButton);
                    updateView();
                } catch (ProductDeleteException e) {
                    e.printStackTrace();
                }
            }else{
                showAlertEmptyFields("El porcentaje no puede ser 0");
            }
        }else{
            showAlertEmptyFields("No puedes dejar el campo de porcentaje vacio");
        }

    }


    @Override
    public boolean existChanges() {
        if(actualRebate == null){
            return false;
        }
        verifyClassType(actualRebate);
        Rebate rebate = getInstance();
        setInfo(rebate);
        if (!actualRebate.equals(rebate)){
            if(!showAlertUnsavedElement(rebate.getIdentifier().toString(), rebate.getIdentifier())) {
                listView.getSelectionModel().select(rebate);
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
        startDatePicker.setValue(actualRebate.getStartDate().toLocalDate());
        endDatePicker.setValue(actualRebate.getEndDate().toLocalDate());
        porcentajeField.setText(actualRebate.getPercent().toString());
        if (actualRebate.getClass() == ProductRebate.class){
            objectLabel.setText("Producto");
            tipoComboBox.setVisible(true);
            rebajaLabel.setVisible(true);
            tipoComboBox.setValue(actualRebate.getType());
            objectName.setText(((ProductRebate)actualRebate).getProduct().getName());
        }else {
            objectLabel.setText("Regalo");
            tipoComboBox.setVisible(false);
            rebajaLabel.setVisible(false);
            objectName.setText(((GiftRebate)actualRebate).getGift().getName());
        }
    }

    @Override
    public void updateView() {
        actualRebate = listView.getSelectionModel().getSelectedItem();
        index = rebatesObservableList.indexOf(listView.getSelectionModel().getSelectedItem());
        editSwitch.setSelected(false);
        updateButton.setVisible(true);
        editView(fieldsAnchorPane, editSwitch, updateButton);
        putFields();
    }

    @Override
    public void cleanForm() {
        objectName.setText("");
        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(LocalDate.now());
    }
}
