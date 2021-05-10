package org.example.controllers.create.controllers;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.example.controllers.parent.controllers.CatalogParentController;
import org.example.interfaces.IControllerCreate;
import org.example.model.Catalog;
import org.example.model.Employee;
import org.example.model.Picture;
import org.example.model.products.Product;
import org.example.services.ImageService;
import org.example.services.Request;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CatalogCreateController extends CatalogParentController implements Initializable, IControllerCreate<Catalog> {

    Catalog catalog = new Catalog();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        updateButton.setOnMouseClicked(mouseEvent -> {
            if(!nombreField.getText().isEmpty()){
                setInfo(catalog);
                Catalog returnedCatalog = null;
                try {
                    returnedCatalog = (Catalog) Request.postJ(catalog.getRoute(),catalog);
                    catalog.setIdCatalog(returnedCatalog.getIdCatalog());
                    if(!catalog.getPath().equals("/images/catalog.png")) {
                        List<String> images = new ArrayList<>();
                        images.add(catalog.getPath());
                        images = ImageService.uploadImages(images);
                        catalog.setPath(images.get(0));
                        returnedCatalog = (Catalog) Request.putJ(catalog.getRoute(), catalog);
                    }
                } catch (Exception e) {
                    duplyElementAlert(catalog.getIdentifier());
                    return;
                }
                Node source = (Node)  mouseEvent.getSource();
                Stage stage  = (Stage) source.getScene().getWindow();
                stage.close();
                stage.setUserData(returnedCatalog);
            }else{
                showAlertEmptyFields("No puedes dejar campos indispensables vacios");
            }
        });
    }



    @Override
    public void setInfo(Catalog catalog) {
        catalog.setName(nombreField.getText());
        catalog.setCatalogClassification(clasificacionComboBox.getValue());
        catalog.setGifts(giftObservableList);
        if(imageFile != null) {
            catalog.setPath(imageFile);
        }
    }
}
