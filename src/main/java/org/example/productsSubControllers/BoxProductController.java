package org.example.productsSubControllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.interfaces.ListToChangeTools;
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
    private final ObservableList<Hole> holeList = FXCollections.observableArrayList();
    private final Set<Hole> originalHoleList = new HashSet<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        addHoleButton.setOnMouseClicked(new ShowDialog(true));

        holesListView.setOnMouseClicked(new ShowDialog(false));
    }

    public void showList(){
        holesListView.setItems(FXCollections.observableList(holeList.stream().filter(hole -> !hole.isToDelete()).collect(Collectors.toList())));
    }

    @Override
    public String[] getIdentifier() {
        return new String[]{"Cajas"};
    }

    @Override
    public String getResource() {
        return "/fxml/box_product_properties.fxml";
    }


    public BoxProduct getObjectByFields() {
        if(!altoIntField.getText().isEmpty() || !anchoIntField.getText().isEmpty() || !largoIntField.getText().isEmpty()) {
            return super.getObjectByFields();
        }
        return null;
    }

    @Override
    public BoxProduct getObject(){
        BoxProduct boxProduct = super.getObject(BoxProduct.class);
        new ListToChangeTools<Hole,Integer>().setToDeleteItems(originalHoleList, holeList);
        boxProduct.setHolesDimensions(holeList);
        boxProduct.getInternalMeasures().setX(new BigDecimal(anchoIntField.getText()));
        boxProduct.getInternalMeasures().setY(new BigDecimal(altoIntField.getText()));
        boxProduct.getInternalMeasures().setZ(new BigDecimal(largoIntField.getText()));
        return boxProduct;

    }

    public void clearController(){
        holeList.clear();
        originalHoleList.clear();
    }

    @Override
    public void setObject(BoxProduct boxProduct){
        clearController();
        if (boxProduct.getHolesDimensions() != null){
            for (Hole hole : boxProduct.getHolesDimensions()) {
                originalHoleList.add(new Hole(hole.getHoleNumber(), hole.getHoleDimensions(),false));
            }
        }
        holeList.setAll(boxProduct.getHolesDimensions());
        showList();
        anchoIntField.setText(String.valueOf(boxProduct.getInternalMeasures().getX()));
        altoIntField.setText(String.valueOf(boxProduct.getInternalMeasures().getY()));
        largoIntField.setText(String.valueOf(boxProduct.getInternalMeasures().getZ()));
        super.setObject(boxProduct);
    }

    @Override
    public BoxProduct findObject(Product object) {
        return findObject(object,"products/boxes", BoxProduct.class );
    }

    class ShowDialog implements EventHandler<MouseEvent> {
        private final boolean isCreate;
        ShowDialog(boolean isCreate){
            this.isCreate = isCreate;
        }
        @Override
        public void handle(MouseEvent mouseEvent) {
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
                if (hole.getHole() != null) {
                    if (isCreate){
                        holeList.add(hole.getHole());
                    }else {
                        if (hole.getAction() == Action.DELETE){
                            holeList.remove(hole.getHole());
                        }else {
                            holeList.set(holeList.indexOf(holesListView.getSelectionModel().getSelectedItem()), hole.getHole());
                        }
                    }
                    int index = 1;
                    for (Hole holeIn : holeList){
                        holeIn.setHoleNumber(index);
                        index ++;
                    }
                    showList();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
