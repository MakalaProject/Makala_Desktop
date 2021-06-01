package org.example.controllers.products.subcontrollers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.interfaces.IControllerCreate;
import org.example.interfaces.IListController;
import org.example.interfaces.ListToChangeTools;
import org.example.model.ChangedVerificationFields;
import org.example.model.FocusVerificationFields;
import org.example.model.GiftProductsToSend;
import org.example.model.products.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class BoxProductController extends StaticParentProductController<BoxProduct> {
    @FXML public TextField altoIntField;
    @FXML public TextField anchoIntField;
    @FXML public TextField largoIntField;
    @FXML public FontAwesomeIconView addHoleButton;
    @FXML public ListView<Hole> holesListView;
    @FXML public CheckBox isClosed;
    private ObservableList<Hole> holeList;
    private final Set<Hole> originalHoleList = new HashSet<>();
    private BoxProduct actualBoxProduct;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        actualBoxProduct = new BoxProduct();
        super.initialize(url,resourceBundle);
        holeList = FXCollections.observableArrayList();
        addHoleButton.setOnMouseClicked(new ShowDialog(true));
        holesListView.setOnMouseClicked(new ShowDialog(false));
        altoIntField.textProperty().addListener(new ChangedVerificationFields(altoIntField, true, 3,2));
        largoIntField.textProperty().addListener(new ChangedVerificationFields(largoIntField, true, 3,2));
        anchoIntField.textProperty().addListener(new ChangedVerificationFields(anchoIntField, true, 3,2));
        altoIntField.focusedProperty().addListener(new FocusVerificationFields(altoIntField, true, 3,2));
        largoIntField.focusedProperty().addListener(new FocusVerificationFields(largoIntField, true, 3,2));
        anchoIntField.focusedProperty().addListener(new FocusVerificationFields(anchoIntField, true, 3,2));
    }

    public void showList(){
        ObservableList<Hole> holes = FXCollections.observableList(holeList.stream().filter(hole -> !hole.isToDelete()).collect(Collectors.toList()));
        holes.sort(Comparator.comparing(Hole::getHoleNumber));
        holesListView.setItems(holes);
        holesListView.prefHeightProperty().bind(Bindings.size(holes).multiply(26.1));
    }

    @Override
    public String[] getIdentifier() {
        return new String[]{"Cajas"};
    }

    @Override
    public String getResource() {
        return "/fxml/box_product_properties.fxml";
    }


    @Override
    public BoxProduct getObject(){
        BoxProduct boxProduct = getLocalObject();
        if (boxProduct != null){
            if(!altoIntField.getText().isEmpty() && !anchoIntField.getText().isEmpty() && !largoIntField.getText().isEmpty()) {
                if (Float.parseFloat(altoIntField.getText()) > 0 && Float.parseFloat(anchoIntField.getText()) > 0 && Float.parseFloat(largoIntField.getText()) > 0) {
                    new ListToChangeTools<Hole, Integer>().setToDeleteItems(originalHoleList, holeList);
                    boxProduct.setHolesDimensions(holeList);
                    return boxProduct;
                } else {
                    showAlertEmptyFields("Las medidas no pueden ser 0");
                }
            }else {
                showAlertEmptyFields("No puedes dejar los campos de las medidas vacios");
            }
        }
        return new BoxProduct();
    }

    @Override
    public BoxProduct getObjectInstance() {
        return new BoxProduct();
    }

    public BoxProduct getLocalObject(){
        BoxProduct boxProduct = super.getObject(BoxProduct.class);
        if (boxProduct != null) {
            boxProduct.setHolesDimensions(holeList);
            boxProduct.setClosed(isClosed.isSelected());
            boxProduct.getInternalMeasures().setX(new BigDecimal(anchoIntField.getText()));
            boxProduct.getInternalMeasures().setY(new BigDecimal(altoIntField.getText()));
            boxProduct.getInternalMeasures().setZ(new BigDecimal(largoIntField.getText()));
            Measure3Dimensions internalMeasure = boxProduct.getInternalMeasures();
            Measure3Dimensions externalMeasures = boxProduct.getMeasures();
            if (externalMeasures.getZ().compareTo(internalMeasure.getZ()) > 0 && externalMeasures.getX().compareTo(internalMeasure.getX()) > 0 && externalMeasures.getY().compareTo(internalMeasure.getY()) > 0) {
                if (boxProduct.getTotalHolesArea().compareTo(boxProduct.getArea()) < 1) {
                    return boxProduct;
                }
            }
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Medidas fuera de rango");
            alert.setContentText("Las medidas de la caja son invalidas");
            alert.showAndWait();

        }
        return null;
    }

    public void clearController(){
        holeList.clear();
        originalHoleList.clear();
    }

    public void verifyAvailableArea(){
        boolean t = actualBoxProduct.getAvailableArea().compareTo(new BigDecimal(0)) != 0;
        addHoleButton.setVisible(t);
    }

    @Override
    public void setObject(BoxProduct boxProduct){
        actualBoxProduct = boxProduct;
        verifyAvailableArea();
        clearController();
        if (boxProduct.getHolesDimensions() != null){
            for (Hole hole : boxProduct.getHolesDimensions()) {
                originalHoleList.add(new Hole(hole.getHoleNumber(), hole.getHoleDimensions(),false));
            }
        }
        holeList.setAll(boxProduct.getHolesDimensions());
        showList();
        isClosed.setSelected(actualBoxProduct.isClosed());
        anchoIntField.setText(String.valueOf(boxProduct.getInternalMeasures().getX()));
        altoIntField.setText(String.valueOf(boxProduct.getInternalMeasures().getY()));
        largoIntField.setText(String.valueOf(boxProduct.getInternalMeasures().getZ()));
        super.setObject(boxProduct);
    }

    @Override
    public BoxProduct findObject(Product object) {
        return findObject(object,"products/boxes", BoxProduct.class );
    }

    public void alertOutOfBounds(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Medidas fuera de rango");
        alert.setContentText("El orificio que intentas crear excede el area de la caja, espacio disponible: " + actualBoxProduct.getAvailableArea() + "cm");
        alert.showAndWait();
    }

    @Override
    public boolean indispensableChanges(){
        BoxProduct boxProduct = super.getObject(BoxProduct.class);
        boxProduct.setHolesDimensions(holeList);
        boxProduct.getInternalMeasures().setX(new BigDecimal(anchoIntField.getText()));
        boxProduct.getInternalMeasures().setY(new BigDecimal(altoIntField.getText()));
        boxProduct.getInternalMeasures().setZ(new BigDecimal(largoIntField.getText()));
        Measure3Dimensions internalMeasure = boxProduct.getInternalMeasures();
        Measure3Dimensions externalMeasures = boxProduct.getMeasures();
        if (externalMeasures.getZ().compareTo(internalMeasure.getZ()) > 0 && externalMeasures.getX().compareTo(internalMeasure.getX()) > 0 && externalMeasures.getY().compareTo(internalMeasure.getY()) > 0) {
            return boxProduct.getTotalHolesArea().compareTo(boxProduct.getArea()) >= 1;
        }
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Medidas fuera de rango");
        alert.setContentText("Las medidas de la caja son invalidas");
        alert.showAndWait();
        return true;
    }

    @Override
    public void setInfo(StaticProduct object) {

    }

    class ShowDialog implements EventHandler<MouseEvent> {
        private final boolean isCreate;
        ShowDialog(boolean isCreate){
            this.isCreate = isCreate;
        }
        @Override
        public void handle(MouseEvent mouseEvent) {
            actualBoxProduct = getLocalObject();
            if (actualBoxProduct == null){
                return;
            }
            actualBoxProduct.setInternalMeasures( new Measure3Dimensions(new BigDecimal(anchoIntField.getText()), new BigDecimal(altoIntField.getText()), new BigDecimal(largoIntField.getText())));
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/box_hole_properties.fxml"));
            try {
                Parent parent = fxmlLoader.load();
                HoleController dialogController = fxmlLoader.<HoleController>getController();
                if (!isCreate){
                    dialogController.deleteButton.setVisible(true);
                    dialogController.setHole(holesListView.getSelectionModel().getSelectedItem());
                }else {
                    dialogController.deleteButton.setVisible(false);
                }
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
                HoleToSend hole = (HoleToSend) stage.getUserData();
                if (hole != null) {
                    if (isCreate){
                        if (hole.getHole().getArea().compareTo(actualBoxProduct.getAvailableArea()) > 0){
                            alertOutOfBounds();
                            return;
                        }
                        holeList.add(hole.getHole());
                    }else {
                        if (hole.getAction() == Action.DELETE){
                            holeList.remove(hole.getHole());
                        }else {
                            if (hole.getHole().getArea().compareTo(actualBoxProduct.getAvailableArea().add(holesListView.getSelectionModel().getSelectedItem().getArea())) > 0){
                                alertOutOfBounds();
                                return;
                            }
                            holeList.set(holeList.indexOf(holesListView.getSelectionModel().getSelectedItem()), hole.getHole());
                        }
                    }
                    int index = 1;
                    for (Hole holeIn : holeList){
                        holeIn.setHoleNumber(index);
                        index ++;
                    }
                    showList();
                    actualBoxProduct.setHolesDimensions(holeList);
                    verifyAvailableArea();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
