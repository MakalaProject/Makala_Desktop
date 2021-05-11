package org.example.controllers.list.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.ToggleSwitch;
import org.example.controllers.parent.controllers.ManualParentController;
import org.example.customCells.ManualListViewCell;
import org.example.customCells.ProductListViewCell;
import org.example.interfaces.IControllerCreate;
import org.example.interfaces.IListController;
import org.example.model.Catalog;
import org.example.model.Manual;
import org.example.model.products.Product;
import org.example.services.Request;

import java.net.URL;
import java.util.ResourceBundle;

public class ManualController extends ManualParentController implements Initializable, IListController<Manual>, IControllerCreate<Manual> {

    @FXML ListView<Manual> listView;
    @FXML ComboBox<String> comboBox;
    @FXML TextField searchField;
    @FXML AnchorPane fieldsAnchorPane;
    @FXML ToggleSwitch editSwitch;
    @FXML FontAwesomeIconView deleteButton;
    @FXML FontAwesomeIconView addButton;
    private Manual actualManual;
    private final ObservableList<Manual> manualObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        comboBox.getItems().addAll(typeItems);
        manualObservableList.addAll(Request.getJ("manuals", Manual[].class, true));

        FilteredList<Manual> filteredManuals = new FilteredList<>(manualObservableList, p ->true);
        searchField.textProperty().addListener((observable, oldValue, newValue) ->{
            if (!existChanges()) {
                filteredManuals.setPredicate(manual -> {
                    if (newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseText = newValue.toLowerCase();
                    if (manual.getName().toLowerCase().contains(lowerCaseText)) {
                        return true;
                    }
                    return false;
                });
                if (!filteredManuals.isEmpty()) {
                    showList(FXCollections.observableList(filteredManuals), listView, ManualListViewCell.class);
                }
            }
        } );

        //Select item list


        listView.setOnMouseClicked(mouseEvent -> {
            if (listView.getSelectionModel().getSelectedItem() != null && !existChanges()) {
                updateView();
            }
        });

        //CRUD Buttons
        updateButton.setOnMouseClicked(mouseEvent -> {
            updateButton.requestFocus();
            if (actualManual != null)
                update();
        });

        deleteButton.setOnMouseClicked(mouseEvent -> {
            deleteButton.requestFocus();
            deleteAlert("producto");
        });

        addButton.setOnMouseClicked(mouseEvent -> {
            addButton.requestFocus();
            if (!existChanges()){
                add();
            }
        });
    }

    @Override
    public void setInfo(Manual object) {

    }

    @Override
    public void delete() {

    }

    @Override
    public void update() {

    }

    @Override
    public boolean existChanges() {
        return false;
    }

    @Override
    public void putFields() {

    }

    @Override
    public void updateView() {

    }

    @Override
    public void cleanForm() {

    }

    public void add(){

    }
}
