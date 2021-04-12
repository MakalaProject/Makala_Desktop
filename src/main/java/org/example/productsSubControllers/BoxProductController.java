package org.example.productsSubControllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.shape.Box;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.controllers.ProductController;
import org.example.model.products.BoxProduct;
import org.example.model.products.Hole;
import org.example.model.products.Product;
import org.example.services.Request;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BoxProductController extends StaticParentProductController<BoxProduct> {
    @FXML public TextField altoIntField;
    @FXML public TextField anchoIntField;
    @FXML public TextField largoIntField;
    @FXML public FontAwesomeIconView addHoleButton;
    @FXML public ListView<Hole> holesListView;
    private final ObservableList<Hole> holeList = FXCollections.observableArrayList();
    private final List<Hole> originalHoleList = new ArrayList<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        addHoleButton.setOnMouseClicked(mouseEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/box_hole_properties.fxml"));
            try {
                Parent parent = fxmlLoader.load();
                HoleController dialogController = fxmlLoader.<HoleController>getController();
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
                Hole hole = (Hole) stage.getUserData();
                if (hole != null) {
                    holeList.add(hole);
                    showList();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        holesListView.


    }

    public void showList(){
        holesListView.setItems(FXCollections.observableList(holeList));
    }

    @Override
    public String[] getIdentifier() {
        return new String[]{"Caja"};
    }

    @Override
    public String getResource() {
        return "/fxml/static_box_properties.fxml";
    }


    public BoxProduct getObjectByFields() {
        if(!altoIntField.getText().isEmpty() || !anchoIntField.getText().isEmpty() || !largoIntField.getText().isEmpty()) {
            super.getObjectByFields();
        }
        return null;
    }

    @Override
    public BoxProduct getObject(){
        BoxProduct boxProduct = super.getObject(BoxProduct.class);
        boxProduct.setHolesDimensions(holeList);
        boxProduct.getInternalMeasures().setX(new BigDecimal(anchoField.getText()));
        boxProduct.getInternalMeasures().setY(new BigDecimal(altoField.getText()));
        boxProduct.getInternalMeasures().setZ(new BigDecimal(largoField.getText()));
        return boxProduct;
    }

    @Override
    public void setObject(BoxProduct boxProduct){
        originalHoleList.addAll(boxProduct.getHolesDimensions());
        super.setObject(boxProduct);
        holeList.setAll(boxProduct.getHolesDimensions());
        showList();
        anchoIntField.setText(String.valueOf(boxProduct.getInternalMeasures().getX()));
        altoIntField.setText(String.valueOf(boxProduct.getInternalMeasures().getY()));
        largoIntField.setText(String.valueOf(boxProduct.getInternalMeasures().getZ()));
    }

    @Override
    public BoxProduct findObject(Product object) {
        return (BoxProduct) Request.find("products/boxes", object.getIdProduct(), BoxProduct.class);
    }
}
