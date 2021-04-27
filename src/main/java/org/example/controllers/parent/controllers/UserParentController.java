package org.example.controllers.parent.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.ToggleSwitch;
import org.example.customCells.UserListViewCell;
import org.example.interfaces.IListController;
import org.example.model.User;
import org.example.services.Request;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class UserParentController <D extends User> extends UserGenericController<D> implements IListController<D> {

    @FXML protected AnchorPane fieldsAnchorPane;
    @FXML protected ToggleSwitch editSwitch;
    @FXML protected TextField searchField;
    @FXML protected FontAwesomeIconView deleteButton;
    @FXML protected FontAwesomeIconView addButton;
    @FXML protected ListView<D> listView;
    protected D actualUser;
    protected final ObservableList<D> userObservableList = FXCollections.observableArrayList();
    protected final FilteredList<D> filteredUsers = new FilteredList<>(FXCollections.observableArrayList(userObservableList), p ->true);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        showList();
        initialList(listView);

        listView.setOnMouseClicked(mouseEvent -> {
            if (!existChanges()) {
                updateView();
            }
        });
        updateButton.setOnMouseClicked(mouseEvent -> {
            if (actualUser != null)
                update();
        });
        editSwitch.setOnMouseClicked(mouseEvent -> {
            editView(fieldsAnchorPane,editSwitch,updateButton);
        });
        deleteButton.setOnMouseClicked(mouseEvent -> {
            deleteAlert(actualUser.getIdentifier().toLowerCase());
        });


    }

    @Override
    public void delete() {
        Request.deleteJ( actualUser.getRoute(), actualUser.getIdUser());
        if (listView.getItems().size() > 1) {
            userObservableList.remove(actualUser);
            listView.getSelectionModel().select(0);
            showList();
            updateView();
        }else {
            userObservableList.remove(actualUser);
            cleanForm();
            showList();
        }
    }

    public void showList(){
        showList(userObservableList, listView, UserListViewCell.class);
    }


    protected void editView() {
        editView(fieldsAnchorPane,editSwitch,updateButton);
    }


    @Override
    public void update() {
        if (!nombresField.getText().isEmpty() ){
            setInfo(actualUser);
            userObservableList.set(userObservableList.indexOf(actualUser), actualUser);
            showList();
            try {
                Request.putJ(actualUser.getRoute(), actualUser);
            } catch (Exception e) {
                e.printStackTrace();
            }
            editSwitch.setSelected(false);
            editView();
        }else{
            showAlertEmptyFields("No puedes dejar campos indispensables vacios");
        }
    }

    protected abstract D instanceObject();

    @Override
    public boolean existChanges() {
        if(actualUser ==null){
            return false;
        }
        D user = instanceObject();
        setInfo(user);
        if (!actualUser.equals(user)){
            if(!showAlertUnsavedElement(actualUser.getFirstName(), actualUser.getIdentifier())) {
                listView.getSelectionModel().select(actualUser);
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
        nombresField.setText(actualUser.getFirstName());
        apellidosField.setText(actualUser.getLastName());
        telefonoField.setText(actualUser.getPhone());
    }

    @Override
    public void updateView() {
        editSwitch.setSelected(false);
        editView();
        actualUser = listView.getSelectionModel().getSelectedItem();
        putFields();
    }

    @Override
    public void cleanForm() {
        nombresField.setText("");
        apellidosField.setText("");
        telefonoField.setText("");
    }


}
