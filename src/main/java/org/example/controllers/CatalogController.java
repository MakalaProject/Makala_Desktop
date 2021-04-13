package org.example.controllers;

import org.example.customCells.CatalogListViewCell;
import org.example.customCells.GiftListViewCell;
import org.example.interfaces.IControllerCreate;
import org.example.interfaces.IListController;
import org.example.model.Catalog;
import org.example.model.CatalogClassification;
import org.example.model.Gift;
import org.example.services.CatalogService;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class CatalogController implements Initializable, IListController<Catalog>, IControllerCreate<Catalog> {
    @FXML
    FontAwesomeIconView updateButton;
    @FXML
    FontAwesomeIconView deleteButton;
    @FXML
    FontAwesomeIconView addButton;
    @FXML
    ListView<Catalog> listView;
    @FXML
    ListView<Gift> giftListView;
    @FXML
    TextField searchField;
    @FXML
    TextField nombreField;
    @FXML
    ComboBox<CatalogClassification> clasificacionComboBox;

    private ObservableList<Catalog> catalogObservableList;
    private ObservableList<Gift> giftObservableList;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        catalogObservableList = FXCollections.observableList(CatalogService.getCatalogs());
        showListCatalogs(catalogObservableList);
        FilteredList<Catalog> filteredCatalog = new FilteredList<>(FXCollections.observableArrayList(catalogObservableList), p ->true);
        //Check if the list is empty to update the view and show its values at the beggining
        if(!catalogObservableList.isEmpty()){
            listView.getSelectionModel().select(0);
            updateView();
        }
        searchField.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredCatalog.setPredicate(catalog -> {
                if (newValue.isEmpty()){
                    return true;
                }
                String lowerCaseText = newValue.toLowerCase();
                if (catalog.getName().toLowerCase().contains(lowerCaseText)){
                    return true;
                }else{
                    return false;
                }

            });
            SortedList<Catalog> sortedCatalogs = new SortedList<>(filteredCatalog);
            catalogObservableList = FXCollections.observableList(sortedCatalogs);
            showListCatalogs(catalogObservableList);
        } );

        listView.setOnMouseClicked(mouseEvent -> {
            giftObservableList = FXCollections.observableList(listView.getSelectionModel().getSelectedItem().getGifts());
            updateView();

        });

        updateButton.setOnMouseClicked(mouseEvent -> {
            /*Employee employee = listView.getSelectionModel().getSelectedItem();
            if (nombresField.getText().isEmpty()){
                Notifications.create().title("Campos vacios").text("No puedes dejar el campo 'Nombres' vacio").showWarning();
            }else{
                employee.setFirstName(nombresField.getText());
                employee.setLastName(apellidosField.getText());
                employee.setPhone(telefonoField.getText());
                if(contraseñaField.getText() != null){
                    employee.setPassword(contraseñaField.getText());
                }
                employeeObservableList.set(listView.getSelectionModel().getSelectedIndex(), employee);
                showListEmployees(employeeObservableList);
                EmployeeService.UpdateEmployee(employee);
                updateView();
            }*/
        });

        deleteButton.setOnMouseClicked(mouseEvent -> {
            /*EmployeeService.DeleteEmployee(listView.getSelectionModel().getSelectedItem().getId());
            employeeObservableList.remove(listView.getSelectionModel().getSelectedItem());
            showListEmployees(employeeObservableList);
            updateView();*/
        });

        addButton.setOnMouseClicked(mouseEvent -> {
            Catalog catalog = listView.getSelectionModel().getSelectedItem();
            catalog.setName("Nuevo catalogo");
            catalog = CatalogService.InsertCatalog(catalog);
            catalogObservableList.add(catalog);
            showListCatalogs(catalogObservableList);
            listView.scrollTo(catalog);
            listView.getSelectionModel().select(catalog);
            updateView();
        });


    }
    private void showListGifts(ObservableList<Gift> giftsList){
        giftListView.setItems(giftsList);
        giftListView.setCellFactory(catalogListView -> new GiftListViewCell());
    }

    @Override
    public void delete() {

    }

    @Override
    public void update() {

    }

    @Override
    public void add() {

    }

    @Override
    public boolean existChanges() {
        return false;
    }

    @Override
    public void putFields() {

    }



    @Override
    public void updateView(){
        nombreField.setText(listView.getSelectionModel().getSelectedItem().getName());
        showListGifts(giftObservableList);
    }

    @Override
    public void cleanForm() {

    }

    public void showList(ObservableList List) {

    }


    private void showListCatalogs(ObservableList<Catalog> catalogList){
        listView.setItems(catalogList);
        listView.setCellFactory(employeeListView -> new CatalogListViewCell());
    }
    @Override
    public Catalog setInfo(Catalog object) {
        return null;
    }
}
