package org.example.interfaces;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.ToggleSwitch;
import org.example.customCells.IConstructor;
import org.example.customCells.ProductListViewCell;

import java.io.IOException;
import java.util.Optional;

public interface IListController<D>{
    default void deleteAlert(String element){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Image image = new Image(getClass().getResource("/Images/delete.png").toString(), 50, 70, false, false);
        ImageView imageView = new ImageView(image);
        alert.setGraphic(imageView);
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
        Image image = new Image(getClass().getResource("/Images/delete.png").toString(), 50, 70, false, false);
        ImageView imageView = new ImageView(image);
        alert.setGraphic(imageView);
        alert.setTitle(element + " sin guardar");
        alert.setHeaderText("Tienes cambios sin guardar de '" + elementName + "'");
        alert.setContentText("¿Quieres mantener los cambios?");
        ButtonType noBttn = new ButtonType("No guardar", ButtonBar.ButtonData.NO);
        Node closeButton = alert.getDialogPane().lookupButton(ButtonType.CANCEL);
        closeButton.managedProperty().bind(closeButton.visibleProperty());
        closeButton.setVisible(false);
        alert.getButtonTypes().add(noBttn);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            update();
            return false;
        }else return result.get().getText().equals("No guardar");
    }
    void delete(
    );
    void update();
    void add ();
    boolean existChanges();
    void putFields();
    default void editView(AnchorPane anchorPane, ToggleSwitch editSwitch, FontAwesomeIconView updateButton){
        if (editSwitch.isSelected()){
            anchorPane.setDisable(false);
            updateButton.setVisible(true);
        }else {
            anchorPane.setDisable(true);
            updateButton.setVisible(false);
        }
    }
    void updateView() throws IOException;
    default void showList(ObservableList<D> List, ListView<D> listView, Class<?> controllerCell){
        listView.setItems(List);
        listView.setCellFactory(cellList -> {
            try {
                return (ListCell<D>) controllerCell.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        });
    }
    void cleanForm();
}
