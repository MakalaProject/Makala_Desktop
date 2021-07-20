package org.example.controllers.create.controllers;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.example.controllers.parent.controllers.DecorationsParentController;
import org.example.interfaces.IControllerCreate;
import org.example.model.Bow;
import org.example.model.Catalog;
import org.example.services.ImageService;
import org.example.services.Request;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DecorationCreateController extends DecorationsParentController implements Initializable, IControllerCreate<Bow> {

    Bow decoration = new Bow();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        updateButton.setOnMouseClicked(mouseEvent -> {
            if(!priceField.getText().isEmpty() && !descriptionTextArea.getText().isEmpty() && !priceField.getText().isEmpty()){
                if(Integer.parseInt(priceField.getText()) > 0) {
                    setInfo(decoration);
                    Bow returnedDecoration = null;
                    try {
                        returnedDecoration = (Bow) Request.postJ(decoration.getRoute(),decoration);
                        decoration.setId(returnedDecoration.getId());
                        if(!decoration.getPath().equals("/images/bow.png")) {
                            List<String> images = new ArrayList<>();
                            images.add(decoration.getPath());
                            images = ImageService.uploadImages(images);
                            decoration.setPath(images.get(0));
                            decoration = (Bow) Request.putJ(decoration.getRoute(), decoration);
                        }
                    } catch (Exception e) {
                        duplyElementAlert(decoration.getIdentifier());
                        return;
                    }
                    Node source = (Node)  mouseEvent.getSource();
                    Stage stage  = (Stage) source.getScene().getWindow();
                    stage.close();
                    stage.setUserData(decoration);
                }else{
                    checkFields();
                    showAlertEmptyFields("El valor del precio no puede ser 0");
                }
            }else{
                checkFields();
                showAlertEmptyFields("No puedes dejar campos indispensables vacios");
            }
            checkFields();
        });
    }

    @Override
    public void setInfo(Bow decoration) {
        decoration.setName(nameField.getText());
        if(imageFile != null) {
            decoration.setPath(imageFile);
        }
        decoration.setDescription(descriptionTextArea.getText());
        decoration.setPrice(new BigDecimal(priceField.getText()));
    }
}
