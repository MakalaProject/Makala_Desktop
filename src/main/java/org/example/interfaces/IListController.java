package org.example.interfaces;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
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

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

public interface IListController<D>{
    default void deleteAlert(String element){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar " + element);
        alert.setHeaderText("Estas a punto de eliminar un " + element);
        alert.setContentText("¿Seguro quieres eliminarlo?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            delete();
        }
    };

    default boolean showAlertUnsavedElement(String elementName, String element){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(element + " sin guardar");
        alert.setHeaderText("Tienes cambios sin guardar de '" + elementName + "'");
        alert.setContentText("¿Quieres mantener los cambios?");
        ButtonType noBttn = new ButtonType("Salir sin guardar", ButtonBar.ButtonData.NO);
        Node closeButton = alert.getDialogPane().lookupButton(ButtonType.CANCEL);
        closeButton.managedProperty().bind(closeButton.visibleProperty());
        closeButton.setVisible(false);
        alert.getButtonTypes().add(noBttn);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            update();
            return false;
        }else return result.get().getText().equals("Salir sin guardar");
    }
    void delete();
    void update();
    boolean existChanges();
    void putFields();
    default void editView(AnchorPane anchorPane, ToggleSwitch editSwitch, FontAwesomeIconView updateButton){
        if (editSwitch.isSelected()){
            anchorPane.setVisible(false);
            updateButton.setVisible(true);
        }else {
            anchorPane.setVisible(true);
            updateButton.setVisible(false);
        }
    }
    default void editView(AnchorPane anchorPane, AnchorPane anchorPane2, Label infoLabel, ToggleSwitch editSwitch, FontAwesomeIconView updateButton){
        if (editSwitch.isSelected()){
            anchorPane.setVisible(false);
            anchorPane2.setVisible(false);
            infoLabel.setVisible(true);
            updateButton.setVisible(true);
        }else {
            anchorPane.setVisible(true);
            anchorPane2.setVisible(true);
            infoLabel.setVisible(false);
            updateButton.setVisible(false);
        }
    }
    void updateView();
    default void showList(ObservableList<D> List, ListView<D> listView, Class<?> controllerCell){
        listView.setItems(List);
        listView.getStylesheets().add(getClass().getResource("/configurations/style.css").toString());
        listView.prefHeightProperty().bind(Bindings.size(List).multiply(102));
        Parent stage  = listView.getParent();
        listView.setCellFactory(cellList -> {
            try {
                return (ListCell<D>) controllerCell.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        });
    }
    default void initialList(ListView listView){
        if (!listView.getItems().isEmpty()){
            listView.getSelectionModel().select(0);
            updateView();
        }
    }




    void cleanForm();
    default void add(String resource, ListView<D> listView, ObservableList<D> observableList, Class<?> controllerCell) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resource));
        try {
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
            D object = (D) stage.getUserData();
            if(object != null){
                observableList.add(object);
                showList(observableList,listView,controllerCell);
                listView.getSelectionModel().select(object);
                listView.scrollTo(object);
                updateView();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
